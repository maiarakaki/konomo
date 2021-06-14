package ar.com.konomo.operators;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.CoordinateIn;

public class CoordinateBuilder {
    private Coordinate coordinate;

    public Coordinate build(CoordinateIn coordinate){
        this.coordinate = new Coordinate();
        this.coordinate.setRow(coordinate.getRow() - 1);
        this.coordinate.setColumn(Character.getNumericValue(coordinate.getColumn()));
        return this.coordinate;
    }

    public CoordinateIn build(Coordinate coordinate) {
        CoordinateIn coordinateIn = new CoordinateIn();
        coordinateIn.setRow(coordinate.getRow()+1);

        int colvalue= coordinate.getColumn() + 55;
        char alpha = (char) colvalue;
        coordinateIn.setColumn(alpha);

        return coordinateIn;
    }

}
