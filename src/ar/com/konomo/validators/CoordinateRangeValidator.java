package ar.com.konomo.validators;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;

import java.util.List;

import static ar.com.konomo.Main.BOARD_SIZE;

public class CoordinateRangeValidator {
    private OpError errors;
    private static final int X_OUT_OF_RANGE= 3;
    private static final int Y_OUT_OF_RANGE = 4;
    private static final String X_OUT_OF_RANGE_MSG = "La letra se encuentra fuera de rango en la coordenada nùmero ";
    private static final String Y_OUT_OF_RANGE_MSG = "El número se encuentra fuera de rango  en la coordenada nùmero ";

    public CoordinateRangeValidator(){
        errors = new OpError();
    }

    public boolean validate(Coordinate coordinate){
        boolean isValidX = xIsValid(coordinate.getColumn());
        if (!isValidX) {
            errors.add(X_OUT_OF_RANGE, X_OUT_OF_RANGE_MSG);
        }
        boolean isValidY =yIsValid(coordinate.getRow());
        if(!isValidY){
            errors.add(Y_OUT_OF_RANGE, Y_OUT_OF_RANGE_MSG);
        }

        return isValidX && isValidY;
    }

    public boolean validate(List<Coordinate> coordinates){
        boolean success= true;
        int i = 1;
         for (Coordinate coordinate:coordinates
             ) {

             boolean xIsValid = xIsValid(coordinate.getColumn());
             boolean yIsValid = yIsValid(coordinate.getRow());

             coordinate.setValid(xIsValid && yIsValid);

             if (!xIsValid) {
                 errors.add(X_OUT_OF_RANGE, X_OUT_OF_RANGE_MSG + i);
                 success = false;
             }
             if (!yIsValid) {
                 errors.add(Y_OUT_OF_RANGE, Y_OUT_OF_RANGE_MSG + i);
                 success = false;
             }
             i++;
        }
         return success;
    }

    private boolean xIsValid(int coordX) {
        int a = Character.getNumericValue('A');
        return coordX >= a && coordX < a + BOARD_SIZE;
    }

    private boolean yIsValid(int coordY) {
        return coordY >=0 && coordY <BOARD_SIZE;
    }

    public OpError getErrors(){
        return errors;
    }
}
