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

    private static volatile GameState gameState = GameState.ON;


    public Game (){
        gameManager = new GM();
        display = new Display();
        initializer = new Initializer(display, gameManager);
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
/*            } else {
                //espero a que el cliente me avise que está ready para ir a buscar sus intenciones
                while (!ReadyHandler.isReady()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //attackLogger =
            }*/


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


                if (playerInTurn == player1) {
                    playerInTurn = player2;
                    adversary = player1;
                } else {
                    playerInTurn = player1;
                    adversary = player2;}

                gameOver = winValidator.winConditionsMet(player1, player2);

                System.out.println("==============FIN DE TURNO=============");
            }
        } else {
               playerInTurn = player2;

               Requester requester = new Requester();
               Message message;
               //requester.setIp(HandshakeHandler.getIp()+":"+"8001");
               requester.setIp("127.0.0.1:8001");
           //    Message message =  requester.sendGet("/hitMe", Message.class);
             //  attackLogger = (AttackLogger) message.getBody();
                attackLogger = requester.sendGet("/hitMe", attackLogger);

               if (!attackLogger.getAttackLog().isEmpty()) {
                   gameManager.updateBoards(playerInTurn, attackLogger.getAttackLog());
                   message = requester.sendGet("/events", String.class);
                   EventMessageLog eventLog = Converter.fromJson(message.getMessage(), EventMessageLog.class);
                   eventLog.show();

                   //gameManager.getEventLog().show(); //<-- este tambien necesita un get
                  // gameOver = winValidator.winConditionsMet(player1, player2);
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



}
