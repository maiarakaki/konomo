package ar.com.konomo.display;
import java.util.Arrays;
import java.util.Scanner;

public class TitleScreen {
    static int SIZE = 10;
    Scanner scan = new Scanner(System.in);
    private final String[][] screen = new String[SIZE][SIZE];
    private final String message= "Kokumo Monogatari";
    private final String nextMessage = "Press Enter key to continue . . . ";


    public void jumpStart(){

        fillBlanks();
        screen[SIZE /2][SIZE/2] = message;
        screen[SIZE/2 +2][SIZE / 2 - 2] = nextMessage;

        for (String[] strings : screen) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.print("\n");
        }

        Read();
    }

    private void fillBlanks() {
        try {
            for (String[] row : screen) {
                Arrays.fill(row, " ");
            }

        } catch (Exception ex) {
            System.out.println("ESTO NO FUNCAAA");
        }

    }

    void Read()
    {
        scan.nextLine();
    }
}
