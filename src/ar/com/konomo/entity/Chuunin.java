package ar.com.konomo.entity;

import ar.com.konomo.enums.Action;

public class Chuunin extends Placeable{
    private boolean isAlive;
    private Action lastActionTaken;
    private int stamina;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Action getLastActionTaken() {
        return lastActionTaken;
    }

    public void setLastActionTaken(Action lastActionTaken) {
        this.lastActionTaken = lastActionTaken;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
}
