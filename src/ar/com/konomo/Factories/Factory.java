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

    public List<Player> createPlayers(int numberOfNinjas, int boardsize){
        List<Player> players = new ArrayList<>();
        players.add(playerFactory.create(numberOfNinjas, boardsize));
        players.add(playerFactory.create(numberOfNinjas, boardsize));
        return players;
    }
}
