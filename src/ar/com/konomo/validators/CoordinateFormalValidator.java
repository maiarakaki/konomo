package ar.com.konomo.validators;

import ar.com.konomo.entity.CoordinateIn;
import ar.com.konomo.entity.OpError;


public class CoordinateFormalValidator {
    private CoordinateIn coordinate;
    private static final int INVALID_INPUT_X = 1;
    private static final int INVALID_INPUT_Y = 2;
    private static final String INVALID_INPUT_X_MSG = "El primer valor debe ser una letra!";
    private static final String INVALID_INPUT_Y_MSG = "El segundo valor debe ser un número!";
    private OpError opError;

    public CoordinateFormalValidator(){
        opError = new OpError();
    }

    public boolean validate(String userInput, CoordinateIn coordinate){
        this.coordinate = coordinate;
        boolean isValidChar =getAlpha(coordinate, userInput);
        boolean isValidNumber = getNumber(coordinate, userInput);

        return isValidChar && isValidNumber;
    }

    private boolean getAlpha(CoordinateIn coordinate, String userInput) {
        boolean success = false;
        char alpha='\0';
        try {
            alpha =userInput.charAt(0);
            if (!Character.isDigit(alpha)) {
                coordinate.setColumn(alpha);
                success= true;
            } else {
                throw new Exception(INVALID_INPUT_X_MSG);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            opError.add(INVALID_INPUT_X +": " + INVALID_INPUT_X_MSG);
        }
        return success;
    }

    private boolean getNumber(CoordinateIn coordinate, String userInput) {
        boolean success= false;
        int number = 0;
        try {
           number += Integer.parseInt(userInput.substring(1));
            success= true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            opError.add(INVALID_INPUT_Y +": " + INVALID_INPUT_Y_MSG);
        }
        coordinate.setRow(number);
        return success;
    }

    public OpError getError() {
        return opError;
    }


}
