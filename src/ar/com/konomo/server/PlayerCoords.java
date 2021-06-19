package ar.com.konomo.server;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;
import ar.com.konomo.entity.Shinobi;

import java.util.List;

public class PlayerCoords {
    boolean allGood;
    private List<Coordinate> coords;
    private OpError errors;
    private List <Shinobi> ninjaList;

    public List<Shinobi> getNinjaList() {
        return ninjaList;
    }

    public void setNinjaList(List<Shinobi> ninjaList) {
        this.ninjaList = ninjaList;
    }


    public List<Coordinate> getCoords() {
        return coords;
    }

    public void setCoords(List<Coordinate> coords) {
        this.coords = coords;
    }

    public OpError getErrors() {
        return errors;
    }

    public void setErrors(OpError errors) {
        this.errors = errors;
    }

    public boolean isAllGood() {
        return allGood;
    }

    public void setAllGood(boolean allGood) {
        this.allGood = allGood;
    }
}
