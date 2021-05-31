package ar.com.konomo.validators;

public class LegitCoordinateValidator {
    private CoordinateTranslator coordinateTranslator = new CoordinateTranslator();

    public boolean validate(String userInput){
        try {

            coordinateTranslator.translate(userInput);
        } catch(Exception ex) {
            System.out.println("fallamo nel legitcoordinatorrr");
        }
        return false;

    }
}
