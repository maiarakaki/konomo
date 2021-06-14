package ar.com.konomo.operators;

import ar.com.konomo.enums.Event;
import ar.com.konomo.enums.NinjaType;

public class EventClassifier {
    private Event event;

    public Event decideEvent(NinjaType ninjaType) {
        switch (ninjaType) {
            case CHUUNIN:
                event = Event.HIT_CONNECTED_NINJA;
                break;
            case FIAMBRENIN:
                event = Event.HIT_CONNECTED;
                break;
            case OBSTACLE:
                event = Event.NOTHING_TO_SEE_HERE;
                break;
        }
        return event;
    }

    public Event decideEvent(int jouninStamina) {
        try {
            if (jouninStamina == NinjaType.JOUNIN.getBaseStamina()/2) {
                event = Event.KILLED_COMMANDER;
            } else {
                event =  Event.HIT_CONNECTED;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return event;
    }
}
