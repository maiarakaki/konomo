package ar.com.konomo.validators;

import java.util.ArrayList;
import java.util.List;

public class CoordinateTranslator {
    static String[] yAxisLabels= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
    private List<String> labelsCopy = new ArrayList<>();

    public int[] translate(String string){
        int[] coordinates = new int[2];
        String letter;
        try {
            copyLabels();
            letter = string.substring(0);
            if (labelsCopy.contains(letter)){
                System.out.printf("LETRA VALIDA");
                coordinates[0] = labelsCopy.indexOf(letter);
            }


        } catch (Exception ex) {
            System.out.println("manqueaste manipulando las coordenadas mana");
        }

        System.out.println(coordinates[0]);

        return coordinates;
    }

    private void copyLabels(){
        int i = 0;
        //NO TIENE Q IR HASTA EL LENGTH, SINO HASTA EL BOARDSIZE
        while(i < yAxisLabels.length) {
            labelsCopy.add(yAxisLabels[i]);
            i ++;
        }
    }
}
