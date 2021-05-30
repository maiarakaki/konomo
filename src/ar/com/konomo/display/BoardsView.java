package ar.com.konomo.display;

public class BoardsView {
    static int HORIZONTAL_PADDING = 6;
    static int BOARDS = 2;
    static int VERTICAL_PADDING = 10;
    static int BORDER_PADDING = 2;
    static int OFFSET = 2;
    static int BOARD_SIZE = 5;
    private Borders borderSets = new SimpleBorders();
    static String[] yAxisLabels= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};



    static String[][] screen = new String[BOARD_SIZE * BORDER_PADDING + OFFSET][BOARD_SIZE  * BORDER_PADDING+ OFFSET];

    public void jumpStart(){
        fillBlanks();
        //screen[5][5] = message;

        for (int i = 0 ; i < screen.length; i++) {
            for (int j = 0 ; j < screen[0].length; j++) {
                System.out.print(String.format(" %s ", screen[i][j]) );
            }
            System.out.print("\n");
        }
    }

    public void fillBlanks() {
        int k = 0;
        for (int i = 0 ; i < screen.length; i++) {
            for (int j = 0 ; j < screen[0].length; j++) {
                if (i == 0 && j >= OFFSET && j % 2 == 0) {
                    screen[i][j] = yAxisLabels[k];
                    k++;
                }
                if ( j == 0 && i >= OFFSET && i%2 == 0) {
                    Integer value = i/OFFSET;
                    if (value.intValue() <10) {
                        screen[i][j] = " "+ value.toString();
                    } else
                        screen[i][j] = value.toString();
                }

                if (j%2!=0 && i >= OFFSET && i%2 == 0) {
                    screen[i][j] = borderSets.getBorders(9);
                }

                if(i%2!=0) {
                    if (j>= OFFSET) {
                        if (j %2==0){
                        screen[i][j] = borderSets.getBorders(10);
                        }

                        if (j == screen.length-1) {
                            screen[i][j] = borderSets.getBorders(5);
                        }
                    } if (j == OFFSET -1) {
                        screen[i][j] = borderSets.getBorders(4);
                    }
                }
                if ( i == OFFSET-1) {
                    if (j == OFFSET -1) {
                        screen[i][j] = borderSets.getBorders(0);
                    } else if (j == screen.length -1) {
                        screen[i][j] = borderSets.getBorders(2);
                    } else if (j%2!= 0){
                        screen[i][j] = borderSets.getBorders(6);
                    }
                }
                if(j%2 != 0 && j > OFFSET -1 && j <screen.length -1) {
                    if(i >2 && i < screen.length -1 && i%2!=0) {
                    screen[i][j] = borderSets.getBorders(8);
                    }
                }

                if (i == screen.length -1 ) {
                    if (j == OFFSET -1 ) {
                        screen[i][j] = borderSets.getBorders(1);
                    }
                    else if (j == screen.length-1) {
                        screen[i][j] = borderSets.getBorders(3);
                    }
                    else if (j%2!= 0) {
                        screen[i][j] = borderSets.getBorders(7);
                    }
                }

                if (screen[i][j] ==null) {screen[i][j] =" ";}
            }
        }
    }
}
