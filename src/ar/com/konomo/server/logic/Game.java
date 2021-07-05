package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.display.Initializer;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.server.*;
import ar.com.konomo.server.handlers.HandshakeHandler;
import ar.com.konomo.server.handlers.ReadyHandler;
import ar.com.konomo.validators.WinValidator;

import java.util.*;

public class Game {

    private Server server;
    private GM gameManager;
    private Display display;
    private static Player player1;
    private static Player player2;
    private WinValidator winValidator;
    private List<Coordinate> coordinates;
    public static GameMode mode;
    private Initializer initializer;
    private Requester requester;
    private int port;

    public static volatile GameState gameState = GameState.ON;



    public Game (){
        gameManager = new GM();
        display = new Display();
        requester = new Requester();
        initializer = new Initializer(display, gameManager, requester);
        server = new Server(gameManager);
        coordinates = new ArrayList<>();
        winValidator = new WinValidator();
        gameManager.createGame();
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
    }



    public void start(){
        initializer.initiate();

        mode = initializer.getGameMode();

        if (mode == GameMode.GUEST) {
            Client client = new Client();
            client.setPlayer(initializer.getPlayer(GameMode.GUEST));
            client.setDisplay(display);
            client.setRequester(requester);
            client.setWinValidator(winValidator);
            client.play();

        } else {
            if (mode == GameMode.HOST) {
                /**
                 * Inicializado el host, aviso al cliente que estoy listo para jugar
                 */
                requester.sendGet("/ready", Message.class);
                play();

            }
        }
    }





    public void play(){
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();

        boolean gameOver;
        boolean allGood;
        AttackLogger attackLogger = new AttackLogger();

        while (gameState == GameState.ON) {
            Map<Integer, Intention> playerIntentions = new HashMap<>();


            if (mode == GameMode.HOST) {

                while(!ReadyHandler.isReady()){

                }
                ReadyHandler.setReady(false);


                /**
                 * Al inicio de cada turno, evaúo si recibí ataques para actualizar el tablero antes de pedir
                 * las intenciones al jugador.
                 */
                attackLogger = gameManager.getAttackLogs();


                if (!attackLogger.getAttackLog().isEmpty()) {
                    gameManager.updateBoards(player1, attackLogger.getAttackLog());
                    attackLogger.getAttackLog().clear();
                    gameManager.getEventLog().show();
                    gameOver = winValidator.winConditionsMet(player1, player2);

                    if (gameOver) {
                        gameState = GameState.OVER;
                    }
                }

                if (gameState == GameState.ON) {
                    display.retrieveBoard(player1);
                    playerIntentions = display.gamePlay(player1);
                    allGood = gameManager.validate(playerIntentions, player1);

                    while (!allGood) {
                        OpError errors = gameManager.getErrors();
                        playerIntentions = display.ammendIntentions(playerIntentions, errors, player1);
                        gameManager.getErrors().clear();
                        allGood = gameManager.validate(playerIntentions, player1);
                    }
                    gameManager.updateBoards(player1, playerIntentions, player2.getLocalBoard());
                    gameManager.getEventLog().show();
                    gameManager.updateBoards(player2, attackLogger.getAttackLog());
                    display.retrieveBoard(player1);


                    requester.sendGet("/ready", Message.class);

                }
            }

            if (winValidator.winConditionsMet(player1, player2)) {
                gameState = GameState.OVER;
                try {
                    requester.sendGet("/gameState", Message.class);
                } catch (IllegalStateException  ex ) {
                    ex.printStackTrace();
                }
            }

        }


        display.newScreen ("GAME OVER\n Ganador: " + winValidator.getWinner());
        restart(port);

    }

    public void quit(){

        display.newScreen("Cya! Gracias por jugar!\n\nﾟ･:,｡★＼(^-^ )♪ありがとう♪( ^-^)/★,｡･:･ﾟ");
        restart();
    }

    public Game restart(){
        ReadyHandler.setReady(false);
        HandshakeHandler.setConnected(false);
        server.stop();
        gameState= GameState.ON;
        return new Game();
    }

}
