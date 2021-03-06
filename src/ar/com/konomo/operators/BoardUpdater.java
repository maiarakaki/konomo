package ar.com.konomo.operators;

import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Event;
import ar.com.konomo.enums.NinjaType;

import static ar.com.konomo.constants.Constants.ALPHA_OFFSET;
import static ar.com.konomo.enums.Event.NOTHING_HAPPENED;
import static ar.com.konomo.enums.Event.NOTHING_TO_SEE_HERE;

public class BoardUpdater {
    private EventMessageLog eventLog;
    private EventClassifier classifier;
    private String valueToWrite;
    private JackpotChecker jackpotChecker;
    private NinjaType whatsThere;
    private Event event;

    public BoardUpdater(){
        eventLog = new EventMessageLog();
        classifier = new EventClassifier();
        jackpotChecker = new JackpotChecker();
    }

    public void update(Board board, Coordinate attackTarget) {

        if(jackpotChecker.check(attackTarget, board)) {
            whatsThere = jackpotChecker.whatIsThere(attackTarget, board);
            switch (whatsThere) {
                case CHUUNIN:
                case JOUNIN:
                    Shinobi ninja = (Shinobi) board.getBoard()[attackTarget.getRow()][attackTarget.getColumn()-ALPHA_OFFSET ];
                    updateNinja(ninja);
                    break;
            }

        } else {
            Obstacle voidTile = new Obstacle();
            board.place(attackTarget.getRow(), attackTarget.getColumn()-ALPHA_OFFSET , voidTile);
            logEvent(NOTHING_HAPPENED, attackTarget);
        }
    }

    private void updateNinja(Shinobi ninja) {
        ninja.setStamina(ninja.getStamina()-NinjaType.CHUUNIN.getBaseStamina());
        Coordinate coord = new Coordinate(ninja.getColumnIndex() +ALPHA_OFFSET , ninja.getRowIndex());
        if (ninja.getStamina() <= 0) {
            ninja.setAlive(false);
            if (ninja.getNinjaType() == NinjaType.JOUNIN) {
                logEvent(Event.COMMANDER_GOT_KILLED, coord);
            } else {
                logEvent(Event.GOT_KILLED, coord);
            }
            ninja.setNinjaType(NinjaType.FIAMBRENIN);
        } else {
            logEvent(Event.COMMANDER_GOT_HIT, coord);
        }

    }

    public void update(Board board, Intention intention, Shinobi ninja) {
        board.getBoard()[ninja.getRowIndex()][ninja.getColumnIndex()] = null;
        ninja.setRowIndex(intention.getCoordinate().getRow());
        ninja.setColumnIndex(intention.getCoordinate().getColumn()-ALPHA_OFFSET );
    }

    public void update(String [][] knownBoard, Board actualEnemyBoard, Coordinate target){
        valueToWrite = "";

        if (jackpotChecker.check(target, actualEnemyBoard)) {

            whatsThere = jackpotChecker.whatIsThere(target, actualEnemyBoard);
            if (whatsThere == NinjaType.JOUNIN) {
                int jouninStamina = actualEnemyBoard.getBoard()[target.getRow()][target.getColumn()-ALPHA_OFFSET ].getStamina();
                event = classifier.decideEvent(jouninStamina);
                valueToWrite = decideJouninValue(event);

            } else {
                event = classifier.decideEvent(whatsThere);
                valueToWrite = whatsThere.getValue();
            }
            logEvent(event, target);
        } else {
            //significa que era null, por lo tanto en esa posici??n a??n no hay nada, ni un obst??culo
            valueToWrite = NinjaType.OBSTACLE.getValue();
            logEvent(NOTHING_TO_SEE_HERE, target);
        }
        writeBoard(target,valueToWrite, knownBoard);
    }

    private String decideJouninValue(Event event){

        switch (event){
            case KILLED_COMMANDER : valueToWrite =  NinjaType.FIAMBRENIN.getValue();
                break;
            case HIT_CONNECTED : valueToWrite =  "?";
                break;
            default: valueToWrite = "";
        }
        return valueToWrite;
    }


    private void writeBoard(Coordinate coordinate, String value, String[][] board){
        try {
            board[coordinate.getRow()][coordinate.getColumn()-ALPHA_OFFSET ] = value;
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }




    private void logEvent(Event type, Coordinate coord){
        try {
            eventLog.log(type, coord);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public EventMessageLog getEventLog() {
        return eventLog;
    }
}
