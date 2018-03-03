package com.company;

public class Main {

    public static void main(String[] args) {
        run();
    }

    public static void run(){
        GameJFrame.buildFrame();
        GameBoard g = new GameBoard();
        Mover m = new Mover();
        g.print();
    }
}
