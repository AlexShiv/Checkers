package com.company;

public class Main {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        /*final int WHITE = 1;
        final int BLACK = 2;
        final int BLOCKED = 3;
        final int WHITE_CAT = -1;
        final int BLACK_CAT = -2;*/

        GameJFrame.buildFrame();
        GameBoard g = new GameBoard();
        Mover m = new Mover();
        g.print();
    }
}
