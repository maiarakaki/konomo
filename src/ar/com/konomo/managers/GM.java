package ar.com.konomo.managers;

import ar.com.konomo.Factories.Factory;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.Player;
import ar.com.konomo.operators.CoordinateSplitter;
import ar.com.konomo.validators.CoordinateValidator;

import java.util.ArrayList;
import java.util.List;

import static ar.com.konomo.Main.BOARD_SIZE;
import static ar.com.konomo.Main.NINJAS;


public class GM {
    private Factory factory;
    private List<Player> players;
    private CoordinateValidator coordinateValidator;
    private CoordinateSplitter coordinateSplitter;

    public void createGame(){
        factory= new Factory();
        players = factory.createPlayers(NINJAS, BOARD_SIZE);
        coordinateValidator = new CoordinateValidator();
        coordinateSplitter = new CoordinateSplitter();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void validate(String[] coordinatesToValidate){
        List <Coordinate> coordinateList = new ArrayList<>();
        for (String entry: coordinatesToValidate
             ) {
            coordinateList.add(coordinateSplitter.split(entry));
        }

        for (Coordinate coordinate: coordinateList
             ) {
            coordinateValidator.validate(coordinate);
        }

    }
}
