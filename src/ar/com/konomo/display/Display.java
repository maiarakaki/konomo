package ar.com.konomo.display;

public class Display {
    private final TitleScreen titleScreen = new TitleScreen();
    private final HomeScreen homeScreen = new HomeScreen();
    private final Background background = new Background(5, 5);
    private final CreationScreen creationScreen = new CreationScreen();
    private final NinjaPlacing ninjaPlacing = new NinjaPlacing();

    public void titleScreen(){
        titleScreen.jumpStart();
    }

    public String showOptions () {
        return homeScreen.showOptions().toUpperCase();
    }

    public String serverCreation(String title){
        return creationScreen.getIpAndPort(title);
    }

    public void newScreen(String title){
        background.setTitle(title);
        background.showBackground();
    }

    public void placeNinjas(String title){
        ninjaPlacing.getCoordenates(title);
    }



}
