package ar.com.konomo;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.validators.WinValidator;
import com.google.gson.Gson;

import java.util.*;

public class Main {
    private String ip;
    private String port;
    public static final int NINJAS = 3;
    public static final int BOARD_SIZE = 5;

    public static void main(String[] args) {
        GM gameManager = new GM();
        gameManager.createGame();
        Player player1 = gameManager.getPlayer1();
        player1.setName("Jugador 1");
        Player player2 = gameManager.getPlayer2();
        AttackLogger attackLogger = new AttackLogger();
        WinValidator winValidator = new WinValidator();

        player2.setName("Jugador 2");
        Display display = new Display();
        //display.titleScreen();
        //String userOption = display.showOptions();
        List<Coordinate> coordinates = new ArrayList<>();
        //coordinates= (display.playerSettings(player1));
        //SETEO LOS NIJS DEL P1 EN A1 (10:0) ; B1 (11:0) y D1 (13:0)
        { // TODO boletear esto cuando termine las pruebas con las coordenadas
            Coordinate coord1 = new Coordinate(10, 0);
            coordinates.add(coord1);
            Coordinate coord2 = new Coordinate(11, 0);
            Coordinate coord3 = new Coordinate(13, 0);
            coordinates.add(coord2);
            coordinates.add(coord3);
        }
//VALIDO Y MUESTRO COMO QUEDARON LOS TABLEROS DEL P1
        boolean allGood = gameManager.validate(coordinates, player1);
        if (allGood) {
            display.retrieveBoard(player1);
        } else {
            while (!allGood) {
                OpError errors = gameManager.getErrors();
                coordinates = display.ammendCoordinates(coordinates, errors);
                allGood = gameManager.validate(coordinates, player1);
            }
        }
//SETEO LOS NIJAS DEL P2 EN B3 (11:2), D5 (13:4) Y E1 (14:0)
        { // TODO boletear esto cuando termine las pruebas con las coordenadas
            Coordinate coord1 = new Coordinate(11, 2);
            coordinates.set(0, coord1);
            Coordinate coord2 = new Coordinate(13, 4);
            coordinates.set(1, coord2);
            Coordinate coord3 = new Coordinate(14, 0);
            coordinates.set(2, coord3);
        }
        //VALIDO, ETC
        allGood = gameManager.validate(coordinates, player2);
        if (allGood) {
            display.retrieveBoard(player2);

        } else {
            while (!allGood) {
                OpError errors = gameManager.getErrors();
                coordinates = display.ammendCoordinates(coordinates, errors);
                allGood = gameManager.validate(coordinates, player2);
            }
        }
        //  display.retrieveBoard(player2);

        //ATACO CON EL P1 A TODOS LOS NINJAS DEL P2


        Map<Integer, Intention> playerIntentions = new HashMap<>();
        Player playerInTurn= player1;
        Player adversary = player2;

        boolean gameOver = winValidator.winConditionsMet(player1, player2);

        while (!gameOver) {
            //al inicio del turno me recibo los ataques de mi oponente...

            if (!gameManager.getAttackLogs().getAttackLog().isEmpty()) {
                gameManager.updateBoards(playerInTurn, gameManager.getAttackLogs().getAttackLog());
                gameManager.getEventLog().show();
                gameOver = winValidator.winConditionsMet(player1, player2);
            }
            display.retrieveBoard(playerInTurn);
            playerIntentions = display.gamePlay(playerInTurn);
            allGood = gameManager.validate(playerIntentions, playerInTurn);
/*
            if (allGood) {
                gameManager.updateBoards(playerInTurn, playerIntentions, adversary.getLocalBoard());
                gameManager.getEventLog().show();
                display.retrieveBoard(playerInTurn);

            } else {*/
                while (!allGood) {
                    OpError errors = gameManager.getErrors();
                    playerIntentions = display.ammendIntentions(playerIntentions, errors, playerInTurn);
                    allGood = gameManager.validate(playerIntentions, playerInTurn);
                }
                gameManager.updateBoards(playerInTurn, playerIntentions, adversary.getLocalBoard());
                gameManager.getEventLog().show();
                display.retrieveBoard(playerInTurn);
            //}


            if (playerInTurn == player1) {
                playerInTurn = player2;
                adversary = player1;
            } else {
                playerInTurn = player1;
                adversary = player2;}
            gameOver = winValidator.winConditionsMet(player1, player2);

            System.out.println("==============FIN DE TURNO=============");
        }

        System.out.println("GAME OVERRRRR");
        System.out.println(winValidator.getWinner());

        //al comienzo del turno, reviso el attackLog OJO, AL FINAL DE CADA TURNO DEBE PISARSE CON EL ATAQUE DEL JUGADOR Q ESTA JUGANDO

        //WinValidator winValidator = new WinValidator();

/*
        while (!winValidator.winConditionsMet(player1, player2)) {
        List<Intention> opponentAttacks = new ArrayList<>();



            //setear turno
            //get player actions
            //validar
            //update board
            //check win
        }
*/


}

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
