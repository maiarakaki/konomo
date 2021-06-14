package ar.com.konomo.operators;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.CoordinateIn;
import ar.com.konomo.enums.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMessageLog {
    private List<String> playerLog;

    public EventMessageLog () {
        playerLog = new ArrayList<>();
    }

    private void logMessage(String message) {
        if (playerLog == null) {
            playerLog = new ArrayList<>();
        }
        playerLog.add(message);
    }

    public void log(Event event, Coordinate coord){
        CoordinateBuilder coordinateBuilder = new CoordinateBuilder();
        CoordinateIn coordToShow = coordinateBuilder.build(coord);
        String message;

        message = event.getMessage() + " " + coordToShow.getColumn() + ":" + coordToShow.getRow();

        logMessage(message);
    }

    public List<String> getPlayerLog() {
        return playerLog;
    }

    public void show (){
        for (String message: playerLog
             ) {
            System.out.println(message);
        }
    }
}
