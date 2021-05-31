package ar.com.konomo.display;
import java.util.Scanner;

public class HomeScreen {
    Scanner scan = new Scanner(System.in);

    public String showOptions(){
        String userInput = "";
        try {
            System.out.printf("%s", "N – Nueva partida\nC – Conectarse a una partida\nX – Salir\n");
            userInput = scan.nextLine();
        }catch (Exception ex) {
            System.out.println("Could not retrieve user input");
        }
        return userInput;
    }
}
