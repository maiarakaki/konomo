package ar.com.konomo.server;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;

import java.util.List;

public class PlayerCoords {
    private Player player;
    private List<Coordinate> coords;
    private OpError errors;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
}
