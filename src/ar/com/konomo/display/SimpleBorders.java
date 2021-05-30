package ar.com.konomo.display;

public class SimpleBorders extends Borders{
    private final String[] simpleBorders= {" ┌"," └","┐","┘"," ├","┤","┬","┴","┼","│","─"};

    @Override
    public String getBorders(int borderID) {
        return simpleBorders[borderID];
    }
}
