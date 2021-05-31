package ar.com.konomo.entity;

import java.util.List;

public class Player {
    private String name;
    private List <Shinobi> myNinjas;
    private Board localBoard;
    private String[][] enemyBoard;


    public void placeTroops(){
        try {
            for (Shinobi ninja: myNinjas
                 ) {
                if (ninja.isJounin()) {
                    System.out.println("Coordenadas del Comandante:");
                } else {
                    System.out.println("Coordenadas del ninja: ");
                }

            }
        } catch (Exception ex) {
            System.out.println("Ocurrió un problema al ubicar los ninjas!");
        }

    }

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
