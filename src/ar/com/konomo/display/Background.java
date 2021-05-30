package ar.com.konomo.display;

public class Background {
    private int rows;
    private int columns;
    private String[][] background;
    static int HORIZONTAL_PADDING = 6;
    static int BOARDS = 2;
    static int VERTICAL_PADDING = 10;
    static int GRID_PADDING_X = 2;
    static int GRID_PADDING_Y = 2;


    public Background (int rows, int columns){
        background = new String[rows * GRID_PADDING_Y + VERTICAL_PADDING][columns * BOARDS + HORIZONTAL_PADDING];
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String[][] getBackground() {
        return background;
    }

    public void setBackground(String[][] background) {
        this.background = background;
    }
}
