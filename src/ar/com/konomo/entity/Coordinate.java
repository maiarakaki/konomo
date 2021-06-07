package ar.com.konomo.entity;

public class Coordinate {
    private int y;
    private String x;

    public Coordinate(int y, String x){
        this.x = x;
        this.y = y;
    }

    public Coordinate(){}

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
}
