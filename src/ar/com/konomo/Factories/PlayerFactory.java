package ar.com.konomo.Factories;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Player;

public class PlayerFactory {
    private Player player;
    private NinjaCreator ninjaCreator;

    public PlayerFactory (NinjaCreator ninjaCreator){
        this.ninjaCreator = ninjaCreator;
    }

    public Player create(int numberOfNinjas, int boardSize){
        player = new Player();
        Board board = new Board(boardSize, boardSize);
        player.setLocalBoard(board);
        player.setMyNinjas(ninjaCreator.create(numberOfNinjas));
        String[][] enemyboard = new String[boardSize][boardSize];
        player.setEnemyBoard(enemyboard);

        return player;
    }

}
