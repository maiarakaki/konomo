package ar.com.konomo.display;

import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.operators.CoordinateBuilder;
import ar.com.konomo.validators.CoordinateFormalValidator;


import java.util.*;

import static ar.com.konomo.constants.Constants.ALPHA_OFFSET;
import static ar.com.konomo.constants.Constants.NINJAS;

public class Gameplay {
    private CoordinateFormalValidator coordinateFormalValidator;
    private CoordinateBuilder coordBuilder;
    private Scanner scanner;
    private String userInput;

    public Gameplay (Scanner scanner, CoordinateFormalValidator coordinateFormalValidator, CoordinateBuilder coordinateBuilder){
        this.scanner =  scanner;
        this.coordinateFormalValidator = coordinateFormalValidator;
        this.coordBuilder = coordinateBuilder;
    }

    public Map<Integer, Intention> getPlayerIntentions(Player player){
        CoordinateIn userCoordinate = new CoordinateIn();
        Map<Integer, Intention> intentionSet = new HashMap<>();

        int i = 0;

        while (i < NINJAS)
        {
            Shinobi ninja = player.getMyNinjas().get(i);
            if (ninja.isAlive()) {
                askForAction(i, ninja); //pido accion

                String userAction = scanner.nextLine();
                userAction= askforCoord(userAction, i, ninja );


                userInput = scanner.nextLine();

                boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);


                if (isValid) {
                    Coordinate coordOut = coordBuilder.build(userCoordinate);
                    Intention intention = new Intention(getAction(userAction), coordOut);
                    intentionSet.put(i, intention);
                } else {
                    i--;
                }

            }

            i++;

        }


        return intentionSet;
    }

    /**
     * Asks for user desired action till a valid value is provided.
     */
    private String askforCoord(String input, int index, Shinobi ninja){
        switch (input) {
                case "1":
                    System.out.println("¿Atacar a dónde?");
                    break;
                case "2":
                    System.out.println("¿Mover a dónde?");
                    break;
                default:
                    System.out.println("Las opciones eran 1 o 2!");
                    System.out.println("Intentémoslo nuevamente...");
                    askForAction(index, ninja);
                    input = scanner.nextLine();
                    askforCoord(input, index, ninja);
            }
        return input;
    }

    private void askForAction (int index, Shinobi ninja) {
        Coordinate ninjaCoord = new Coordinate(ninja.getColumnIndex()+ALPHA_OFFSET, ninja.getRowIndex());
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
            if (set.getValue()!= null && !set.getValue().isValid()) {
                askForAction(set.getKey(), ninjas.get(set.getKey()));

                String userAction= scanner.nextLine();
                userAction= askforCoord(userAction, set.getKey(), ninjas.get(set.getKey()) );


                userInput = scanner.nextLine();

                boolean isValid = coordinateFormalValidator.validate(userInput, userCoordinate);
                if (isValid) {
                    Coordinate coordOut = coordBuilder.build(userCoordinate);
                    Intention intention = new Intention(getAction(userAction), coordOut);
                    intentionsMap.put(set.getKey(), intention);
                }
            }
        }
        return intentionsMap;
    }

    private Action getAction(String userAction) {
        if (userAction.equals("1")){
            return Action.ATTACK;
        }else {
            return Action.MOVE;
        }
    }
}


