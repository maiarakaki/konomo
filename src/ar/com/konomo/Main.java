package ar.com.konomo;

import ar.com.konomo.enums.GameState;
import ar.com.konomo.server.logic.Game;


public class Main {

    public static void main(String[] args) {
        Game game = new Game(Integer.parseInt(args[0]));

        while (Game.gameState != GameState.QUIT) {
            game.start();
            System.gc();
            game = game.restart(Integer.parseInt(args[0]));
        }
        game.quit();
    }
}
