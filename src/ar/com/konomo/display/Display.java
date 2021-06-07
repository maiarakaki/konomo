package ar.com.konomo.display;

import ar.com.konomo.entity.Player;

import java.util.Scanner;

public class Display {
    private TitleScreen titleScreen;
    private HomeScreen homeScreen;
    private Background background;
    private CreationScreen creationScreen;
    private PlayerCreationDialogue playerCreation;
    private NinjaPlacing ninjaPlacing;
    private Scanner scanner;

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


    public String [] playerCreation(Player player) {
        playerCreation.getPlayerName(player);
        return playerCreation.getPlayerVariables(player);
    }

    public Display(){
        scanner = new Scanner(System.in);
        titleScreen = new TitleScreen();
        homeScreen = new HomeScreen();
        background = new Background(5, 5);
        creationScreen = new CreationScreen();
        ninjaPlacing = new NinjaPlacing();
        playerCreation = new PlayerCreationDialogue(ninjaPlacing, scanner);
    }

}
