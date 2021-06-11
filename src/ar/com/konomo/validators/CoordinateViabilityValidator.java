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
    private static final String NON_VIABLE_COORD_MSG = "Una o más coordenadas están repetidas!";

    public CoordinateViabilityValidator (){
        errors = new OpError();
    }

    public boolean isViable(List<Coordinate> coordinateList){
        oneAndTwo = compare(coordinateList.get(0),coordinateList.get(1));
        oneAndThree = compare(coordinateList.get(0),coordinateList.get(0));
        twoAndThree = compare(coordinateList.get(1), coordinateList.get(2));

        if (oneAndTwo || oneAndThree || twoAndThree) {
            errors.add(NON_VIABLE_COORD, NON_VIABLE_COORD_MSG);
        }

        return !(oneAndTwo || oneAndThree || twoAndThree);
    }

    private boolean compare(Coordinate coordA, Coordinate coordB) {
        boolean coordinatesMatch;
        coordinatesMatch = coordA.getColumn() == coordA.getColumn() && coordA.getRow() == coordB.getRow();
        if (coordinatesMatch) {
            coordB.setValid(false);
        }
        return coordinatesMatch;
    }

    public OpError getErrors(){
        return errors;
    }

}
