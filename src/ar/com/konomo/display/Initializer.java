package ar.com.konomo.display;

import ar.com.konomo.Factories.NinjaCreator;
import ar.com.konomo.Factories.PlayerFactory;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.*;
import ar.com.konomo.server.handlers.HandshakeHandler;
import ar.com.konomo.server.logic.Game;

import java.util.List;
import java.util.Scanner;

import static ar.com.konomo.Main.BOARD_SIZE;
import static ar.com.konomo.Main.NINJAS;

public class Initializer {
    private GameMode gameMode;
    private Display display;
    private Scanner scan;
    private String ip;
    private Requester requester;
    private Player player1;
    private Player player;
    private Player player2;
    private GM gameManager;

    private static volatile boolean isReady;

    public static void setReady(boolean ready) {
        isReady = ready;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Initializer(Display display, GM manager, Requester requester) {
        this.display = display;
        this.gameManager = manager;
        this.requester = requester;
        NinjaCreator ninjaCreator = new NinjaCreator();
        PlayerFactory playerFactory = new PlayerFactory(ninjaCreator);
        scan = new Scanner(System.in);
        player = playerFactory.create(NINJAS, BOARD_SIZE);

    }

    public void initiate() {

       // requester = new Requester();

        display.titleScreen();
        String userOption = display.showOptions().toUpperCase();
        String input;
        Message message = null;



        switch (userOption) {
            case "N": {
                gameMode = GameMode.HOST;

                input = showOptions(GameMode.HOST);
                System.out.println(input);
                if (Integer.parseInt(input) == 1) {
                    //  attemptConnection();
                    message = null;
                    while (message == null || message.getCode() != 200) {
                        ip = display.serverCreation("Datos de conexión");
                        requester.setIp(ip);
                        message = requester.sendGet("/connect", Message.class);

                        if (message == null) {
                            System.out.println("Falló la conexión!");
                        }
                        // ver que hago si no me puedo conectar (???)
                        System.out.println("Esperando conexión");

                    }

                } else {

                    System.out.println("Esperando conexión");

                    while (!HandshakeHandler.isConnected()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ip = HandshakeHandler.getIp();
                        requester.setIp(ip+":"+Server.PORT);

                    }
                }

                System.out.println("Conexión establecida! Juguemosss! =D");
                player1 =initializePlayer();
                gameManager.setPlayer1(player1);
            }
            break;
            case "C": {
                gameMode = GameMode.GUEST;

                input = showOptions(GameMode.GUEST);
                System.out.println(input);

                if (Integer.parseInt(input) == 1) {
                    //  attemptConnection();
                    message = null;
                    while (message == null || message.getCode() != 200) {
                        ip = display.serverCreation("Datos de conexión");
                        requester.setIp(ip);
                        message = requester.sendGet("/connect", Message.class);

                        if (message == null) {
                            System.out.println("Falló la conexión!");
                        }
                        // ver que hago si no me puedo conectar (???)
                        System.out.println("Esperando conexión");

                    }

                } else {

                    System.out.println("Esperando conexión");

                    while (!HandshakeHandler.isConnected()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    requester.setIp(HandshakeHandler.getIp()+":"+Server.PORT);
                }

                System.out.println("Conexión establecida! Juguemosss! =D");


                player2 = initializeClient();


                //requester.setIp("127.0.0.1:8001");
                String json = Converter.toJson(player.getName());
                requester.sendPost(json, "/player");


                try{
                    display.retrieveBoard(player2);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            break;
            case "X":
                return;
        }


    }


    private String showOptions(GameMode gameMode) {
        String input = "";
        display.newScreen("¿Qué deseas hacer?");
        switch (gameMode) {
            case HOST:

                System.out.println("1. Invitar a alguien");
                System.out.println("2. Esperar a que se conecte alguien");

                input = scan.nextLine();

                while ((Integer.parseInt(input) > 2 || Integer.parseInt(input) < 1)) {
                    display.newScreen("Debes seleccionar una opción válida!");

                    System.out.println("1. Invitar a alguien");
                    System.out.println("2. Esperar a que se conecte alguien");

                    input = scan.nextLine();
                }
                break;
            case GUEST:
                System.out.println("1. Conectarme a una partida");
                System.out.println("2. Esperar invitación");

                input = scan.nextLine();

                while ((Integer.parseInt(input) > 2 || Integer.parseInt(input) < 1)) {
                    display.newScreen("Debes seleccionar una opción válida!");

                    System.out.println("1. Invitar a alguien");
                    System.out.println("2. Esperar invitación");

                    input = scan.nextLine();
                    break;
                }

        }
        return input;
    }

    public String getIp() {
        return ip;
    }

    public Player initializePlayer() {

        List<Coordinate> coordinates = (display.playerSettings(player));
        boolean allGood = gameManager.validate(coordinates, player);
        if (allGood) {
            display.retrieveBoard(player);
        } else {
            while (!allGood) {
                OpError errors = gameManager.getErrors();
                coordinates = display.ammendCoordinates(coordinates, errors);
                //
                allGood = gameManager.validate(coordinates, player);
            }
        }
        return player;
    }

    public Player getPlayer(GameMode gameMode) {
        switch (gameMode) {
            case HOST -> {
                return player1;
            }
            case GUEST -> {
                return player2;
            }
        }
        return null;
    }

    private Player initializeClient(){

        PlayerCoords playerCoords= new PlayerCoords();
        OpError errors;
        playerCoords.setAllGood(false);
        List<Coordinate> coordinates= (display.playerSettings(player));
        playerCoords.setNinjaList(player.getMyNinjas());
        playerCoords.setCoords(coordinates);
     //   Requester requester = new Requester();
/*        if (HandshakeHandler.getIp() != null) {
            requester.setIp(HandshakeHandler.getIp()+":"+"8000");
        }*/
        //requester.setIp("127.0.0.1:8001");

        try {
            playerCoords = requester.sendPost(playerCoords, "/validate");

            if (playerCoords == null) {
                System.out.println("Conexión rechazada!");
                return null;
            }

            while (!playerCoords.isAllGood() ) {
                errors = playerCoords.getErrors();
                coordinates = display.ammendCoordinates(playerCoords.getCoords(), errors);
                playerCoords.setCoords(coordinates);
                errors = new OpError();
                playerCoords.setErrors(errors);


                playerCoords = requester.sendPost(playerCoords, "/validate");

            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

      try {
          gameManager.place(playerCoords.getNinjaList(), player.getLocalBoard());
          player.setMyNinjas(playerCoords.getNinjaList());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return player;
    }

    public Requester getRequester() {
        return requester;
    }
}