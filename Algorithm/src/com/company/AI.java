package com.company;

import java.awt.*;
import java.util.ArrayList;

public class AI {
    /*
     * если хотя бы одна шашка или дамка может бить, то выбираем битье при котором умрет наибольшее число вражеских фигур
     * если при убийстве можем бить дамку, то убийство дамки преоритетнее убийства нескольких простых шашек
     * при убийстве считать до тех пор пока можем бить
     * */

    private int enemy;
    private int player;
    private ArrayList<Point> listMoves;
    private Mover mover;

    public AI(int player) {
        this.player = player;
        if (player == 1) enemy = 2;
        else if (player == 2) enemy = 1;
    }

    public ArrayList<Point> canFightChees() {
        int[][] a = GameBoard.board;
        ArrayList<Point> canChess = new ArrayList<>(mover.canFight(a)); // список всех точек которыми может распоряжаться ИИ в данный момент времени

        // Ищем все дамки которые могут бить кого либо
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0 && Math.abs(a[i][j]) == player && mover.canFightCat(a, i, j, enemy).size() != 0) {
                    canChess.add(new Point(i, j));
                }
            }
        }
        return canChess;
    }

    public boolean canFight(ArrayList<Point> fightChess) {
        return fightChess.size() != 0;
    }

    public ArrayList<Point> canMoveChess() {
        int[][] a = GameBoard.board;
        ArrayList<Point> canMove = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (Math.abs(a[i][j]) == player) {

                }
            }
        }
        return canMove;
    }

}
