package ar.com.konomo.managers;

import ar.com.konomo.Factories.Factory;
import ar.com.konomo.entity.*;
import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.NinjaType;
import ar.com.konomo.operators.AttackLogger;
import ar.com.konomo.operators.BoardUpdater;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.operators.NinjaPlacer;
import ar.com.konomo.validators.*;

import java.util.*;

import static ar.com.konomo.Main.BOARD_SIZE;
import static ar.com.konomo.Main.NINJAS;


public class GM {
    private Factory factory;
    private Player player1;
    private Player player2;
    private CoordinateRangeValidator coordinateRangeValidator;
    private CoordinateViabilityValidator coordinateViabilityValidator;
    private CoordinatePositionValidator coordinatePositionValidator;
    private OpError errors;
    private IntentionViabilityValidator intentionViabilityValidator;
    private MoveValidator moveValidator;
    private AttackLogger attackLogger;
    private EventMessageLog eventMessageLog;

    public void createGame(){
        factory= new Factory();
        player1 = factory.createPlayers(NINJAS, BOARD_SIZE);
        player2 = factory.createPlayers(NINJAS, BOARD_SIZE);
        coordinateRangeValidator = new CoordinateRangeValidator();
        coordinateViabilityValidator =  new CoordinateViabilityValidator();
        coordinatePositionValidator = new CoordinatePositionValidator();
        intentionViabilityValidator = new IntentionViabilityValidator();
        moveValidator = new MoveValidator(coordinatePositionValidator);
        attackLogger = new AttackLogger();
        errors = new OpError();
        eventMessageLog = new EventMessageLog();
    }

    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public boolean validate(List<Coordinate> coordinates, Player player) {

        boolean allValid = false;

        if (coordinateRangeValidator.validate(coordinates)) {
            setNinjaCoordinates(coordinates, player.getMyNinjas());
            if (coordinateViabilityValidator.isViable(coordinates)) {
                setNinjaCoordinates(coordinates, player.getMyNinjas());
                if (coordinatePositionValidator.validate(player.getLocalBoard(), coordinates)) {
                    place(player.getMyNinjas(), player.getLocalBoard());
                    setNinjaCoordinates(coordinates, player.getMyNinjas());
                    allValid = true;
                } else {
                    errors.addAll(coordinatePositionValidator.getErrors());
                }
            } else {
                errors.addAll(coordinateViabilityValidator.getErrors());
            }
        } else {
            errors.addAll(coordinateRangeValidator.getErrors());
        }
        return allValid;
    }

    private List <Coordinate> getIntentionTargets(Map <Integer, Intention> intentions){
        List <Coordinate> intentionTargets = new ArrayList<>();
        for (Map.Entry<Integer, Intention> record : intentions.entrySet()
        ) {
            intentionTargets.add(record.getValue().getCoordinate());
        }
        return intentionTargets;
    }

    private List<Intention> getIntentionList(Map <Integer, Intention> intentions){
        List <Intention> intentionsList = new ArrayList<>();
        for (Map.Entry<Integer, Intention> record : intentions.entrySet()
        ) {
            intentionsList.add(record.getValue());
        }
        return intentionsList;
    }

    public boolean validate(Map<Integer, Intention> playerIntentions, Player player) {

        boolean allValid = true;
        boolean commanderIsAlive = player.getMyNinjas().get(2).isAlive();
        List <Coordinate> coordinates = getIntentionTargets(playerIntentions);
        try {
            if (coordinateRangeValidator.validate(coordinates)) {
                List<Intention> intentionList = getIntentionList(playerIntentions);
                if(intentionViabilityValidator.isViable(intentionList)){
                    for (Map.Entry<Integer, Intention> record : playerIntentions.entrySet()
                    ) {
                        record.getValue().setValid(true);
                        if (record.getValue().getAction() == Action.MOVE) {
                            Shinobi ninja = player.getMyNinjas().get(record.getKey());
                            Coordinate coordinate = record.getValue().getCoordinate();
                            boolean moveIsValid = moveValidator.isValid(coordinate, ninja, commanderIsAlive, player.getLocalBoard());
                            if (!moveIsValid) {
                                record.getValue().setValid(false);

                                allValid =false;
                            }
                        }

                    }
                    if (!allValid) {
                        errors.addAll(moveValidator.getError());
                    }

                } else {
                    errors.addAll(intentionViabilityValidator.getErrors());
                    allValid =false;
                }
            } else {
                errors.addAll(coordinateRangeValidator.getErrors());
                allValid =false;
            }



        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allValid;
    }

    public OpError getErrors(){
        return errors;
    }

    public void place(List<Shinobi> placeables, Board board) {
/*        try {
            for (Placeable item: placeables
            ) {
                board.place(item.getRowIndex(), item.getColumnIndex(), item);
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage() + " place ninjas en el board falló");
        }*/
        try {
            NinjaPlacer.place(placeables, board);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setNinjaCoordinates (List<Coordinate> coords, List<Shinobi> ninjas){
        try {
            for (Coordinate coordinate: coords
            ) {
                if (coords.indexOf(coordinate) == NINJAS -1) {
                    ninjas.get(coords.indexOf(coordinate)).setNinjaType(NinjaType.JOUNIN);
                } else {
                    ninjas.get(coords.indexOf(coordinate)).setNinjaType(NinjaType.CHUUNIN);
                }
                ninjas.get(coords.indexOf(coordinate)).setColumnIndex(coordinate.getColumn()-10);
                ninjas.get(coords.indexOf(coordinate)).setRowIndex(coordinate.getRow());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " en setPleaceableCoords!");
        }
    }

    public void updateBoards(Player player, Map <Integer, Intention> intentionMap, Board enemyBoard) {
        attackLogger.clear();
        BoardUpdater boardUpdater = new BoardUpdater();
        Board myBoard = player.getLocalBoard();
        String[][] knownEnemyBoard = player.getEnemyBoard();
        List <Shinobi> movedNinjas = new ArrayList<>();

        for (Map.Entry<Integer, Intention> record: intentionMap.entrySet()
        ) {
            Coordinate coordinate = record.getValue().getCoordinate();
            Shinobi myNinja = player.getMyNinjas().get(record.getKey());
            //FIXME TRANSFORMAR LAS COORDENADAS UNA VEZ Q VALIDO QUE ESTÁN EN RANGO PARA QUE SEA MAS FACIL OPERAR CON ELLAS EN TODOS LADOS!
            switch (record.getValue().getAction()) {
                case MOVE :
                    boardUpdater.update(myBoard, record.getValue(), myNinja);
                    movedNinjas.add(myNinja);
                    myNinja.setLastActionTaken(Action.MOVE);
                    break;
                case ATTACK:
                    attackLogger.logAttacks(record.getValue());
                    try {
                        boardUpdater.update(knownEnemyBoard, enemyBoard, coordinate);
                        myNinja.setLastActionTaken(Action.ATTACK);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage() + " Fallamos updateando la representacion del board lpm");
                    }
                    break;
            }
        }
        place(movedNinjas, myBoard);
        eventMessageLog = boardUpdater.getEventLog();
    }

    public void updateBoards(Player player, Map <Integer, Intention> attackList) {
        eventMessageLog.getPlayerLog().clear();
        BoardUpdater boardUpdater = new BoardUpdater();

        for (Map.Entry<Integer, Intention> record: attackList.entrySet()
        ) {
            Coordinate target = record.getValue().getCoordinate();
            boardUpdater.update(player.getLocalBoard(), target);

        }
        eventMessageLog = boardUpdater.getEventLog();
        attackList.clear();
    }

    public EventMessageLog getEventLog() {
        return eventMessageLog;
    }

    public AttackLogger getAttackLogs(){
        if (attackLogger == null) {
            attackLogger = new AttackLogger();
        }
        return attackLogger;
    }

    public boolean validate(List<Coordinate> coordinates, List <Shinobi>ninjaList) {

        boolean allValid = false;

        if (coordinateRangeValidator.validate(coordinates)) {
            setNinjaCoordinates(coordinates, ninjaList);
            if (coordinateViabilityValidator.isViable(coordinates)) {
                setNinjaCoordinates(coordinates, ninjaList);
                place(ninjaList, player2.getLocalBoard());
                allValid = true;
            } else {
                errors.addAll(coordinateViabilityValidator.getErrors());
            }
        } else {
            errors.addAll(coordinateRangeValidator.getErrors());
        }
        return allValid;
    }

    public List <Coordinate> getIntentionTargets(List<Intention> clientIntentions) {
        List <Coordinate> coordinates = new ArrayList<>();
        for (Intention intention: clientIntentions
        ) {
            coordinates.add(intention.getCoordinate());
        }
        return coordinates;
    }

    public boolean validate(List<Intention> playerIntentions) {

        boolean allValid = true;
        boolean commanderIsAlive = player2.getMyNinjas().get(2).isAlive();
        List <Coordinate> coordinates = getIntentionTargets(playerIntentions);
        try {
            if (coordinateRangeValidator.validate(coordinates)) {
                // List<Intention> intentionList = playerIntentions;
                if(intentionViabilityValidator.isViable(playerIntentions)){
                    int i = 0;
                    for (Intention intention: playerIntentions
                    ) {
                        if (intention.getAction() == Action.MOVE) {
                            Shinobi ninja = player2.getMyNinjas().get(i);
                            Coordinate coordinate = intention.getCoordinate();
                            boolean moveIsValid = moveValidator.isValid(coordinate, ninja, commanderIsAlive, player2.getLocalBoard());
                            if (!moveIsValid){
                                intention.setValid(false);
                            }
                            allValid = false;
                        }
                    }

                    if (!allValid) {
                        errors.addAll(moveValidator.getError());
                    }

                } else {
                    errors.addAll(intentionViabilityValidator.getErrors());
                    allValid =false;
                }
            } else {
                errors.addAll(coordinateRangeValidator.getErrors());
                allValid =false;
            }



        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return allValid;
    }

}

