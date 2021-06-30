package ar.com.konomo;

import ar.com.konomo.enums.GameState;
import ar.com.konomo.server.logic.Game;


public class Main {
    public static final int NINJAS = 3;
    public static final int BOARD_SIZE = 5;

    public static void main(String[] args) {
        Game game = new Game(Integer.parseInt(args[0]));

        while (Game.gameState != GameState.QUIT) {
            game.start();
            System.gc();
            game = new Game(Integer.parseInt(args[0]));
        }
        game.quit();
    }
}
