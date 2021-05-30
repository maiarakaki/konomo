package ar.com.konomo.display;

public class DoubleBorders extends Borders{
    private final String[] doubleBorders= {" ╔"," ╚","╗","╝"," ╠","╣","╦","╩","╬","║","═"};

    @Override
    public String getBorders(int borderId) {
        return doubleBorders[borderId];
    }
}
