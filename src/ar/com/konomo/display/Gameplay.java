package ar.com.konomo.display;

import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.operators.CoordinateBuilder;
import ar.com.konomo.validators.CoordinateFormalValidator;
import com.sun.jdi.IntegerValue;

import java.util.*;

import static ar.com.konomo.Main.NINJAS;

public class Gameplay {
    private CoordinateFormalValidator coordinateFormalValidator;
    private CoordinateBuilder coordBuilder;
    private Scanner scanner;

    public Gameplay (Scanner scanner, CoordinateFormalValidator coordinateFormalValidator, CoordinateBuilder coordinateBuilder){
        this.scanner =  scanner;
        this.coordinateFormalValidator = coordinateFormalValidator;
        this.coordBuilder = coordinateBuilder;
    }

    public Map<Integer, Intention> getPlayerIntentions(Player player){
        CoordinateIn userCoordinate = new CoordinateIn();
        Map<Integer, Intention> intentionSet = new HashMap<>();

        //List <Intention> intentions = new ArrayList<>();
        int i = 0;
        int userSelection;
        String userInput;
        while (i < NINJAS)
        {
            Shinobi ninja = player.getMyNinjas().get(i);
            try {
                askForAction(i, ninja);
                userInput= scanner.nextLine();
                userSelection = Integer.parseInt(userInput);
            }catch (Exception ex) {
                System.out.println(ex.getMessage()+"Debías seleccionar una opción válida U¬¬");
                System.out.println("Defaulteamos en ataque :p");
                userSelection = 1;
            }

            //System.out.println(Action.values()[userSelection-1]);
           //scanner.nextLine();

            askforCoord(userSelection);

            userInput = scanner.nextLine();

            boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);

            if (isValid) {
                Coordinate coordOut = coordBuilder.build(userCoordinate);
                Intention intention = new Intention(Action.values()[userSelection-1], coordOut);
                intentionSet.put(i, intention);
            } else {
                i--;
            }

            i++;

        }


        return intentionSet;
    }
    private void askforCoord(int input){
            switch (input) {
                case 1:
                    System.out.println("¿Atacar a dónde?");
                    break;
                case 2:
                    System.out.println("¿Mover a dónde?");
                    break;
                default:
                    System.out.println("Las opciones eran 1 o 2!");
                    System.out.println("Intentémoslo nuevamente...");
                    try {
                        input = Integer.parseInt(scanner.nextLine());
                        askforCoord(input);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
            }
    }

    private void askForAction (int index, Shinobi ninja) {
        try {
            if (ninja.isAlive()) {
                if (index != NINJAS - 1) {
                    System.out.printf("%s %d %s\n", "¿Qué debería hacer el ninja ", index + 1, "?");
                } else {
                    System.out.println("¿Qué debería hacer el comandante?");
                }
                System.out.println("1. Atacar");
                System.out.println("2. Moverse");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
