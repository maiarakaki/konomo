package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.display.Initializer;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.BoardUpdater;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.server.*;
import ar.com.konomo.server.handlers.HandshakeHandler;
import ar.com.konomo.server.handlers.ReadyHandler;
import ar.com.konomo.validators.WinValidator;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class Game {

    private Server server;
    private Client client;
    private GM gameManager;
    private Display display;
    private static Player player1;
    private static Player player2;
    private WinValidator winValidator;
    private List<Coordinate> coordinates;
   // private Player playerInTurn;
    private Player adversary;
    public static GameMode mode;
    private Initializer initializer;
    private Requester requester;

    public static volatile GameState gameState = GameState.ON;
    public static volatile Player playerInTurn;



    public Game (){
        gameManager = new GM();
        display = new Display();
        requester = new Requester();
        initializer = new Initializer(display, gameManager, requester);
        server = new Server(gameManager, initializer);
        coordinates = new ArrayList<>();
        winValidator = new WinValidator();
        gameManager.createGame();
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
    }



    public void start(){
        gameManager.createGame();
        initializer.initiate();
        requester.setIp("127.0.0.1:8001");
/*        player1 = initializer.getPlayer(GameMode.HOST);
        player2 = initializer.getPlayer(GameMode.GUEST);*/
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        mode = initializer.getGameMode();

        play();

    }





    public void play(){
      //  boolean gameOver = winValidator.winConditionsMet(player1, player2);
        boolean gameOver;
        boolean allGood;
        AttackLogger attackLogger = new AttackLogger();

        while (gameState == GameState.ON) {
            Map<Integer, Intention> playerIntentions = new HashMap<>();
           // while (!ReadyHandler.isReady()) ;


            if (mode == GameMode.HOST) {
                playerInTurn = player1;


                /**
                 * en teoría el attacklogguer "VIVE" en el manager, en el server... asi que en teoría cuando valido las intenciones del cliente,
                 * debería ser capaz de analizarlo localmente
                 */
                attackLogger = gameManager.getAttackLogs();


                if (!attackLogger.getAttackLog().isEmpty()) {
                    gameManager.updateBoards(playerInTurn, attackLogger.getAttackLog());
                    gameManager.getEventLog().show();
                    gameOver = winValidator.winConditionsMet(player1, player2);

                    if (gameOver) {
                        gameState = GameState.OVER;
                    }
                }

                if (gameState == GameState.ON) { //<- soy el servidor, sé que el juego no terminó
                    display.retrieveBoard(playerInTurn); //<-le muestro el board al jugador en juego y le pido sus intenciones
                    playerIntentions = display.gamePlay(playerInTurn);
                    allGood = gameManager.validate(playerIntentions, playerInTurn); //el manager valida las intenciones ... ACÁ NECESITO BIFURCAR SI ES CLIENTE

                    while (!allGood) {
                        OpError errors = gameManager.getErrors();
                        playerIntentions = display.ammendIntentions(playerIntentions, errors, playerInTurn);
                        allGood = gameManager.validate(playerIntentions, playerInTurn);
                    }
                    gameManager.updateBoards(playerInTurn, playerIntentions, adversary.getLocalBoard());
                    gameManager.getEventLog().show();
                    display.retrieveBoard(playerInTurn);



                    // gameOver = winValidator.winConditionsMet(player1, player2);

                    System.out.println("==============FIN DE TURNO=============");
                    requester.sendGet("/ready", Message.class);

                    while (!ReadyHandler.isReady()) {

                    }
                }
            } else {
                while (!ReadyHandler.isReady()) ;
                ReadyHandler.setReady(false);

                if (mode == GameMode.GUEST) {
                    player2 = initializer.getPlayer(GameMode.GUEST);
                    playerInTurn = player2;
/**
 * intento de recuperar el attack logger como dior manda..
 */
                    Message message;
                    try {
                        attackLogger = requester.sendGet("/hitMe");

                    } catch (Exception ex) {
                        System.out.println("exception en el attacklogger vieja");
                        System.out.println(ex.getMessage());
                    }

                    message = (Message) requester.sendGet("/events", String.class);

                    if (!attackLogger.getAttackLog().isEmpty()) {
                        gameManager.updateBoards(playerInTurn, attackLogger.getAttackLog());

                        try{
                            gameManager.getEventLog().show();
                        }catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }

                      /*  EventMessageLog eventLog = Converter.fromJson(message.getMessage(), EventMessageLog.class);

                        try {
                            message = (Message) requester.sendGet("/events", String.class).getBody();
                            LinkedTreeMap<String, ArrayList<String>> messages = (LinkedTreeMap<String, ArrayList<String>>) message.getBody();
                            EventMessageLog eventMessageLog = new EventMessageLog();

                            eventMessageLog.setPlayerLog(messages.get("playerLog")) ;
                            eventMessageLog.show();
*/
/*                            for (String log: eventMessageLog.getPlayerLog()
                            ) {
                                System.out.println(log);
                            }

                        } catch (Exception ex) {
                            System.out.println("exception en el mensaje vieja");
                            System.out.println(ex.getMessage());
                        }*/

                        //si me mataron todos los ninjas, hago un post para cambiar la variable de ON a OVER

                        if (winValidator.allNinjasKilled(playerInTurn)) {
                            requester.setIp("127.0.0.1:8001");
                            String json = Converter.toJson(playerInTurn.getMyNinjas());
                            requester.sendPost(json, "/gameState"); //<-- mando mis ninjas para que los actualice
                        }
                    }
                    display.retrieveBoard(playerInTurn);
                    /**
                     * //pido las intenciones al cliente y las valido... salgo de acá con las intenciones para actualizar en el board
                     */
                    playerIntentions = getClientIntentions();

                    try{
                        player2.setEnemyBoard(sendClientIntentions(playerIntentions));
                    }
                    catch (Exception ex){
                        System.out.println("rompimo todo");
                    }


                    List <Shinobi> movedNinjas = new ArrayList<>();
                    for (Map.Entry<Integer, Intention> intention : playerIntentions.entrySet()
                         ) {
                        Shinobi myNinja = player2.getMyNinjas().get(intention.getKey());
                        if (intention.getValue().getAction() == Action.MOVE) {
                            movedNinjas.add(myNinja);
                            BoardUpdater boardUpdater = new BoardUpdater();
                            boardUpdater.update(player2.getLocalBoard(),intention.getValue(), myNinja);
                            myNinja.setLastActionTaken(Action.MOVE);
                        }
                        gameManager.place(movedNinjas, player2.getLocalBoard());
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
                    display.retrieveBoard(player2);


                    
                    //   gameManager.updateBoards(playerInTurn, playerIntentions, enemyBoard);


                    requester.sendGet("/ready", Message.class);

/*                    while(!ReadyHandler.isReady()){

                    }
                    ReadyHandler.setReady(false);*/

                }
                }



            if (winValidator.winConditionsMet(player1, player2)) gameState = GameState.OVER;
        }


        System.out.println("GAME OVERRRRR");
        System.out.println("Ganador: " + winValidator.getWinner());
    }

    public void quit(){
        System.out.println("Cya!");
       // server.stop();
    }

private String[][] sendClientIntentions( Map<Integer, Intention> clientIntentions) {

    List<Intention> intentionList = new ArrayList<>();
    IntentionPack intentionPack = new IntentionPack(intentionList, player2.getMyNinjas(), false, player2.getEnemyBoard());
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
    Map<Integer, Intention>  playerIntentions = display.gamePlay(playerInTurn);
    List<Intention> intentionList = new ArrayList<>();
    for (Map.Entry<Integer,Intention> entry: playerIntentions.entrySet()
         ) {
        intentionList.add(entry.getValue());
    }
    OpError errors;


    //  requester.setIp("127.0.0.1:8001");

    try {
        IntentionPack intentionPack = new IntentionPack(intentionList, playerInTurn.getMyNinjas(),false, player2.getEnemyBoard());
       // String json = Converter.toJson(intentionPack);
        intentionPack = requester.sendPost(intentionPack, "/intentions");

        if (intentionPack == null) {
            System.out.println("Conexión rechazada!");
            return null;
        }

        while (!intentionPack.allGood ) {
            intentionList.clear();
            errors = intentionPack.getErrors();
            playerIntentions = display.ammendIntentions(playerIntentions, errors, playerInTurn);


            intentionList = new ArrayList<>();
            for (Map.Entry<Integer,Intention> entry: playerIntentions.entrySet()
            ) {
                intentionList.add(entry.getValue());
            }
            intentionPack.setIntentions(intentionList);
            errors = new OpError();
            intentionPack.setErrors(errors);


            intentionPack = requester.sendPost(intentionPack, "/intentions");

        }
        intentionList = intentionPack.getIntentions();


    } catch (Exception ex) {
        System.out.println(ex.getMessage());
    }
    return playerIntentions;
}

/*    public static void switchARoo(){
        if (playerInTurn == player1) {
            playerInTurn = player2;
        } else {playerInTurn = player1;}
    }*/

}
