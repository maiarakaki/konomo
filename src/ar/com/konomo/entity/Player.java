package ar.com.konomo.entity;

import ar.com.konomo.enums.NinjaType;

import java.util.List;

public class Player {
    private String name;
    private List <Shinobi> myNinjas;
    private Board localBoard;
    private String[][] enemyBoard;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List <Shinobi> getMyNinjas() {
        return myNinjas;
    }

    public void setMyNinjas(List <Shinobi> myNinjas) {
        this.myNinjas = myNinjas;
    }

    public Board getLocalBoard() {
        return localBoard;
    }

    public void setLocalBoard(Board localBoard) {
        this.localBoard = localBoard;
    }

    public String[][] getEnemyBoard() {
        return enemyBoard;
    }

    public void setEnemyBoard(String[][] enemyBoard) {
        this.enemyBoard = enemyBoard;
    }
}
