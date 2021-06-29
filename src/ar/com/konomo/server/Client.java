package ar.com.konomo.server;


import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.BoardUpdater;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.operators.NinjaPlacer;
import ar.com.konomo.server.handlers.ReadyHandler;
import ar.com.konomo.validators.WinValidator;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private Player player;
    private AttackLogger attackLogger;
    private Requester requester;
    private Display display;
    private WinValidator winValidator;
    private Map<Integer, Intention> playerIntentions = new HashMap<>();
    private BoardUpdater boardUpdater = new BoardUpdater();
    private EventMessageLog eventMessageLog = new EventMessageLog();

    public static volatile GameState gameState = GameState.ON;



    public void play(){
        while(gameState == GameState.ON) {

            while (!ReadyHandler.isReady()) ;
            ReadyHandler.setReady(false);
            Message message;
            try {
                attackLogger = requester.sendGet("/hitMe");

            } catch (Exception ex) {
                System.out.println("exception en el attacklogger vieja");
                System.out.println(ex.getMessage());
            }

            message = (Message) requester.sendGet("/events", String.class);

            if (!attackLogger.getAttackLog().isEmpty()) {
                updateBoard(attackLogger.getAttackLog());

                try{
                    boardUpdater.getEventLog().show();
                    //gameManager.getEventLog().show();
                }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }


                //si me mataron todos los ninjas, hago un post para cambiar la variable de ON a OVER

                if (winValidator.allNinjasKilled(player)) {
                    //requester.setIp(HandshakeHandler.getIp()+":"+"8000");
                   // requester.setIp("127.0.0.1:8001");
                    String json = Converter.toJson(player.getMyNinjas());
                    requester.sendPost(json, "/gameState"); //<-- mando mis ninjas para que los actualice
                    gameState = GameState.OVER; // <-- por si se me retovó el volatile (?)
                }
            }
            display.retrieveBoard(player);
            /**
             * //pido las intenciones al cliente y las valido... salgo de acá con las intenciones para actualizar en el board
             */
            playerIntentions = getClientIntentions();

            try{
                player.setEnemyBoard(sendClientIntentions(playerIntentions));
            }
            catch (Exception ex){
                System.out.println("rompimo todo");
            }


            List<Shinobi> movedNinjas = new ArrayList<>();
            for (Map.Entry<Integer, Intention> intention : playerIntentions.entrySet()
            ) {
                Shinobi myNinja = player.getMyNinjas().get(intention.getKey());
                if (intention.getValue().getAction() == Action.MOVE) {
                    movedNinjas.add(myNinja);
                    boardUpdater.update(player.getLocalBoard(),intention.getValue(), myNinja);
                    myNinja.setLastActionTaken(Action.MOVE);
                }
                NinjaPlacer.place(movedNinjas, player.getLocalBoard());
            }
            try {
                message = (Message) requester.sendGet("/events", String.class).getBody();
                LinkedTreeMap<String, ArrayList<String>> messages = (LinkedTreeMap<String, ArrayList<String>>) message.getBody();
                EventMessageLog eventMessageLog = new EventMessageLog();

                eventMessageLog.setPlayerLog(messages.get("playerLog")) ;

                for (String log: eventMessageLog.getPlayerLog()
                ) {
                    System.out.println(log);
                }

            } catch (Exception ex) {
                System.out.println("exception en el mensaje vieja");
                System.out.println(ex.getMessage());
            }
            display.retrieveBoard(player);


            requester.sendGet("/ready", Message.class);
        }

    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void updateBoard(Map <Integer, Intention> attackList) {
        eventMessageLog.getPlayerLog().clear();

        for (Map.Entry<Integer, Intention> record: attackList.entrySet()
        ) {
            Coordinate target = record.getValue().getCoordinate();
            boardUpdater.update(player.getLocalBoard(), target);

        }
        eventMessageLog = boardUpdater.getEventLog();
        attackList.clear();
    }

    private String[][] sendClientIntentions( Map<Integer, Intention> clientIntentions) {

        List<Intention> intentionList = new ArrayList<>();
        IntentionPack intentionPack = new IntentionPack(intentionList, player.getMyNinjas(), false, player.getEnemyBoard());
        for (Map.Entry<Integer, Intention> entry : clientIntentions.entrySet()
        ) {
            intentionList.add(entry.getValue());
        }
        OpError errors;

        try {

            intentionPack = requester.sendPost(intentionPack, "/actions");

            if (intentionPack == null) {
                System.out.println("Conexión rechazada!");
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return intentionPack.getKnownBoard();
    }

    private Map<Integer, Intention>  getClientIntentions(){
        Map<Integer, Intention>  playerIntentions = display.gamePlay(player);

        List<Intention> intentionList = new ArrayList<>();
        for (Map.Entry<Integer,Intention> entry: playerIntentions.entrySet()
        ) {
            intentionList.add(entry.getValue());
        }
        OpError errors;


        try {
            IntentionPack intentionPack = new IntentionPack(intentionList, player.getMyNinjas(),false, player.getEnemyBoard());
            intentionPack = requester.sendPost(intentionPack, "/intentions");

            if (intentionPack == null) {
                System.out.println("Conexión rechazada!");
                return null;
            }

            while (!intentionPack.allGood ) {
                intentionList.clear(); //<-- evaluar si acá debo limpiar la lista...
                errors = intentionPack.getErrors();
                playerIntentions = display.ammendIntentions(playerIntentions, errors, player);


                intentionList = new ArrayList<>(); //mepa q esto está al pedo si lo estoy inicializando arriba
                for (Map.Entry<Integer,Intention> entry: playerIntentions.entrySet()
                ) {
                    intentionList.add(entry.getValue());
                }
                intentionPack.setIntentions(intentionList);
                errors = new OpError();
                intentionPack.setErrors(errors);


                intentionPack = requester.sendPost(intentionPack, "/intentions");

            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return playerIntentions;
    }


    public void setRequester(Requester requester) {
        this.requester = requester;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public WinValidator getWinValidator() {
        return winValidator;
    }

    public void setWinValidator(WinValidator winValidator) {
        this.winValidator = winValidator;
    }


}