package ar.com.konomo.managers;

import ar.com.konomo.Factories.Factory;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.NinjaType;
import ar.com.konomo.validators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private IntentionViabilityValidator intentionViabilityValidator;
    private MoveValidator moveValidator;

    public void createGame(){
        factory= new Factory();
        player1 = factory.createPlayers(NINJAS, BOARD_SIZE);
        player2 = factory.createPlayers(NINJAS, BOARD_SIZE);
        coordinateRangeValidator = new CoordinateRangeValidator();
        coordinateViabilityValidator =  new CoordinateViabilityValidator();
        coordinatePositionValidator = new CoordinatePositionValidator();
        intentionViabilityValidator = new IntentionViabilityValidator();
        moveValidator = new MoveValidator(coordinatePositionValidator);
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

    private List <Coordinate> getIntentionTargets(Map <Integer, Intention> intentions){
        List <Coordinate> intentionTargets = new ArrayList<>();
        for (Map.Entry<Integer, Intention> record : intentions.entrySet()
        ) {
            intentionTargets.add(record.getValue().getCoordinate());
        }
        return intentionTargets;
    }

    private List<Intention> getIntentionList(Map <Integer, Intention> intentions){
        List <Intention> intentionsList = new ArrayList<>();
        for (Map.Entry<Integer, Intention> record : intentions.entrySet()
        ) {
            intentionsList.add(record.getValue());
        }
        return intentionsList;
    }

    public boolean validate(Map<Integer, Intention> playerIntentions, Player player) {
        boolean allValid = true;
        boolean commanderIsAlive = player.getMyNinjas().get(2).isAlive();
        List <Coordinate> coordinates = getIntentionTargets(playerIntentions);
        try {
            if (coordinateRangeValidator.validate(coordinates)) {
                List<Intention> intentionList = getIntentionList(playerIntentions);
                if(intentionViabilityValidator.isViable(intentionList)){
                    for (Map.Entry<Integer, Intention> record : playerIntentions.entrySet()
                    ) {
                        record.getValue().setValid(true);
                        if (record.getValue().getAction() == Action.MOVE) {
                            Shinobi ninja = player.getMyNinjas().get(record.getKey());
                            Coordinate coordinate = record.getValue().getCoordinate();
                            boolean moveIsValid = moveValidator.isValid(coordinate, ninja, commanderIsAlive, player.getLocalBoard());
                            if (!moveIsValid) {
                                record.getValue().setValid(false);

                                allValid =false;
                            }
                        }

                    }
                    if (!allValid) {
                        errors.addAll(moveValidator.getError());
                    }

                } else {
                    errors.addAll(intentionViabilityValidator.getErrors());
                    allValid =false;
                }
            } else {
                errors.addAll(coordinateRangeValidator.getErrors());
                allValid =false;
            }



        }catch (Exception ex) {
            System.out.println(ex.getMessage());
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
