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
    private CoordinateIn userCoordinate;
    private CoordinateBuilder coordBuilder;
    private ScreenBoard screenBoard;


    public NinjaPlacing() {
        background = new Background(5, 5);
        boardMaker = new BoardMaker();
        borders = new DoubleBorders();
        coordinateFormalValidator = new CoordinateFormalValidator();
        userCoordinate = new CoordinateIn();
        coordBuilder = new CoordinateBuilder();
    }

    public List<Coordinate> getCoordenates(String title) {
        background.setTitle(title);
        background.showBackground();
        screenBoard = boardMaker.create(borders);

        List<Coordinate> coordinates = new ArrayList<>();

        int i = 0;

        screenBoard.print();
        while (i < NINJAS) {
            if (i == NINJAS - 1) {
                System.out.println("Ingresa las coordenadas para tu comandante: ");
            } else {
                System.out.printf("%s %d %s\n", "Ingresa las coordenadas para tu ninja", i + 1, ":");
            }
            String userInput = scan.nextLine();
            boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);

            if (isValid) {
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

    public List<Coordinate> getCoordenates(String title, List<Coordinate> coordinates) {
        background.setTitle(title);
        background.showBackground();



        screenBoard.print();

        for (Coordinate coordinate : coordinates
        ) {
            if (!coordinate.isValid()) {
                if (coordinates.indexOf(coordinate) == NINJAS - 1) {
                    System.out.println("Ingresa las coordenadas para tu comandante: ");
                } else {
                    System.out.printf("%s %d %s\n", "Ingresa las coordenadas para tu ninja", coordinates.indexOf(coordinate) + 1, ":");
                }
                String userInput = scan.nextLine();
                boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);
                if (isValid) {
                    Coordinate coordOut = coordBuilder.build(userCoordinate);
                    System.out.println(userCoordinate.getColumn() + " " + userCoordinate.getRow());
                    coordinates.set(coordinates.indexOf(coordinate), coordOut);
                }
            }
        }

        return coordinates;
    }
}
