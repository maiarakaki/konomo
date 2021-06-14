package ar.com.konomo.Factories;

import ar.com.konomo.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Factory {
    private PlayerFactory playerFactory;
    private NinjaCreator ninjaCreator;

    public Factory(){
        ninjaCreator = new NinjaCreator();
        playerFactory = new PlayerFactory(ninjaCreator);
    }

    public Player createPlayers(int numberOfNinjas, int boardsize){
        return playerFactory.create(numberOfNinjas, boardsize);
    }
}
