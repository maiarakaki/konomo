package ar.com.konomo.display;


public class BoardMaker {

    static int BORDER_PADDING = 2;
    static int OFFSET = 2;
    static int BOARD_SIZE = 5;
    public static String[] yAxisLabels= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};

    static String[][] screenBoard = new String[BOARD_SIZE * BORDER_PADDING + OFFSET][BOARD_SIZE  * BORDER_PADDING+ OFFSET];


    public String[][] getBoard (){
        return screenBoard;
    }

    public void create(Borders borderSets) {
        int k = 0;
        for (int i = 0; i < screenBoard.length; i++) {
            for (int j = 0; j < screenBoard[0].length; j++) {
                if (i == 0 && j >= OFFSET && j % 2 == 0) {
                    screenBoard[i][j] = yAxisLabels[k];
                    k++;
                }
                if ( j == 0 && i >= OFFSET && i%2 == 0) {
                    int value = i/OFFSET;
                    if (value <10) {
                        screenBoard[i][j] = " "+ value;
                    } else
                        screenBoard[i][j] = ""+ value;
                }

                if (j%2!=0 && i >= OFFSET && i%2 == 0) {
                    screenBoard[i][j] = borderSets.getBorders(9);
                }

                if(i%2!=0) {
                    if (j>= OFFSET) {
                        if (j %2==0){
                        screenBoard[i][j] = borderSets.getBorders(10);
                        }

                        if (j == screenBoard.length-1) {
                            screenBoard[i][j] = borderSets.getBorders(5);
                        }
                    } if (j == OFFSET -1) {
                        screenBoard[i][j] = borderSets.getBorders(4);
                    }
                }
                if ( i == OFFSET-1) {
                    if (j == OFFSET -1) {
                        screenBoard[i][j] = borderSets.getBorders(0);
                    } else if (j == screenBoard.length -1) {
                        screenBoard[i][j] = borderSets.getBorders(2);
                    } else if (j%2!= 0){
                        screenBoard[i][j] = borderSets.getBorders(6);
                    }
                }
                if(j%2 != 0 && j > OFFSET -1 && j < screenBoard.length -1) {
                    if(i >2 && i < screenBoard.length -1 && i%2!=0) {
                    screenBoard[i][j] = borderSets.getBorders(8);
                    }
                }

                if (i == screenBoard.length -1 ) {
                    if (j == OFFSET -1 ) {
                        screenBoard[i][j] = borderSets.getBorders(1);
                    }
                    else if (j == screenBoard.length-1) {
                        screenBoard[i][j] = borderSets.getBorders(3);
                    }
                    else if (j%2!= 0) {
                        screenBoard[i][j] = borderSets.getBorders(7);
                    }
                }

                if (screenBoard[i][j] ==null) {
                    screenBoard[i][j] =" ";}
            }
        }
    }

    public void print() {
        for (int i = 0 ; i < screenBoard.length; i++) {
            for (int j = 0 ; j < screenBoard[i].length; j++) {
                System.out.print(String.format(" %s ", screenBoard[i][j]) );
            }
            System.out.print("\n");
        }
    }
}



