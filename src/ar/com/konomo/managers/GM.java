package ar.com.konomo.managers;

import ar.com.konomo.Factories.Factory;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.NinjaType;
import ar.com.konomo.validators.CoordinatePositionValidator;
import ar.com.konomo.validators.CoordinateRangeValidator;
import ar.com.konomo.validators.CoordinateViabilityValidator;

import java.util.List;

import static ar.com.konomo.Main.BOARD_SIZE;
import static ar.com.konomo.Main.NINJAS;


public class GM {
    private Factory factory;
    private Player player1;
    private Player player2;
    private CoordinateRangeValidator coordinateRangeValidator;
    private CoordinateViabilityValidator coordinateViabilityValidator;
    private CoordinatePositionValidator coordinatePositionValidator;
    private OpError errors;

    public void createGame(){
        factory= new Factory();
        player1 = factory.createPlayers(NINJAS, BOARD_SIZE);
        player2 = factory.createPlayers(NINJAS, BOARD_SIZE);
        coordinateRangeValidator = new CoordinateRangeValidator();
        coordinateViabilityValidator =  new CoordinateViabilityValidator();
        coordinatePositionValidator = new CoordinatePositionValidator();
        errors = new OpError();
    }

    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }

    public boolean validate(List<Coordinate> coordinates, Player player) {

        boolean allValid = false;

        if (coordinateRangeValidator.validate(coordinates)) {
            setNinjaCoordinates(coordinates, player.getMyNinjas());
            if (coordinateViabilityValidator.isViable(coordinates)) {
                setNinjaCoordinates(coordinates, player.getMyNinjas());
                if (coordinatePositionValidator.validate(player.getLocalBoard(), coordinates)) {
                    place(player.getMyNinjas(), player.getLocalBoard());
                    setNinjaCoordinates(coordinates, player.getMyNinjas());
                    allValid = true;
                } else {
                    errors.addAll(coordinatePositionValidator.getErrors());
                }
            } else {
                errors.addAll(coordinateViabilityValidator.getErrors());
            }
        } else {
            errors.addAll(coordinateRangeValidator.getErrors());
        }
        return allValid;
    }

    public OpError getErrors(){
        return errors;
    }

    public void place(List<Shinobi> placeables, Board board) {
        try {
            for (Placeable item: placeables
            ) {
                board.place(item.getRowIndex(), item.getColumnIndex(), item);
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage() + " place ninjas en el board falló");
        }
    }

    private void setNinjaCoordinates (List<Coordinate> coords, List<Shinobi> ninjas){
        try {
            for (Coordinate coordinate: coords
            ) {
                if (coords.indexOf(coordinate) == NINJAS -1) {
                    ninjas.get(coords.indexOf(coordinate)).setNinjaType(NinjaType.JOUNIN);
                } else {
                    ninjas.get(coords.indexOf(coordinate)).setNinjaType(NinjaType.CHUUNIN);
                }
                ninjas.get(coords.indexOf(coordinate)).setColumnIndex(coordinate.getColumn()-10);
                ninjas.get(coords.indexOf(coordinate)).setRowIndex(coordinate.getRow());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " en setPleaceableCoords!");
        }
    }
}
