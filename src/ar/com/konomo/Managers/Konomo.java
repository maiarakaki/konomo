package ar.com.konomo.Managers;

import ar.com.konomo.display.BoardsView;
import ar.com.konomo.display.TitleScreen;

public class Konomo implements Runnable{
    BoardsView titleScreen = new BoardsView();
    //PUEDO CREAR UN TTITLES SCREEN Y UBICARLO EN LA SCREEN
    //CALCULAR EL TAMANIO DE LA PANTALLA TOTAL INCLUYENDO BOARDS Y MENSAJES PARA MOSTRAR
    public void run() {
        titleScreen.jumpStart();
    }
}
