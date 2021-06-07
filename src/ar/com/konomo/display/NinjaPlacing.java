package ar.com.konomo.display;

import ar.com.konomo.validators.LegitCoordinateValidator;

import java.util.Scanner;

import static ar.com.konomo.Main.NINJAS;

public class NinjaPlacing {
    Scanner scan = new Scanner(System.in);
    Background background;
    BoardMaker boardMaker;
    private Borders borders;
    String[][] board;
    LegitCoordinateValidator coordinateValidator= new LegitCoordinateValidator();

    public NinjaPlacing(){
        background = new Background(5,5);
        boardMaker = new BoardMaker();
        borders = new DoubleBorders();
        boardMaker.create(borders);
    }

    public String[] getCoordenates(String title){
        background.setTitle(title);
        background.showBackground();
        board = boardMaker.getBoard();
        int i = 0;
        String entries[] = new String[NINJAS];

        printBoard();
        while (i <NINJAS) {
            if (i == NINJAS -1) {
                System.out.println("Ingresa las coordenadas para tu comandante: ");
            } else {
                System.out.printf("%s %d %s\n", "Ingresa las coordenadas para tu ninja" ,i+1 ,":");
            }
            entries[i] = scan.nextLine();
            i++;
        }

        return entries;
    }

    private void printBoard() {
        for (int i = 0 ; i < board.length; i++) {
            for (int j = 0 ; j < board[i].length; j++) {
                System.out.print(String.format(" %s ", board[i][j]) );
            }
            System.out.print("\n");
        }
    }

}
