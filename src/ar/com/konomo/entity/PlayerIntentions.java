package ar.com.konomo.entity;

import java.util.List;
import java.util.Map;

public class PlayerIntentions {
    private Map<Integer, Intention> playerIntentions;
    private Board board;
    private List<Shinobi> ninjaList;

    public Map<Integer, Intention> getPlayerIntentions() {
        return playerIntentions;
    }

    public void setPlayerIntentions(Map<Integer, Intention> playerIntentions) {
        this.playerIntentions = playerIntentions;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Shinobi> getNinjaList() {
        return ninjaList;
    }

    public void setNinjaList(List<Shinobi> ninjaList) {
        this.ninjaList = ninjaList;
    }

    public PlayerIntentions(Map<Integer, Intention> playerIntentions, Board board, List<Shinobi> ninjaList) {
        this.playerIntentions = playerIntentions;
        this.board = board;
        this.ninjaList = ninjaList;
    }

    public PlayerIntentions(){}
}
