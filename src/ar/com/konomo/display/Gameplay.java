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
    private String userInput;
    private int userSelection;

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
        //int userSelection;
        //String userInput;
        while (i < NINJAS)
        {
            Shinobi ninja = player.getMyNinjas().get(i);
            if (ninja.isAlive()) {
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
        Coordinate ninjaCoord = new Coordinate(ninja.getColumnIndex()+10, ninja.getRowIndex());
        CoordinateIn coordToShow = coordBuilder.build(ninjaCoord);
        try {
            if (ninja.isAlive()) {

                if (index != NINJAS - 1) {
                    System.out.printf("Qué debería hacer el ninja %d (%s:%d)?\n", index + 1, coordToShow.getColumn(), coordToShow.getRow());
                } else {
                    System.out.println("¿Qué debería hacer el comandante (" + coordToShow.getColumn() + ":" + coordToShow.getRow() + ")?" );
                }
                System.out.println("1. Atacar");
                System.out.println("2. Moverse");
            } else return;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public Map<Integer, Intention> getPlayerIntentions(String title,Map<Integer, Intention> intentionsMap, List<Shinobi> ninjas) {
        System.out.println(title);
        CoordinateIn userCoordinate = new CoordinateIn();

        for (Map.Entry<Integer, Intention> set: intentionsMap.entrySet()
             ) {
            if (!set.getValue().isValid()) {
                askForAction(set.getKey(), ninjas.get(set.getKey()));
                try {
                    userInput= scanner.nextLine();
                    userSelection = Integer.parseInt(userInput);
                }catch (Exception ex) {
                    System.out.println(ex.getMessage()+"Debías seleccionar una opción válida U¬¬");
                    System.out.println("Defaulteamos en ataque :p");
                    userSelection = 1;
                }
                askforCoord(userSelection);

                userInput = scanner.nextLine();

                boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);

                if (isValid) {
                    Coordinate coordOut = coordBuilder.build(userCoordinate);
                    Intention intention = new Intention(Action.values()[userSelection-1], coordOut);
                    intentionsMap.put(set.getKey(), intention);
                }
            }
        }
        return intentionsMap;
    }
}


