package ar.com.konomo.display;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Display {
    private TitleScreen titleScreen;
    private HomeScreen homeScreen;
    private Background background;
    private CreationScreen creationScreen;
    private PlayerCreationDialogue playerCreation;
    private NinjaPlacing ninjaPlacing;
    private Scanner scanner;
    private BoardMaker boardMaker;

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


    public List<Coordinate> playerSettings(Player player) {
        playerCreation.getPlayerName(player);

        return playerCreation.getPlayerCoordinates();
    }

    public List<Coordinate> ammendCoordinates(List<Coordinate> coordinates, OpError errors){
        showErrors(errors);
        return ninjaPlacing.getCoordenates("Intentémoslo de nuevo", coordinates);
    }


    public Display(){
        scanner = new Scanner(System.in);
        titleScreen = new TitleScreen();
        homeScreen = new HomeScreen();
        background = new Background(5, 5);
        creationScreen = new CreationScreen();
        ninjaPlacing = new NinjaPlacing();
        playerCreation = new PlayerCreationDialogue(ninjaPlacing, scanner);
        boardMaker = new BoardMaker();
    }

    public void retrieveBoard(Player player){
        boardMaker.update(player.getLocalBoard());
        boardMaker.print();
    }

    private void showErrors(OpError errors){
        for (String error: errors.getErrors()
             ) {
            System.out.println(error);
        }
    }

}
