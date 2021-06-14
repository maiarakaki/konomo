package ar.com.konomo.validators;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;

import java.util.List;

public class CoordinateViabilityValidator {
    boolean oneAndTwo;
    boolean oneAndThree;
    boolean twoAndThree;
    private OpError errors;
    private static final int NON_VIABLE_COORD = 5;
    private static final String NON_VIABLE_COORD_MSG = "Una o más intenciones están repetidas!";

    public CoordinateViabilityValidator (){
        errors = new OpError();
    }

    public boolean isViable(List<Coordinate> coordinateList){
        final int COORD1 = 0;
        final int COORD2 = 1;
        final int COORD3 = 2;

        oneAndTwo = compare(coordinateList.get(COORD1),coordinateList.get(COORD2));
        oneAndThree = compare(coordinateList.get(COORD1),coordinateList.get(COORD3));
        twoAndThree = compare(coordinateList.get(COORD2), coordinateList.get(COORD3));

        if (oneAndTwo || oneAndThree || twoAndThree) {
            errors.add(NON_VIABLE_COORD +": " + NON_VIABLE_COORD_MSG);
        }

        return !(oneAndTwo || oneAndThree || twoAndThree);
    }

    private boolean compare(Coordinate coordA, Coordinate coordB) {
        boolean coordinatesMatch;
        coordinatesMatch = coordA.getColumn() == coordB.getColumn() && coordA.getRow() == coordB.getRow();
        if (coordinatesMatch) {
            coordA.setValid(false);
            coordB.setValid(false);
        }
        return coordinatesMatch;
    }

    public OpError getErrors(){
        return errors;
    }
}
