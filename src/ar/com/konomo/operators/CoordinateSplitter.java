package ar.com.konomo.operators;

import ar.com.konomo.entity.Coordinate;


public class CoordinateSplitter {
    private Coordinate coordinate;

    public Coordinate split(String userInput){
        Coordinate coordinate = new Coordinate();
        getAlpha(coordinate, userInput);
        getNumber(coordinate, userInput);

        return coordinate;
    }

    private Coordinate getAlpha(Coordinate coordinate, String userInput) {
        String alpha = "";
        try {
            alpha = userInput.substring(0,1);

        } catch (Exception ex) {
            System.out.println("Ha ingresado valores inválidos " + ex.getMessage());
        }
        coordinate.setX(alpha);
        return coordinate;
    }

    private Coordinate getNumber(Coordinate coordinate, String userInput) {
        int number = 0;
        try {
           number += Integer.parseInt(userInput.substring(1,userInput.length()));
        } catch (Exception ex) {
            System.out.println("Ha ingresado valores inválidos " + ex.getMessage());
        }
        coordinate.setY(number);

        return coordinate;
    }
}
