package ar.com.konomo;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.*;
import ar.com.konomo.managers.GM;

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
        Player player2= gameManager.getPlayer2();
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
