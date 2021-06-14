package ar.com.konomo.display;
import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

    public List<Coordinate> getPlayerCoordinates(){

        List<Coordinate> coordinates= new ArrayList<>();
        try {
            coordinates= ninjaPlacing.getCoordenates("Despliega tus ninjas!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return coordinates;
    }





}


