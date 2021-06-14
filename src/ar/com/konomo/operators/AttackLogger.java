package ar.com.konomo.operators;

import ar.com.konomo.entity.Intention;

import java.util.HashMap;
import java.util.Map;

public class AttackLogger {
    private int attackIndex;
    private Map <Integer, Intention> attackLog;

    public AttackLogger() {
        attackLog = new HashMap<>();
    }

    public Map<Integer, Intention> logAttacks(Intention intention){
        if(attackLog == null) {
            attackLog = new HashMap<>();
        } else {
            if (attackLog.isEmpty()) {
                attackIndex = 0;
            }
        }

        attackLog.put(attackIndex, intention);
        attackIndex ++;
        return attackLog;
    }

    public Map<Integer, Intention> getAttackLog() {
        return attackLog;
    }
}
