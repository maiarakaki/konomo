package ar.com.konomo.entity;

import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.NinjaType;

public class Intention {
    private Action action;
    private Coordinate coordinate;
    private boolean isValid;
    private NinjaType ninjaType;

    public Intention(Action action, Coordinate coordinate) {
        this.action = action;
        this.coordinate = coordinate;
        isValid = false;
        this.ninjaType = NinjaType.OBSTACLE;
    }

    public Intention (){}

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }

    public NinjaType getNinjaType() {
        return ninjaType;
    }

    public void setNinjaType(NinjaType ninjaType) {
        this.ninjaType = ninjaType;
    }
}
