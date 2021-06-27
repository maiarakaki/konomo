package ar.com.konomo.operators;

import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Event;
import ar.com.konomo.enums.NinjaType;

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
                    Shinobi ninja = (Shinobi) board.getBoard()[attackTarget.getRow()][attackTarget.getColumn()-10];
                    updateNinja(ninja);
                    break;
            }

        } else {
            Obstacle voidTile = new Obstacle();
            board.place(attackTarget.getRow(), attackTarget.getColumn()-10, voidTile);
            logEvent(NOTHING_HAPPENED, attackTarget);
        }
    }

    private void updateNinja(Shinobi ninja) {
        ninja.setStamina(ninja.getStamina()-NinjaType.CHUUNIN.getBaseStamina());
        Coordinate coord = new Coordinate(ninja.getColumnIndex() +10, ninja.getRowIndex());
        if (ninja.getStamina() <= 0) {
            ninja.setAlive(false);
            ninja.setNinjaType(NinjaType.FIAMBRENIN);
            if (ninja.getNinjaType() == NinjaType.JOUNIN) {
                logEvent(Event.COMMANDER_GOT_KILLED, coord);
            } else {
                logEvent(Event.GOT_KILLED, coord);
            }
        } else {
            logEvent(Event.COMMANDER_GOT_HIT, coord);
        }

    }

    public void update(Board board, Intention intention, Shinobi ninja) {
        board.getBoard()[ninja.getRowIndex()][ninja.getColumnIndex()] = null;
        ninja.setRowIndex(intention.getCoordinate().getRow());
        ninja.setColumnIndex(intention.getCoordinate().getColumn()-10);
    }

    public void update(String [][] knownBoard, Board actualEnemyBoard, Coordinate target){
        valueToWrite = "";

        if (jackpotChecker.check(target, actualEnemyBoard)) {

            whatsThere = jackpotChecker.whatIsThere(target, actualEnemyBoard);
            if (whatsThere == NinjaType.JOUNIN) {
                int jouninStamina = actualEnemyBoard.getBoard()[target.getRow()][target.getColumn()-10].getStamina();
                event = classifier.decideEvent(jouninStamina);
                valueToWrite = decideJouninValue(event);

            } else {
                event = classifier.decideEvent(whatsThere);
                valueToWrite = whatsThere.getValue();
            }
            logEvent(event, target);
        } else {
            //significa que era null, por lo tanto en esa posición aún no hay nada, ni un obstáculo
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
            board[coordinate.getRow()][coordinate.getColumn()-10] = value;
        }catch (Exception ex) {
            System.out.println(ex.getMessage() + "jksldañjfklds error escribiendo en el board imaginariooo");
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
