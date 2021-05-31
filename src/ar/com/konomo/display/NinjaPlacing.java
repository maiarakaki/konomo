package ar.com.konomo.display;

import ar.com.konomo.validators.LegitCoordinateValidator;

import java.util.Scanner;

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

    public String getCoordenates(String title){
        background.setTitle(title);
        background.showBackground();
        board = boardMaker.getBoard();
        int i = 0;
        System.out.println("Ingresa las coordenadas para tu ninja " + i+1 +":");
        String userInput;
        while (i <3) {
            printBoard();
            userInput = scan.nextLine();

            coordinateValidator.validate(userInput);

        }


        return scan.nextLine();
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
