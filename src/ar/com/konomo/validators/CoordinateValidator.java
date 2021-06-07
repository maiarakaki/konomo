package ar.com.konomo.validators;

import ar.com.konomo.entity.Coordinate;

public class CoordinateValidator {
    private Coordinate coordinate;

    public boolean validate(Coordinate coordinate){
        boolean isValidX =false;
        boolean isValidY =false;

        this.coordinate = coordinate;

        return isValidX && isValidY;
    }


}
