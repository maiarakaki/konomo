package ar.com.konomo.display;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.CoordinateIn;
import ar.com.konomo.operators.CoordinateBuilder;
import ar.com.konomo.validators.CoordinateFormalValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ar.com.konomo.Main.NINJAS;

public class NinjaPlacing {
    private Scanner scan = new Scanner(System.in);
    private Background background;
    private BoardMaker boardMaker;
    private Borders borders;
    private String[][] board;
    private CoordinateFormalValidator coordinateFormalValidator;



    public NinjaPlacing(){
        background = new Background(5,5);
        boardMaker = new BoardMaker();
        borders = new DoubleBorders();
        boardMaker.create(borders);
        coordinateFormalValidator = new CoordinateFormalValidator();
    }

    public List<Coordinate> getCoordenates(String title){
        background.setTitle(title);
        background.showBackground();
        board = boardMaker.getBoard();
        List<Coordinate> coordinates = new ArrayList<>();
        CoordinateIn userCoordinate = new CoordinateIn();
        CoordinateBuilder coordBuilder = new CoordinateBuilder();
        int i = 0;

        boardMaker.print();
        while (i <NINJAS) {
            if (i == NINJAS -1) {
                System.out.println("Ingresa las coordenadas para tu comandante: ");
            } else {
                System.out.printf("%s %d %s\n", "Ingresa las coordenadas para tu ninja" ,i+1 ,":");
            }
            String userInput = scan.nextLine();
            boolean isValid = coordinateFormalValidator.validate( userInput, userCoordinate);

            if(isValid) {
                Coordinate coordOut = coordBuilder.build(userCoordinate);
                System.out.println(userCoordinate.getColumn() + " " + userCoordinate.getRow());
                coordinates.add(coordOut);
            } else {
                i--;
            }

            i++;
        }

        return coordinates;
    }



}
