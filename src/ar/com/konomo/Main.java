package ar.com.konomo;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.Player;
import ar.com.konomo.managers.GM;
import java.util.List;

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
        gameManager.validate(display.playerCreation(players.get(0)));

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
