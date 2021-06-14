package ar.com.konomo.enums;

public enum Event {
    HIT_CONNECTED("Le diste a algo!"),
    HIT_CONNECTED_NINJA ("Le diste un ninja!"),
    KILLED_COMMANDER("Mataste al comandante enemigo!"),
    GOT_KILLED ("Mataron a tu ninja"),
    COMMANDER_GOT_KILLED ("Mataron a tu comandante! Ya no podés mover a tus ninjas"),
    COMMANDER_GOT_HIT ("Le dieron a tu comandante!"),
    NOTHING_TO_SEE_HERE("No le diste a nada"),
    NOTHING_HAPPENED("Zafaste =D")
    ;

    private String message;

    Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
