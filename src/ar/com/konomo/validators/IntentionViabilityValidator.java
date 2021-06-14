package ar.com.konomo.validators;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.Intention;
import ar.com.konomo.entity.OpError;

import java.util.List;

public class IntentionViabilityValidator {
    boolean oneAndTwo;
    boolean oneAndThree;
    boolean twoAndThree;
    private OpError errors;
    private static final int NON_VIABLE_INT = 7;
    private static final String NON_VIABLE_INT_MSG = "Una o más acciones son idénticas!";

    public IntentionViabilityValidator (){
        errors = new OpError();
    }

    public boolean isViable(List<Intention> intentionsList){
        boolean isViable= false;
        int intentionNum= intentionsList.size();
        final int INT1 = 0;
        final int INT2 = 1;
        final int INT3 = 2;

        switch(intentionNum) {
            case 3:
                oneAndTwo = compare(intentionsList.get(INT1),intentionsList.get(INT2));
                oneAndThree = compare(intentionsList.get(INT1),intentionsList.get(INT3));
                twoAndThree = compare(intentionsList.get(INT2), intentionsList.get(INT3));
                isViable = ! (oneAndTwo || oneAndThree || twoAndThree);
                break;
            case 2:
                oneAndTwo = compare(intentionsList.get(INT1),intentionsList.get(INT2));
                isViable = !oneAndTwo;
                break;
            case 1:
                isViable = true;
                break;
            case 0:
                isViable = false;
        }

        if(!isViable){
            errors.add(NON_VIABLE_INT +": " + NON_VIABLE_INT_MSG);
        }

        return isViable;
    }

    private boolean compare(Intention intentionA, Intention intentionB) {
        boolean intentionsMatch;
        Coordinate coordinateA =  intentionA.getCoordinate();
        Coordinate coordinateB =  intentionB.getCoordinate();
        intentionsMatch = intentionA.getAction() == intentionB.getAction() && coordinateA.getColumn() == coordinateB.getColumn() && coordinateA.getRow() == coordinateB.getRow();
        if (intentionsMatch) {
            intentionA.setValid(false);
            intentionB.setValid(false);
        }
        return intentionsMatch;
    }

    public OpError getErrors(){
        return errors;
    }

}