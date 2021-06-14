package ar.com.konomo.server.logic;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Server;
import ar.com.konomo.validators.WinValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private Server server;
    private GM gameManager;
    private Display display;
    private Player player1;
    private Player player2;
    private WinValidator winValidator;
    private List<Coordinate> coordinates;
    private Player playerInTurn;
    private Player adversary;

    public Game (){
        server = new Server();
        gameManager = new GM();
        display = new Display();
        coordinates = new ArrayList<>();
        winValidator = new WinValidator();

    }

    public void start(){
        gameManager.createGame();
        player1 = gameManager.getPlayer1();
        player2 = gameManager.getPlayer2();
        try {
            server.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " failed to start Server");
        }

        display.titleScreen();
        //String userOption = display.showOptions();

        playerInTurn = getPlayer(player1);
        adversary = getPlayer(player2);
    }


    private Player getPlayer(Player player){
        coordinates= (display.playerSettings(player));
        boolean allGood = gameManager.validate(coordinates, player);
        if (allGood) {
            display.retrieveBoard(player);
        } else {
            while (!allGood) {
                OpError errors = gameManager.getErrors();
                coordinates = display.ammendCoordinates(coordinates, errors);
                allGood = gameManager.validate(coordinates, player);
            }
        }
        return player;
    }

    public void play(){
        boolean gameOver = winValidator.winConditionsMet(player1, player2);
        boolean allGood;

        while (!gameOver) {
            //al inicio del turno me recibo los ataques de mi oponente...
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




}
