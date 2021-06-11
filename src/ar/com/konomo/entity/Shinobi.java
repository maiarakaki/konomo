package ar.com.konomo.entity;

import ar.com.konomo.enums.Action;
import ar.com.konomo.enums.NinjaType;

public class Shinobi extends Placeable{
    private boolean isAlive;
    private Action lastActionTaken;
    private int stamina;
    private NinjaType ninjaType;

    @Override
    public void setColumnIndex(int columnIndex) {
        super.setColumnIndex(columnIndex);
    }

    @Override
    public void setRowIndex(int rowIndex) {
        super.setRowIndex(rowIndex);
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

    public NinjaType getNinjaType() {
        return ninjaType;
    }

    public void setNinjaType(NinjaType ninjaType) {
        this.ninjaType = ninjaType;
    }
}
