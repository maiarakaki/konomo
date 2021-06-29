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



    public Game (int port){
        gameManager = new GM();
        display = new Display();
        requester = new Requester();
        initializer = new Initializer(display, gameManager, requester);
        server = new Server(gameManager, port);
        coordinates = new ArrayList<>();
        winValidator = new WinValidator();
        gameManager.createGame();
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
    }



    public void start(){
        gameManager.createGame();
        initializer.initiate();
        //requester.setIp(HandshakeHandler.getIp()+":"+"8000");
        //requester.setIp("127.0.0.1:8001");

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
                    display.retrieveBoard(player1);



                    System.out.println("==============FIN DE TURNO=============");
                    requester.sendGet("/ready", Message.class);

                }
            }

            if (winValidator.winConditionsMet(player1, player2)) {
                gameState = GameState.OVER;
                requester.sendGet("/gameState", Message.class); //FIJARSE QUÉ PASA ACÁ
            }

        }


        System.out.println("GAME OVERRRRR");
        System.out.println("Ganador: " + winValidator.getWinner());


    }

    public void quit(){


        display.newScreen("Cya! Gracias por jugar!\n\nﾟ･:,｡★＼(^-^ )♪ありがとう♪( ^-^)/★,｡･:･ﾟ");
        server.stop();
    }

}
