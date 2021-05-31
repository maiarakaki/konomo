package ar.com.konomo.entity;

public class Board {
    private Placeable[][] board;

    public Board(int rows, int cols) {
        board = new  Placeable[rows][cols];
    }


}
