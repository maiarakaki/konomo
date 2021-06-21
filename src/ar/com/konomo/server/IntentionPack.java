package ar.com.konomo.server;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Intention;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Shinobi;

import java.util.List;
import java.util.Map;

public class IntentionPack {
    private Map<Integer, Intention> intentions;
    private List<Shinobi> ninjaList;
    private OpError errors;
    public boolean allGood;

    public IntentionPack(Map<Integer, Intention> intentions, List<Shinobi> ninjaList, boolean allGood) {
        this.intentions = intentions;
        this.ninjaList = ninjaList;
        this.allGood = allGood;

    }

    public void setAllGood(boolean allGood) {
        this.allGood = allGood;
    }

    public Map<Integer, Intention> getIntentions() {
        return intentions;
    }

    public List<Shinobi> getNinjaList() {
        return ninjaList;
    }


    public OpError getErrors() {
        if (errors == null) {
            errors = new OpError();
        }
        return errors;
    }

    public void setIntentions(Map<Integer, Intention> intentions) {
        this.intentions = intentions;
    }

    public void setErrors(OpError errors) {
        this.errors = errors;
    }
}
