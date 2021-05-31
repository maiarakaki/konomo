package ar.com.konomo.managers;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Player;

import java.util.Scanner;


public class PlayerManager {
    private Player player = new Player();
    private Scanner scanner = new Scanner(System.in);
    private NinjaCreator ninjaCreator= new NinjaCreator();

    public Player create(int numberOfNinjas, int boardSize){
        Board board = new Board(boardSize, boardSize);
        player.setLocalBoard(board);
        player.setMyNinjas(ninjaCreator.create(numberOfNinjas));
        String[][] enemyboard = new String[boardSize][boardSize];
        player.setEnemyBoard(enemyboard);

        return player;
    }

    private String getPlayerName(){
        System.out.println("Nombre: ");
        return scanner.nextLine();
    }

    public Player setPlayerVariables(Player player){
        new Display().newScreen("Nuevo jugador");
        player.setName(getPlayerName());
        //player.placeTroops();
        new Display().placeNinjas("Despliega tus ninjas!");

        return player;
    }
}
