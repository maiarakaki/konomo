package ar.com.konomo.validators;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;

import java.util.List;

public class CoordinatePositionValidator {
    private OpError errors;
    private static final int NON_PLACEABLE = 6;
    private static final String NON_PLACEABLE_MSG = "El casillero está ocupado o es intransitable!";

    public CoordinatePositionValidator(){
        errors = new OpError();
    }

    public boolean validate(Board board, Coordinate coordinate) {
        return board.getBoard()[coordinate.getRow()][coordinate.getColumn() -10] == null;
    }

    public boolean validate(Board board, List<Coordinate> coordinates) {
        boolean success = true;
        try{
            for (Coordinate coordinate: coordinates
            ) {
                if (!validate(board, coordinate)) {
                    coordinate.setValid(false);
                    success = false;
                    errors.add(NON_PLACEABLE +": " + NON_PLACEABLE_MSG);
                }
            }
        } catch (Exception ex) {
            success = false;
            System.out.println(ex.getMessage());
        }

        return success;
    }

    public OpError getErrors(){
        return errors;
    }

}
