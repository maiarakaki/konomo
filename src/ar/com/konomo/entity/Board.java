package ar.com.konomo.entity;

public class Board {
    private Placeable[][] board;

    public Board(int rows, int cols) {
        board = new  Placeable[rows][cols];
    }

    public Placeable[][] getBoard() {
        return board;
    }

    public void setBoard(Placeable[][] board) {
        this.board = board;
    }

    public void place(int row, int column, Placeable item) {
        try {
            board[row][column] = item;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
