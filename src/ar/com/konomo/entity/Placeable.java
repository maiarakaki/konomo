package ar.com.konomo.entity;

import ar.com.konomo.enums.NinjaType;

public abstract class Placeable {
    private int columnIndex;
    private int rowIndex;

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public abstract NinjaType getType ();
}
