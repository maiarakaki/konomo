package ar.com.konomo.operators;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Placeable;
import ar.com.konomo.entity.Shinobi;

import java.util.List;

public abstract class NinjaPlacer {
    public static void place(List<Shinobi> placeables, Board board){
        try {
            for (Placeable item: placeables
            ) {
                board.place(item.getRowIndex(), item.getColumnIndex(), item);
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage() + " place ninjas en el board falló");
        }
    }
}
