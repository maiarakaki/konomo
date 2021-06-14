package ar.com.konomo.operators;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.enums.NinjaType;

public class JackpotChecker {
    private boolean somethingThere = false;

    public boolean check(Coordinate checkAt, String[][] boardRepresentation) {
        try {
            somethingThere = boardRepresentation[checkAt.getRow()][checkAt.getColumn()-10] != null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return somethingThere;
    }

    public boolean check(Coordinate checkAt, Board actualBoard) {
        try {
            somethingThere =  actualBoard.getBoard()[checkAt.getRow()][checkAt.getColumn()-10] != null;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return somethingThere;
    }

    public NinjaType whatIsThere(Coordinate checkAt, Board actualBoard) {
        return actualBoard.getBoard()[checkAt.getRow()][checkAt.getColumn()-10].getType();
    }
}
