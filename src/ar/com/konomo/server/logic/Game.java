package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.display.Initializer;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.server.*;
import ar.com.konomo.server.handlers.HandshakeHandler;
import ar.com.konomo.server.handlers.ReadyHandler;
import ar.com.konomo.validators.WinValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private Server server;
    private Client client;
    private GM gameManager;
    private Display display;
    private Player player1;
    private Player player2;
    private WinValidator winValidator;
    private List<Coordinate> coordinates;
    private Player playerInTurn;
    private Player adversary;
    public static GameMode mode;
    private Initializer initializer;
    private Requester requester;

    public static volatile GameState gameState = GameState.ON;


    public Game (){
        gameManager = new GM();
        display = new Display();
        initializer = new Initializer(display, gameManager);
        server = new Server(gameManager, initializer);
        requester = new Requester();
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
        player1 = initializer.getPlayer(GameMode.HOST);
        player2 = initializer.getPlayer(GameMode.GUEST);
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



                gameOver = winValidator.winConditionsMet(player1, player2);

                System.out.println("==============FIN DE TURNO=============");

                while (!ReadyHandler.isReady()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
               playerInTurn = player2;


               Message message;

               attackLogger = requester.sendGet("/hitMe", attackLogger);
               message = requester.sendGet("/events", String.class);

               if (!attackLogger.getAttackLog().isEmpty()) {
                   gameManager.updateBoards(playerInTurn, attackLogger.getAttackLog());
                   EventMessageLog eventLog = Converter.fromJson(message.getMessage(), EventMessageLog.class);
                   eventLog.show();
                   //si me mataron todos los ninjas, hago un post para cambiar la variable de ON a OVER

                   if (winValidator.allNinjasKilled(playerInTurn)) {
                       requester.setIp("127.0.0.1:8001");
                       String json = Converter.toJson(playerInTurn.getMyNinjas());
                       requester.sendPost(json, "/gameState"); //<-- mando mis ninjas para que los actualice
                   }
               }
               display.retrieveBoard(playerInTurn);
               playerIntentions = getClientIntentions();


               requester.sendGet("/ready", Message.class);

               while (!ReadyHandler.isReady()) {
                   try {
                       Thread.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
        }


        System.out.println("GAME OVERRRRR");
        System.out.println("Ganador: " + winValidator.getWinner());
    }

    public void quit(){
        System.out.println("Cya!");
       // server.stop();
    }

private Map<Integer, Intention>  getClientIntentions(){
    Map<Integer, Intention>  playerIntentions = display.gamePlay(playerInTurn);
    OpError errors;


    //  requester.setIp("127.0.0.1:8001");

    try {
        IntentionPack intentionPack = new IntentionPack(playerIntentions, playerInTurn.getMyNinjas(), playerInTurn.getLocalBoard(),false);
       // String json = Converter.toJson(intentionPack);
        intentionPack = requester.sendPost(intentionPack, "/intentions");

        if (playerIntentions == null) {
            System.out.println("Conexión rechazada!");
            return null;
        }

        while (!intentionPack.allGood ) {
            errors = intentionPack.getErrors();
            playerIntentions = display.ammendIntentions(playerIntentions, errors, playerInTurn);
            intentionPack.setIntentions(playerIntentions);
            errors = new OpError();
            intentionPack.setErrors(errors);


            intentionPack = requester.sendPost(intentionPack, "/intentions");

        }
        playerIntentions = intentionPack.getIntentions();


    } catch (Exception ex) {
        System.out.println(ex.getMessage());
    }
    return playerIntentions;
}

}
