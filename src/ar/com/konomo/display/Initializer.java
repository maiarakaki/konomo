package ar.com.konomo.display;

import ar.com.konomo.Factories.NinjaCreator;
import ar.com.konomo.Factories.PlayerFactory;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;
import ar.com.konomo.enums.GameMode;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.server.Message;
import ar.com.konomo.server.PlayerCoords;
import ar.com.konomo.server.Requester;
import ar.com.konomo.server.handlers.HandshakeHandler;

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

    private static volatile boolean isReady;

    public static void setReady(boolean ready) {
        isReady = ready;
    }

    public Initializer(Display display) {
        this.display = display;
        NinjaCreator ninjaCreator = new NinjaCreator();
        PlayerFactory playerFactory = new PlayerFactory(ninjaCreator);
        scan = new Scanner(System.in);
        player = playerFactory.create(NINJAS, BOARD_SIZE);

    }

    public void initiate() {

        requester = new Requester();

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
                    }
                }

                System.out.println("Conexión establecida! Juguemosss! =D");
                player1 =initializePlayer();
            }
            break;
            case "C": {
                gameMode = GameMode.GUEST;

                input = showOptions(GameMode.GUEST);
                System.out.println(input);

                if (Integer.parseInt(input) == 1) {


                    while (message == null || message.getCode() != 200) {
                        ip = display.serverCreation("Datos de conexión");
                        requester.setIp(ip);
                        message = requester.sendGet("/connect", Message.class);

                        if (message == null) {
                            System.out.println("Falló la conexión!");
                        }
                        // ver que hago si no me puedo conectar (???)
                    }
                }
                System.out.println("Esperando conexión");

                while (message == null || message.getCode() != 200) {

                }
                System.out.println(message.getMessage());
                System.out.println("Conexión establecida! Juguemosss! =D");
                player2 = initializeClient();
                System.out.println("Le pido datos al cliente....");
            }
            break;
            case "X":
                return;
        }


    }


    private Message attemptConnection() {
        Message message = null;
        while (message == null || message.getCode() != 200) {
            ip = display.serverCreation("Datos de conexión");
            requester.setIp(ip);
            message = requester.sendGet("/connect", Message.class);

            if (message == null) {
                System.out.println("Falló la conexión!");
            }
            // ver que hago si no me puedo conectar (???)
        }
        return message;

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
                System.out.println("2. Esperar a que se conecte alguien");

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
        GM gameManager = new GM();
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
        List<Coordinate> coordinates= (display.playerSettings(player));
        playerCoords.setCoords(coordinates);
        Requester requester = new Requester();
        requester.setIp(HandshakeHandler.getIp()+":"+8000);
        Message message = requester.sendPost(playerCoords, "/validate");

        while (message.getCode() != 200) {
            playerCoords = Converter.fromJson(message.getMessage(),PlayerCoords.class);
            errors = playerCoords.getErrors();
            coordinates = display.ammendCoordinates(playerCoords.getCoords(), errors);

            message = requester.sendPost(coordinates, "/validate");
        }

        return playerCoords.getPlayer();
    }
}
