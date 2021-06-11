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
    private List<Player> players;
    private CoordinateRangeValidator coordinateRangeValidator;
    private CoordinateViabilityValidator coordinateViabilityValidator;
    private CoordinatePositionValidator coordinatePositionValidator;
    private OpError errors;

    public void createGame(){
        factory= new Factory();
        players = factory.createPlayers(NINJAS, BOARD_SIZE);
        coordinateRangeValidator = new CoordinateRangeValidator();
        coordinateViabilityValidator =  new CoordinateViabilityValidator();
        coordinatePositionValidator = new CoordinatePositionValidator();
        errors = new OpError();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean validate(List<Coordinate> coordinates, Player player) {

        boolean allValid = false;
        int i = 0;

        if (coordinateRangeValidator.validate(coordinates)) {
            setNinjaCoordinates(coordinates, player.getMyNinjas());
            if (coordinateViabilityValidator.isViable(coordinates)) {
                setNinjaCoordinates(coordinates, player.getMyNinjas());
                if (coordinatePositionValidator.validate(player.getLocalBoard(), coordinates)) {
                    place(player.getMyNinjas(), player.getLocalBoard());
                    setNinjaCoordinates(coordinates, player.getMyNinjas());
                    allValid = true;
                } else {
                    errors.add(coordinatePositionValidator.getErrors());
                }
            } else {
                errors.add(coordinateViabilityValidator.getErrors());
            }
        } else {
            errors.add(coordinateRangeValidator.getErrors());
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
        int i = 0;
        try {
            for (Coordinate coordinate: coords
                 ) {
                if (i == 0) {
                    ninjas.get(i).setNinjaType(NinjaType.JOUNIN);
                } else {
                    ninjas.get(i).setNinjaType(NinjaType.CHUUNIN);
                }
                ninjas.get(i).setColumnIndex(coordinate.getColumn()-10);
                ninjas.get(i).setRowIndex(coordinate.getColumn()-10);
                i++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " en setPleaceableCoords!");
        }
    }


    //TODO Recibo una lista de coordenadas. Para cada una, valido si están en rango.
    // Si están, valido que en esa ubicación no haya otra cosa. Si no están, cambio el flag del Coordinate a invalid ¿cómo burbujeo el error?
    // Si la ubicación está vacía, enchufo al ninja en el board y le seteo sus coordenads.
}
