package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.display.Initializer;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Client;
import ar.com.konomo.server.Server;
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
    private GameMode mode;
    private Initializer initializer;

    public Game (){
        server = new Server(gameManager);
        gameManager = new GM();
        display = new Display();
        coordinates = new ArrayList<>();
        winValidator = new WinValidator();
        gameManager.createGame();
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        initializer = new Initializer(display, gameManager);
    }






    public void start(){
        gameManager.createGame();
        initializer.initiate();
        player1 = initializer.getPlayer(GameMode.HOST);
        player2 = initializer.getPlayer(GameMode.GUEST);





/*
         player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        playerInTurn = initializePlayer(player1);
        adversary = initializePlayer(player2);

 player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        playerInTurn = gameManager.getPlayer1();
        adversary = getPlayer(player2);*/

//        play();



    }







    public void play(){
        boolean gameOver = winValidator.winConditionsMet(player1, player2);
        boolean allGood;

        while (!gameOver) {
            Map<Integer, Intention> playerIntentions = new HashMap<>();


            if (!gameManager.getAttackLogs().getAttackLog().isEmpty()) {
                gameManager.updateBoards(playerInTurn, gameManager.getAttackLogs().getAttackLog());
                gameManager.getEventLog().show();
                gameOver = winValidator.winConditionsMet(player1, player2);
            }

            if (!gameOver) {
                display.retrieveBoard(playerInTurn);
                playerIntentions = display.gamePlay(playerInTurn);
                allGood = gameManager.validate(playerIntentions, playerInTurn);

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
        }


        System.out.println("GAME OVERRRRR");
        System.out.println("Ganador: " + winValidator.getWinner());
    }

    public void quit(){
        System.out.println("Cya!");
       // server.stop();
    }



}
