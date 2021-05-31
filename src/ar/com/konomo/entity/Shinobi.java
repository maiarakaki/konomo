package ar.com.konomo.entity;

import ar.com.konomo.enums.Action;

public class Shinobi extends Placeable{
    private boolean isAlive;
    private Action lastActionTaken;
    private int stamina;
    private boolean isJounin;

    @Override
    public void setxLocation(int xLocation) {
        super.setxLocation(xLocation);
    }

    @Override
    public void setyLocation(int yLocation) {
        super.setyLocation(yLocation);
    }

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

    public void setJounin(boolean jounin) {
        isJounin = jounin;
    }

    public boolean isJounin(){
        return isJounin;
    }
}
