package ar.com.konomo.display;
import java.util.Scanner;

public class CreationScreen {
    Scanner scan = new Scanner(System.in);
    Background background = new Background(5,5);
    private String ip;
    private String port;


    public String getIpAndPort(String title){
        String VALID_IP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String VALID_PORT = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$" ;
        background.setTitle(title);
        background.showBackground();
        String userInput;
        String userInput2;
        System.out.print("Dirección IP: ");
        userInput = scan.nextLine();
        while (!userInput.matches(VALID_IP)) {
            System.out.println("Debes ingresar una IP válida!");
            System.out.print("Dirección IP: ");
            userInput = scan.nextLine();
        }
        ip = userInput;

        System.out.print("Puerto: ");
        userInput2=scan.next();
        while (!userInput2.matches(VALID_PORT)) {
            System.out.println("Debes ingresar un puerto válido!");
            System.out.print("Puerto: ");
            userInput = scan.nextLine();
        }
        port = userInput2;

        return userInput + ":"+userInput2;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}