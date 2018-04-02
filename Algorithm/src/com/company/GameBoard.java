package com.company;

public class GameBoard {

    public static int[][] board = new int[8][8];
    /*public static int [][] board = {
            {3,0,3,0,3,-2,3,0},
            {0,3,0,3,0,3,0,3},
            {3,0,3,1,3,0,3,0},
            {0,3,0,3,0,3,0,3},
            {3,0,3,0,3,0,3,2},
            {0,3,0,3,-2,3,0,3},
            {3,0,3,0,3,0,3,0},
            {0,3,0,3,0,3,0,3},
    };*/

    public GameBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if ((i + j) % 2 != 1) {
                    board[i][j] = 3;
                }
                if (i < 3 && (i + j) % 2 == 1) {
                    board[i][j] = 1;
                }
                if (i > 4 && (i + j) % 2 == 1) {
                    board[i][j] = 2;
                }
            }
        }
    }

    public static void print() {
        for (int[] aBoard : board) {
            for (int anABoard : aBoard) {
                System.out.print(anABoard + "\t");
            }
            System.out.print("\n");
        }
    }
}