package ar.com.konomo.entity;

import java.util.List;

public class IntentionPack {
    private List <Intention>intentions;
    private OpError errors;
    public boolean allGood;
    private String[][] knownBoard;

    public IntentionPack(List<Intention> intentions, List<Shinobi> ninjaList, boolean allGood, String[][] knownBoard) {
        this.intentions = intentions;
        this.allGood = allGood;
        this.knownBoard = knownBoard;
    }

    public void setAllGood(boolean allGood) {
        this.allGood = allGood;
    }

    public List<Intention> getIntentions() {
        return intentions;
    }


    public OpError getErrors() {
        if (errors == null) {
            errors = new OpError();
        }
        return errors;
    }

    public void setIntentions(List<Intention> intentions) {
        this.intentions = intentions;
    }

    public void setErrors(OpError errors) {
        this.errors = errors;
    }

    public String[][] getKnownBoard() {
        return knownBoard;
    }

    public void setKnownBoard(String[][] knownBoard) {
        this.knownBoard = knownBoard;
    }
}
