package ar.com.konomo.managers;

import ar.com.konomo.display.Display;
import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Chuunin;
import ar.com.konomo.entity.Player;
import java.util.Scanner;


public class PlayerManager {
    private Player player = new Player();
    private Scanner scanner = new Scanner(System.in);

    public Player create(int numberOfNinjas, int boardSize){
        new Display().newScreen("New Player");
        player.setName(getPlayerName());
        Board board = new Board(boardSize, boardSize);
        player.setLocalBoard(board);




        return player;
    }

    private String getPlayerName(){
        System.out.println("nombre?");
        return scanner.nextLine();
    }
}
