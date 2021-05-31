package ar.com.konomo.managers;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.Player;


public class Konomo {
    private final Display display = new Display();
    private final PlayerManager playerManager = new PlayerManager();
    private String ip;
    private String port;
    private Player player1;
    private Player player2;
    static final int NINJAS = 3;
    static final int BOARD_SIZE = 5;


    public void run() {
        display.titleScreen();
        String userOption = display.showOptions();
        while (userOption != null) {
            switch (userOption) {
                case "N" -> {
                    //crea el servidor
                    create();
                    //crea el player 1
                    player1 = playerManager.create(NINJAS, BOARD_SIZE);
                    play();

                    userOption = null;
                }
                case "C" -> {
                    join();
                    player2 = playerManager.create(NINJAS, BOARD_SIZE);
                    userOption = null;
                    play();
                }
                case "X" -> {
                    quit();
                    userOption = null;
                }
                default -> {
                    System.out.print("defaulteo");
                    userOption = display.showOptions();
                }
            }
        }

    }

    public void create(){
        String userInput;
        userInput = display.serverCreation("Nueva Partida");
        setIp(userInput.split(":")[0]);
        setPort(userInput.split(":")[1]);


        System.out.println("creando partida");
        System.out.println(getIp() + " " + getPort());
    }

    public void join(){
        String userInput;
        userInput = display.serverCreation("Unirse a una partida");
        setIp(userInput.split(":")[0]);
        setPort(userInput.split(":")[1]);
    }

    public void quit(){
        System.out.println("Saliendo");
    }

    public void play(){
        boolean gameOver = false;
        while (!gameOver) {


            gameOver = true;
        }
        System.out.println("El ganador es: " + player1.getName());
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
