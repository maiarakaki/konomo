package ar.com.konomo.display;

import ar.com.konomo.entity.*;
import ar.com.konomo.operators.CoordinateBuilder;
import ar.com.konomo.validators.CoordinateFormalValidator;

import java.util.*;

public class Display {
    private TitleScreen titleScreen;
    private HomeScreen homeScreen;
    private Background background;
    private CreationScreen creationScreen;
    private PlayerCreationDialogue playerCreation;
    private NinjaPlacing ninjaPlacing;
    private Scanner scanner;
    private BoardMaker boardMaker;
    private CoordinateFormalValidator coordinateFormalValidator;
    private CoordinateBuilder coordBuilder;
    private Gameplay gameplay;


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
        coordinateFormalValidator = new CoordinateFormalValidator();
        coordBuilder = new CoordinateBuilder();
        gameplay = new Gameplay(scanner, coordinateFormalValidator, coordBuilder);
    }

    public Map<Integer, Intention> gamePlay (Player player){
        return gameplay.getPlayerIntentions(player);
    }

    public Map<Integer, Intention> ammendIntentions(Map<Integer, Intention> intentions, OpError errors, Player player) {
        showErrors(errors);
        retrieveBoard(player);
        return gameplay.getPlayerIntentions("Intentémoslo de nuevo", intentions, player.getMyNinjas());
    }

    public void retrieveBoard(Player player){
        ScreenBoard myBoard = boardMaker.create(new DoubleBorders());
        ScreenBoard enemyBoard = boardMaker.create(new SimpleBorders());
        myBoard.update(player.getLocalBoard());
        enemyBoard.update(player.getEnemyBoard());

        background.setBackground(new String[10 + myBoard.screenBoard.length][myBoard.screenBoard.length + enemyBoard.screenBoard.length +4]);
        try {
            background.fillBackground("Juguemos " + player.getName(), myBoard, enemyBoard, 4, 10);
            background.showBackground();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void showErrors(OpError errors){
        for (String error: errors.getErrors()
             ) {
            System.out.println(error);
        }
    }






}
