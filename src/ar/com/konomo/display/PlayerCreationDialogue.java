package ar.com.konomo.display;
import ar.com.konomo.entity.Player;
import ar.com.konomo.validators.CoordinateValidator;

import java.util.Scanner;

import static ar.com.konomo.Main.NINJAS;

public class PlayerCreationDialogue {
    private Scanner scanner;
    private NinjaPlacing ninjaPlacing;

    public PlayerCreationDialogue(NinjaPlacing ninjaPlacing, Scanner scanner) {
        this.ninjaPlacing = ninjaPlacing;
        this.scanner = scanner;
     }

    public void getPlayerName(Player player){
        new Display().newScreen("Nuevo jugador");
        System.out.println("Nombre: ");
        try {
            player.setName(scanner.nextLine());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String[] getPlayerVariables(Player player){

        String coordinates[] = new String[NINJAS];
        try {

            coordinates = ninjaPlacing.getCoordenates("Despliega tus ninjas!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return coordinates;
    }

}
