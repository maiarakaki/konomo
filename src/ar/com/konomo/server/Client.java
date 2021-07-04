package ar.com.konomo.server;


import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.BoardUpdater;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.operators.NinjaPlacer;
import ar.com.konomo.server.handlers.GameStateHandler;
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



    public void play(){
        while(GameStateHandler.getGameState() == GameState.ON) {

            while (!ReadyHandler.isReady()) ;
            ReadyHandler.setReady(false);

            statusUpdate();

            if (GameStateHandler.getGameState() == GameState.ON){
                display.retrieveBoard(player);

                playerIntentions = getClientIntentions();

                try{
                    player.setEnemyBoard(sendClientIntentions(playerIntentions));
                    moveNinjas();
                    display.retrieveBoard(player);
                    showResults();
                }
                catch (Exception ex){
                    System.out.println("rompimo todo");
                }


                requester.sendGet("/ready", Message.class);
                while (!ReadyHandler.isReady()) ;

            }

        }
        display.newScreen ("GAME OVER");
        if (winValidator.allNinjasKilled(player)) {
            System.out.println("Perdiste D=");
        } else {
            System.out.println("Ganaste!");
        }

    }

    private void statusUpdate(){
        try {
            attackLogger = requester.sendGet("/hitMe");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (!attackLogger.getAttackLog().isEmpty()) {
            updateBoard(attackLogger.getAttackLog());
            attackLogger.getAttackLog().clear();

            boardUpdater.getEventLog().show();


            if (winValidator.allNinjasKilled(player)) {
                GameStateHandler.setGameState(GameState.OVER);
            }
        }
    }

    private void showResults(){
        try {
            Message message = (Message) requester.sendGet("/events", String.class).getBody();
            LinkedTreeMap<String, ArrayList<String>> messages = (LinkedTreeMap<String, ArrayList<String>>) message.getBody();
            EventMessageLog eventMessageLog = new EventMessageLog();

            eventMessageLog.setPlayerLog(messages.get("playerLog")) ;

            for (String log: eventMessageLog.getPlayerLog()
            ) {
                System.out.println(log);
            }
          //  display.retrieveBoard(player);  VER SI NO TENIA Q DECOMENTAR ESTE


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void moveNinjas(){
        List<Shinobi> movedNinjas = new ArrayList<>();
        for (Map.Entry<Integer, Intention> intention : playerIntentions.entrySet()
        ) {
            try {
                Shinobi myNinja = player.getMyNinjas().get(intention.getKey());
                if (!myNinja.isAlive()) {
                    myNinja = player.getMyNinjas().get(intention.getKey()+1);
                }

                if (intention.getValue().getAction() == Action.MOVE) {
                    movedNinjas.add(myNinja);
                    boardUpdater.update(player.getLocalBoard(),intention.getValue(), myNinja);
                    myNinja.setLastActionTaken(Action.MOVE);
                }
                NinjaPlacer.place(movedNinjas, player.getLocalBoard());
            } catch (NullPointerException ex) {
                //if a Null pointer exception occurs, I just need to skip this intention
            }
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

    public void setWinValidator(WinValidator winValidator) {
        this.winValidator = winValidator;
    }


}