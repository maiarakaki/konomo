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
        List<Player> players = gameManager.getPlayers();
        Display display = new Display();
        //display.titleScreen();
        //String userOption = display.showOptions();
        List<Coordinate> coordinates = new ArrayList<>();
        //coordinates= (display.playerSettings(players.get(0)));
        Coordinate coord1 = new Coordinate(0,1);
        coordinates.add(coord1);

        Coordinate coord2 = new Coordinate(0,2);
        Coordinate coord3 = new Coordinate(0,3);

        coordinates.add(coord2);
        coordinates.add(coord3);

        boolean allGood = gameManager.validate(coordinates, players.get(0));

        System.out.println(gameManager.getErrors().getErrors().entrySet());
        gameManager.getErrors().getErrors().forEach((key, value) -> System.out.println(key + " : " + value) );


        }
/*
        if(allGood) {

                List <Shinobi> ninjas = players.get(0).getMyNinjas();
                Board board= players.get(0).getLocalBoard();
                gameManager.place(ninjas, board);

        } else {



            //TODO ver como avisarle a display que no pude agregar a los ninjas
        }
        }
*/



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
