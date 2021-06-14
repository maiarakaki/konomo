package ar.com.konomo.display;


import ar.com.konomo.entity.Board;

public class BoardMaker {

    static int BORDER_PADDING = 2;
    static int OFFSET = 2;
    static int BOARD_SIZE = 5;
    public static String[] yAxisLabels= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};

/*    public String[][] getBoard() {
        return screenBoard;
    }*/

    public ScreenBoard create(Borders borderSets) {
        ScreenBoard screenBoard = new ScreenBoard();
                //new String[BOARD_SIZE * BORDER_PADDING + OFFSET][BOARD_SIZE  * BORDER_PADDING+ OFFSET];

        int k = 0;
        for (int i = 0; i < screenBoard.getScreenBoard().length; i++) {
            for (int j = 0; j < screenBoard.getScreenBoard()[0].length; j++) {
                if (i == 0 && j >= OFFSET && j % 2 == 0) {
                    screenBoard.getScreenBoard()[i][j] = yAxisLabels[k];
                    k++;
                }
                if ( j == 0 && i >= OFFSET && i%2 == 0) {
                    int value = i/OFFSET;
                    if (value <10) {
                        screenBoard.getScreenBoard()[i][j] = " "+ value;
                    } else
                        screenBoard.getScreenBoard()[i][j] = ""+ value;
                }

                if (j%2!=0 && i >= OFFSET && i%2 == 0) {
                    screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(9);
                }

                if(i%2!=0) {
                    if (j>= OFFSET) {
                        if (j %2==0){
                            screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(10);
                        }

                        if (j == screenBoard.getScreenBoard().length-1) {
                            screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(5);
                        }
                    } if (j == OFFSET -1) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(4);
                    }
                }
                if ( i == OFFSET-1) {
                    if (j == OFFSET -1) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(0);
                    } else if (j == screenBoard.getScreenBoard().length -1) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(2);
                    } else if (j%2!= 0){
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(6);
                    }
                }
                if(j%2 != 0 && j > OFFSET -1 && j < screenBoard.getScreenBoard().length -1) {
                    if(i >2 && i < screenBoard.getScreenBoard().length -1 && i%2!=0) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(8);
                    }
                }

                if (i == screenBoard.getScreenBoard().length -1 ) {
                    if (j == OFFSET -1 ) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(1);
                    }
                    else if (j == screenBoard.getScreenBoard().length-1) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(3);
                    }
                    else if (j%2!= 0) {
                        screenBoard.getScreenBoard()[i][j] = borderSets.getBorders(7);
                    }
                }

                if (screenBoard.getScreenBoard()[i][j] ==null) {
                    screenBoard.getScreenBoard()[i][j] =" ";}
            }
        }
        return screenBoard;
    }


}



