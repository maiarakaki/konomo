package ar.com.konomo.enums;

public enum NinjaType {
    CHUUNIN ("n", 10),
    JOUNIN("N", 20),
    OBSTACLE("-", 0),
    FIAMBRENIN ("x", 0);

    private String value;
    private int baseStamina;

    NinjaType (String value, int baseStamina) {
        this.value = value;
        this.baseStamina = baseStamina;
    }

    public String getValue(){
        return value;
    }

    public int getBaseStamina(){
        return baseStamina;
    }
}
