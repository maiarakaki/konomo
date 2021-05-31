package ar.com.konomo.entity;

public class Player {
    private String name;
    private Chuunin [] myNinjas;
    private Board localBoard;
    private String[][] enemyBoard;


    public void placeTroops(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chuunin[] getMyNinjas() {
        return myNinjas;
    }

    public void setMyNinjas(Chuunin[] myNinjas) {
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
