package ar.com.konomo.validators;

import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.operators.CoordinateBuilder;

public class MoveValidator {
    private final OpError opError;
    private static final int DEAD_COMMANDER = 8;
    private static final String DEAD_COMMANDER_MSG = "Tu comandante ha muerto, los ninjas no pueden moverse!";
    private static final int ALREADY_MOVED = 9;
    private static final int NON_VIABLE_MOVE = 10;
    private static final String ALREADY_MOVED_MSG = "Este ninja movió en el turno anterior ¬¬ ... ";
    private static final String NON_VIABLE_MOVE_MSG = "No podés moverte ahí ";
    private final CoordinatePositionValidator positionValidator;


    public boolean isValid(Coordinate coordinate, Shinobi ninja, boolean commanderAlive, Board board){
        CoordinateBuilder builder= new CoordinateBuilder();
        CoordinateIn coordinateIn;
        boolean isValid = false;
        if(commanderAlive) {
            if(ninja.getLastActionTaken() != Action.MOVE) {
                if (positionValidator.validate(board, coordinate) && isValidMove(ninja, coordinate)) {
                    isValid=true;
                } else {
                    coordinateIn = builder.build(coordinate);
                    CoordinateIn ninjaCoordinate = builder.build(new Coordinate(ninja.getColumnIndex()+10, ninja.getRowIndex()));
                    opError.add(NON_VIABLE_MOVE+": " + NON_VIABLE_MOVE_MSG + "(de "+  ninjaCoordinate.getColumn() + ":" + ninjaCoordinate.getRow() + " a " + coordinateIn.getColumn() + ":" + coordinateIn.getRow() + ")");
                }
            } else {
                coordinateIn = builder.build(new Coordinate(ninja.getColumnIndex()+10, ninja.getRowIndex()));
                opError.add(ALREADY_MOVED+": " + ALREADY_MOVED_MSG + " (" + coordinateIn.getColumn() + ":" + coordinateIn.getRow() + ")");
            }
        } else {
            opError.add(DEAD_COMMANDER+": "+DEAD_COMMANDER_MSG);
        }
        return isValid;
    }

    public MoveValidator(CoordinatePositionValidator positionValidator) {
        opError = new OpError();
        this.positionValidator = positionValidator;
    }

    public OpError getError() {
        return opError;
    }

    private boolean isValidMove (Shinobi ninja, Coordinate coordinate) {
        boolean isValidRow;
        boolean isValidCol;

        int coordvalue = coordinate.getColumn() -10;
        isValidCol = Math.abs(ninja.getColumnIndex() - coordvalue) < 2;

        isValidRow = Math.abs(ninja.getRowIndex() - coordinate.getRow()) < 2;

        return isValidRow && isValidCol;
    }
}