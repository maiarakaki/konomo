package ar.com.konomo.entity;

public class CoordinateIn {
    private char column;
    private int row;

    public CoordinateIn(){

    }

    public CoordinateIn(char column, int row){
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public void setColumn(char column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
