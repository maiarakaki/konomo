package ar.com.konomo.display;

public class TitleScreen {
    static int SIZE = 10;
    static String[][] screen = new String[SIZE][SIZE];
    static String message= "Kokumo Monogatari";

    public void jumpStart(){
        fillBlanks();
        screen[5][5] = message;

        for (int i = 0 ; i < screen.length; i++) {
            for (int j = 0 ; j < screen[0].length; j++) {
                System.out.print(screen[i][j]  + " " );
            }
            System.out.print("\n");
        }
    }

    static void fillBlanks() {
        for (int i = 0 ; i < screen.length; i++) {
            for (int j = 0 ; j < screen[0].length; j++) {
                screen[i][j] = "    ";
            }
        }
    }
}
