package ar.com.konomo.display;
import java.util.Scanner;

public class CreationScreen {
    Scanner scan = new Scanner(System.in);
    Background background = new Background(5,5);

    public String getIpAndPort(String title){
        background.setTitle(title);
        background.showBackground();
        String userInput;
        String userInput2;
        System.out.print("Dirección IP: ");
        userInput = scan.nextLine();
        System.out.print("Puerto: ");
        userInput2=scan.next();

        return userInput + ":"+userInput2;
    }
}
