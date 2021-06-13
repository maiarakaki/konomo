package ar.com.konomo;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.managers.GM;
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
        Player player1= gameManager.getPlayer1();
        player1.setName("Jugador 1");
        Player player2= gameManager.getPlayer2();

        player2.setName("Jugador 2");
        Display display = new Display();
        //display.titleScreen();
        //String userOption = display.showOptions();
        List<Coordinate> coordinates = new ArrayList<>();
        //coordinates= (display.playerSettings(player1));

        { // TODO boletear esto cuando termine las pruebas con las coordenadas
            Coordinate coord1 = new Coordinate(10, 0);
            coordinates.add(coord1);
            Coordinate coord2 = new Coordinate(11, 0);
            Coordinate coord3 = new Coordinate(13, 0);
            coordinates.add(coord2);
            coordinates.add(coord3);
        }

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

/*        { // TODO boletear esto cuando termine las pruebas con las coordenadas
            Coordinate coord1 = new Coordinate(11, 2);
            coordinates.set(0, coord1);
            Coordinate coord2 = new Coordinate(13, 4);
            coordinates.set(1, coord2);
            Coordinate coord3 = new Coordinate(14, 0);
            coordinates.set(2, coord3);
        }

     allGood = gameManager.validate(coordinates, player2);
        if (allGood) {
            display.retrieveBoard(player2);
        } else {
            while (!allGood) {
                OpError errors = gameManager.getErrors();
                coordinates = display.ammendCoordinates(coordinates, errors);
                allGood = gameManager.validate(coordinates, player2);
            }
        }*/
        display.retrieveBoard(player1);

        Map<Integer, Intention> dummyIntentions = new HashMap<>();
        Intention int1= new Intention();
        int1.setAction(Action.MOVE);
        int1.setCoordinate(new Coordinate(10, 1));

        Intention int2= new Intention();
        int2.setAction(Action.MOVE);
        int2.setCoordinate(new Coordinate(14, 0));

        Intention int3= new Intention();
        int3.setAction(Action.MOVE);
        int3.setCoordinate(new Coordinate(11, 0));

        dummyIntentions.put(0, int1);
        dummyIntentions.put(1, int2);
        dummyIntentions.put(2, int3);

        //Map <Integer, Intention> dummyIntentions = display.gamePlay(player1);
        gameManager.validate(dummyIntentions, player1);
        OpError errors = gameManager.getErrors();
        for (String error: errors.getErrors()
             ) {
            System.out.println(error);
        }

        //WinValidator winValidator = new WinValidator();

/*
        while (!winValidator.winConditionsMet(player1, player2)) {



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
