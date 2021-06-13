package ar.com.konomo.entity;

import ar.com.konomo.enums.Action;

public class Intention {
    private Action action;
    private Coordinate coordinate;
    private boolean isValid;

    public Intention(Action action, Coordinate coordinate) {
        this.action = action;
        this.coordinate = coordinate;
        isValid = false;
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
}
