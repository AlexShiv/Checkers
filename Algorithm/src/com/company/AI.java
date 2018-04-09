package com.company;

import java.awt.*;
import java.util.ArrayList;

public class AI {

    private int enemy;
    private int player;
    private ArrayList<Point> listMoves;
    private Mover mover;

    public AI(int player) {
        this.player = player;
        if (player == 1) enemy = 2;
        else if (player == 2) enemy = 1;
    }

    public void scanBoard() {
        int[][] a = GameBoard.board; // доска
        ArrayList<Point> canChess = new ArrayList<>(); // список всех точек которыми может распоряжаться ИИ в данный момент времени
        ArrayList<Point> canFightChess = mover.canFight(a); // список всех обычных шашек которые могут бить
        ArrayList<Point> canFightCatChess = new ArrayList<>(); // список всех дамок которые могут бить в данный момент времени

        // Ищем все дамки которые могут бить кого либо
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0 && Math.abs(a[i][j]) == player && mover.canFightCat(a, i, j, enemy).size() != 0) {
                    canFightCatChess.add(new Point(i, j));
                }
            }
        }

        if (canFightCatChess.size() != 0 || canFightChess.size() != 0) {
            /*
             * если хотя бы одна шашка или дамка может бить, то выбираем битье при котором умрет наибольшее число вражеских фигур
             * если при убийстве можем бить дамку, то убийство дамки преоритетнее убийства нескольких простых шашек
             * при убийстве считать до тех пор пока можем бить
             * */
            canChess.addAll(canFightCatChess);
            canChess.addAll(canFightChess);
        } else {
            // если ни кто не может бить, то ищем фигуры которые могут пойти

        }
    }
}
