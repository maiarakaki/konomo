package ar.com.konomo.entity;

public class Coordinate {
    private int row;
    private int column;
    boolean isValid;

    public Coordinate(int column, int row){
        this.column = column;
        this.row = row;
    }

    public Coordinate(){}

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
