package ar.com.konomo.display;
import java.util.Scanner;

public class HomeScreen {
    Scanner scan = new Scanner(System.in);

    public String showOptions(){
        String userInput = "";
        try {
            System.out.println("N - Nueva partida");
            System.out.println("C - Conectarse a una partida");
            System.out.println("X - Salir");
            userInput = scan.nextLine();
        }catch (Exception ex) {
            System.out.println("Could not retrieve user input");
        }
        return userInput;
    }
}
