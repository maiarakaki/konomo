package ar.com.konomo.display;

import ar.com.konomo.entity.Board;

public class ScreenBoard {
    static int BORDER_PADDING = 2;
    static int OFFSET = 2;
    static int BOARD_SIZE = 5;
    String[][] screenBoard;

    public ScreenBoard() {
        screenBoard = new String[BOARD_SIZE * BORDER_PADDING + OFFSET][BOARD_SIZE * BORDER_PADDING + OFFSET];
    }

    public String[][] getScreenBoard() {
        return screenBoard;
    }

    public void setScreenBoard(String[][] screenBoard) {
        this.screenBoard = screenBoard;
    }

    public void print() {
        for (int i = 0; i < screenBoard.length; i++) {
            for (int j = 0; j < screenBoard[i].length; j++) {
                System.out.print(String.format(" %s ", screenBoard[i][j]));
            }
            System.out.print("\n");
        }
    }

    public void update(Board board) {
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard().length; j++) {

                if (board.getBoard()[i][j] != null) {

                    switch (board.getBoard()[i][j].getType()) {
                        case CHUUNIN -> screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = "n";
                        case JOUNIN -> screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = "N";
                        case OBSTACLE -> screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = "-";
                        case FIAMBRENIN -> screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = "x";
                    }

                } else {
                    screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = " ";
                }
            }
        }
    }

    public void update(String[][] knownBoard) {
        for (int i = 0; i < knownBoard.length; i++) {
            for (int j = 0; j < knownBoard.length; j++) {
                if (knownBoard[i][j] != null) {
                    screenBoard[i * OFFSET + BORDER_PADDING][j * OFFSET + BORDER_PADDING] = knownBoard[i][j];
                }
            }
        }
    }
}