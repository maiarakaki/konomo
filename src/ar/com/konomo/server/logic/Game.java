package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.display.Initializer;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.server.*;
import ar.com.konomo.server.handlers.ReadyHandler;
import ar.com.konomo.validators.WinValidator;

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

        mode = initializer.getGameMode();

        if (mode == GameMode.GUEST) {
            Client client = new Client();
            client.setPlayer(initializer.getPlayer(GameMode.GUEST));
            client.setDisplay(display);
            client.setRequester(requester);
            client.setWinValidator(winValidator);
            client.play();

        } else {


        /**
         * la siguiente linea está así en el server, no decomentar
         * hasta no haber separado client de server (creo)
         */
        //requester.sendGet("/ready", Message.class);

        play();
        }
    }





    public void play(){
      //  boolean gameOver = winValidator.winConditionsMet(player1, player2);
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        playerInTurn = player1;

        boolean gameOver;
        boolean allGood;
        AttackLogger attackLogger = new AttackLogger();

        while (gameState == GameState.ON) {
            Map<Integer, Intention> playerIntentions = new HashMap<>();


            if (mode == GameMode.HOST) {
                //playerInTurn = player1;

                while(!ReadyHandler.isReady()){

                }
                ReadyHandler.setReady(false);


                /**
                 * en teoría el attacklogguer "VIVE" en el manager, en el server... asi que en teoría cuando valido las intenciones del cliente,
                 * debería ser capaz de analizarlo localmente
                 */
                attackLogger = gameManager.getAttackLogs();


                if (!attackLogger.getAttackLog().isEmpty()) {
                    gameManager.updateBoards(player1, attackLogger.getAttackLog());
                    gameManager.getEventLog().show();
                    gameOver = winValidator.winConditionsMet(player1, player2);

                    if (gameOver) {
                        gameState = GameState.OVER;
                    }
                }

                if (gameState == GameState.ON) { //<- soy el servidor, sé que el juego no terminó
                    display.retrieveBoard(playerInTurn); //<-le muestro el board al jugador en juego y le pido sus intenciones
                    playerIntentions = display.gamePlay(player1);
                    allGood = gameManager.validate(playerIntentions, player1);

                    while (!allGood) {
                        OpError errors = gameManager.getErrors();
                        playerIntentions = display.ammendIntentions(playerIntentions, errors, player1);
                        allGood = gameManager.validate(playerIntentions, player1);
                    }
                    gameManager.updateBoards(player1, playerIntentions, player2.getLocalBoard());
                    gameManager.getEventLog().show();
                    display.retrieveBoard(player1);



                    System.out.println("==============FIN DE TURNO=============");
                    requester.sendGet("/ready", Message.class);

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



/*    public static void switchARoo(){
        if (playerInTurn == player1) {
            playerInTurn = player2;
        } else {playerInTurn = player1;}
    }*/

}
