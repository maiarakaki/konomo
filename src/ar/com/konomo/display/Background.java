package ar.com.konomo.display;


public class Background {
    private int rows;
    private int columns;
    private String[][] background;
/*    static int HORIZONTAL_PADDING = 6;
    static int BOARDS = 2;
    static int VERTICAL_PADDING = 10;
    static int GRID_PADDING_X = 2;
    static int GRID_PADDING_Y = 2;*/

    public String[][] getBackground() {
        return background;
    }

    public void setBackground(String[][] background) {
        this.background = background;
    }

    public void setTitle(String title) {

        background[rows/2][columns/2] = title;
    }

    public void showBackground(){
        fillBlanks();
        for (String[] strings : background) {
            for (int j = 0; j < background[0].length; j++) {
                System.out.printf(" %s ", strings[j]);
            }
            System.out.print("\n");
        }
    }

    private void fillBlanks() {
        for (int i = 0 ; i < background.length; i++) {
            for (int j = 0 ; j < background[i].length; j++) {
                if (background[i][j] == null) {
                    background[i][j] = " ";
                }
            }
        }
    }

    public Background (int rows, int columns){
        setRows(rows) ;
        setColumns(columns);
        this.background = new String[rows][columns];
    }


    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void fillBackground(String title, ScreenBoard boardA, ScreenBoard boardB, int margin, int offset) {
        background[5][background.length-offset] = title;
        background[offset -2][5] = "Tu tablero";
        background[offset -2][background.length-offset/2] = "Tablero enemigo";

        try {
            for (int i= offset; i < background.length; i++) {
                for (int j = 0; j < boardA.screenBoard.length ; j++)
                    background[i][j] = boardA.getScreenBoard()[i-offset][j];
                for (int j = 0; j < margin ; j++)
                    background[i][j+boardA.screenBoard.length] = " ";
                for (int j = 0 ; j < boardB.getScreenBoard().length; j++)
                    background[i][j + boardA.getScreenBoard().length+margin] = boardB.getScreenBoard()[i-offset][j];
            }

        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
