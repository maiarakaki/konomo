package ar.com.konomo.entity;

import ar.com.konomo.enums.NinjaType;
public class Obstacle extends Placeable{
    private NinjaType ninjaType;

    public Obstacle(){
        ninjaType = NinjaType.OBSTACLE;
    }
    @Override
    public NinjaType getType() {
        return ninjaType;
    }
}
