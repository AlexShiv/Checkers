package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Mover implements MouseListener {

    public int step = 2;//Изменить на 2!!!
    private int click = 1;
    private int fromX, fromY, toX, toY;
    private AI white = new AI(1, this);
    private AI black = new AI(2, this);
    public static int mode;

    public void move(int[][] a, int x1, int y1, int x2, int y2) {
        //Проверка на битье дамки
        int kill = 0;
        if (step == 1) {
            kill = 2;
        } else if (step == 2) {
            kill = 1;
        }
        ArrayList<Point> points;
        ArrayList<Point> points1 = canFight(a);
        boolean flag = false;
        if (a[x1][y1] < 0) {
            points = canFightCat(a, x1, y1, kill);
            if (points1.size() == 0 && points.size() == 0)
                flag = true;
        } else {
            if (points1.size() == 0)
                flag = true;
        }
        if (flag) {
            if (canMove(a, x1, y1, x2, y2)) {
                a[x2][y2] = a[x1][y1];
                a[x1][y1] = 0;
                changePlayer();
                GameJFrame.setTitle(step);
                if (a[x2][y2] == 1 && x2 == 7) {
                    a[x2][y2] = -1;
                } else if (a[x2][y2] == 2 && x2 == 0) {
                    a[x2][y2] = -2;
                }
            } else JOptionPane.showMessageDialog(null, "Ход невозможен!");
        } else JOptionPane.showMessageDialog(null, "Вы обязаны бить!");
    }

    public boolean canMove(int[][] a, int x1, int y1, int x2, int y2) {
        if (Math.abs(a[x1][y1]) == step) {
            if (a[x2][y2] == 0) {
                // проверка хода обычной шашки
                if ((step == 1) && (Math.abs(y1 - y2) == 1) && (x1 - x2 == -1)) {
                    return true;
                }
                if ((step == 2) && (Math.abs(y1 - y2) == 1) && (x1 - x2 == 1)) {
                    return true;
                }
                //проверка хода дамки
                if (a[x1][y1] < 0 && Math.abs(a[x1][y1]) == step) {
                    int i;
                    for (i = 1; x1 - i != -1 && y1 - i != -1; i++) {
                        if (x1 - i != x2 && y1 - i != y2 && a[x1 - i][y1 - i] != 0)
                            break;
                        else if (x1 - i == x2 && y1 - i == y2) {
                            return true;
                        }
                    }
                    for (i = 1; x1 + i != 8 && y1 + i != 8; i++) {
                        if (x1 + i != x2 && y1 + i != y2 && a[x1 + i][y1 + i] != 0)
                            break;
                        if (x1 + i == x2 && y1 + i == y2) {
                            return true;
                        }
                    }
                    for (i = 1; x1 - i != -1 && y1 + i != 8; i++) {
                        if (x1 - i != x2 && y1 + i != y2 && a[x1 - i][y1 + i] != 0)
                            break;
                        if (x1 - i == x2 && y1 + i == y2) {
                            return true;
                        }
                    }
                    for (i = 1; x1 + i != 8 && y1 - i != -1; i++) {
                        if (x1 + i != x2 && y1 - i != y2 && a[x1 + i][y1 - i] != 0)
                            break;
                        if (x1 + i == x2 && y1 - i == y2) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void fightCat(int[][] a, int x1, int y1, int x2, int y2) {
        int kill = 0;
        if (step == 1) {
            kill = 2;
        } else if (step == 2) {
            kill = 1;
        }

        Point from = new Point(x1, y1);
        ArrayList<Point> points = canFightCat(a, from.x, from.y, kill);
        if (points.size() != 0 && points.contains(new Point(x2, y2))) {
            a[x2][y2] = a[x1][y1];
            a[x1][y1] = 0;
            int i, j;
            if (x1 > x2 && y1 > y2) {
                for (i = 1, j = 1; x1 - i != -1 && y1 - j != -1; i++, j++) {
                    if (Math.abs(a[x1 - i][y1 - i]) == kill) {
                        a[x1 - i][y1 - i] = 0;
                        break;
                    }
                }
            } else if (x1 > x2 && y1 < y2) {
                for (i = 1, j = 1; x1 - i != -1 && y1 + j != 8; i++, j++) {
                    if (Math.abs(a[x1 - i][y1 + i]) == kill) {
                        a[x1 - i][y1 + i] = 0;
                        break;
                    }
                }
            } else if (x1 < x2 && y1 > y2) {
                for (i = 1, j = 1; x1 + i != 8 && y1 - j != -1; i++, j++) {
                    if (Math.abs(a[x1 + i][y1 - i]) == kill) {
                        a[x1 + i][y1 - i] = 0;
                        break;
                    }
                }
            } else if (x1 < x2 && y1 < y2) {
                for (i = 1, j = 1; x1 + i != 8 && y1 + j != 8; i++, j++) {
                    if (Math.abs(a[x1 + i][y1 + i]) == kill) {
                        a[x1 + i][y1 + i] = 0;
                        break;
                    }
                }
            }
        }
        if (Math.abs(a[fromX][fromY]) == step) {
            JOptionPane.showMessageDialog(null, "Вы обязаны бить!");
        } else {
            if (canFightCat(a, x2, y2, kill).size() == 0) {
                changePlayer();
                GameJFrame.setTitle(step);
            }
        }
    }

    public ArrayList<Point> canFightCat(int[][] a, int x1, int y1, int kill) {
        ArrayList<Point> points = new ArrayList<>();
        int i, flag = 0;
        try {
            for (i = 1; x1 - i != -1 && y1 - i != -1; i++) {
                if (flag == 0 && Math.abs(a[x1 - i][y1 - i]) == kill && a[x1 - i - 1][y1 - i - 1] == 0) {
                    points.add(new Point(x1 - i - 1, y1 - i - 1));
                    flag = 1;
                }
                if (Math.abs(a[x1 - i][y1 - i]) == kill && a[x1 - i - 1][y1 - i - 1] != 0)
                    break;
                if (flag == 1 && a[x1 - i - 1][y1 - i - 1] == 0) {
                    points.add(new Point(x1 - i - 1, y1 - i - 1));
                } else if (flag == 1 && a[x1 - i - 1][y1 - i - 1] != 0) break;
            }
        } catch (Throwable ex) {
            System.out.println("1");
        }
        flag = 0;
        try {
            for (i = 1; x1 - i != -1 && y1 + i != 8; i++) {
                if (flag == 0 && Math.abs(a[x1 - i][y1 + i]) == kill && a[x1 - i - 1][y1 + i + 1] == 0) {
                    points.add(new Point(x1 - i - 1, y1 + i + 1));
                    flag = 1;
                }
                if (Math.abs(a[x1 - i][y1 + i]) == kill && a[x1 - i - 1][y1 + i + 1] != 0)
                    break;
                if (flag == 1 && a[x1 - i - 1][y1 + i + 1] == 0) {
                    points.add(new Point(x1 - i - 1, y1 + i + 1));
                } else if (flag == 1 && a[x1 - i - 1][y1 + i + 1] != 0) break;
            }
        } catch (Throwable ex) {
            System.out.println("2");
        }
        flag = 0;
        try {
            for (i = 1; x1 + i != 8 && y1 - i != -1; i++) {
                if (flag == 0 && Math.abs(a[x1 + i][y1 - i]) == kill && a[x1 + i + 1][y1 - i - 1] == 0) {
                    points.add(new Point(x1 + i + 1, y1 - i - 1));
                    flag = 1;
                }
                if (Math.abs(a[x1 + i][y1 - i]) == kill && a[x1 + i + 1][y1 - i - 1] != 0)
                    break;
                if (flag == 1 && a[x1 + i + 1][y1 - i - 1] == 0) {
                    points.add(new Point(x1 + i + 1, y1 - i - 1));
                } else if (flag == 1 && a[x1 + i + 1][y1 - i - 1] != 0) break;
            }
        } catch (Throwable ex) {
            System.out.println("3");
        }
        flag = 0;
        try {
            for (i = 1; x1 + i != 8 && y1 + i != -1; i++) {
                if (flag == 0 && Math.abs(a[x1 + i][y1 + i]) == kill && a[x1 + i + 1][y1 + i + 1] == 0) {
                    points.add(new Point(x1 + i + 1, y1 + i + 1));
                    flag = 1;
                }
                if (Math.abs(a[x1 + i][y1 + i]) == kill && a[x1 + i + 1][y1 + i + 1] != 0)
                    break;
                if (flag == 1 && a[x1 + i + 1][y1 + i + 1] == 0) {
                    points.add(new Point(x1 + i + 1, y1 + i + 1));
                } else if (flag == 1 && a[x1 + i + 1][y1 + i + 1] != 0) break;
            }
        } catch (Throwable ex) {
            System.out.println("4");
        }
        return points;
    }

    public void fight(int[][] a, int x1, int y1, int x2, int y2) {
        int kill = 0;
        if (step == 1) {
            kill = 2;
        } else if (step == 2) {
            kill = 1;
        }
        Point from = new Point(x1, y1);
        if (canFight(a).contains(from)) {
            Point to = new Point(x2, y2);
            Point[] neighbor = sightFight(a, from.x, from.y, kill);
            if (neighbor[0] != null && neighbor[0].equals(to)) {
                a[to.x][to.y] = a[from.x][from.y];
                a[from.x - 1][from.y - 1] = 0;
                a[from.x][from.y] = 0;
            } else if (neighbor[1] != null && neighbor[1].equals(to)) {
                a[to.x][to.y] = a[from.x][from.y];
                a[from.x - 1][from.y + 1] = 0;
                a[from.x][from.y] = 0;
            } else if (neighbor[2] != null && neighbor[2].equals(to)) {
                a[to.x][to.y] = a[from.x][from.y];
                a[from.x + 1][from.y - 1] = 0;
                a[from.x][from.y] = 0;
            } else if (neighbor[3] != null && neighbor[3].equals(to)) {
                a[to.x][to.y] = a[from.x][from.y];
                a[from.x + 1][from.y + 1] = 0;
                a[from.x][from.y] = 0;
            }

            if (a[x2][y2] == 1 && x2 == 7) {
                a[x2][y2] = -1;
            } else if (a[x2][y2] == 2 && x2 == 0) {
                a[x2][y2] = -2;
            }

            if (a[from.x][from.y] == step) {
                JOptionPane.showMessageDialog(null, "Вы обязаны бить!");
            } else if (!canFight(a).contains(to)) {//если добили, меняем шаг
                if ((a[x2][y2] > 0) || (canFightCat(a, to.x, to.y, kill).size() == 0)) {
                    changePlayer();
                    GameJFrame.setTitle(step);
                }
            }
        }
    }

    public void changePlayer(){
        if (step == 1) {
            step = 2;
        } else if (step == 2) {
            step = 1;
        }
    }

    /*
    1 2
     x
    3 4
     */
    public Point[] sightFight(int[][] a, int x, int y, int kill) {
        Point[] arr = new Point[4];
        try {
            if (a[x][y] == step && Math.abs(a[x - 1][y - 1]) == kill && a[x - 2][y - 2] == 0) {
                arr[0] = new Point(x - 2, y - 2);
            }
        } catch (Throwable ex) {
            arr[0] = null;
        }
        try {
            if (a[x][y] == step && Math.abs(a[x - 1][y + 1]) == kill && a[x - 2][y + 2] == 0) {
                arr[1] = new Point(x - 2, y + 2);
            }
        } catch (Throwable ex) {
            arr[1] = null;
        }
        try {
            if (a[x][y] == step && Math.abs(a[x + 1][y - 1]) == kill && a[x + 2][y - 2] == 0) {
                arr[2] = new Point(x + 2, y - 2);
            }
        } catch (Throwable ex) {
            arr[2] = null;
        }
        try {
            if (a[x][y] == step && Math.abs(a[x + 1][y + 1]) == kill && a[x + 2][y + 2] == 0) {
                arr[3] = new Point(x + 2, y + 2);
            }
        } catch (Throwable ex) {
            arr[3] = null;
        }
        return arr;
    }

    //использовать исключения как в методе выше!?
    public ArrayList<Point> canFight(int[][] a) {
        ArrayList<Point> points = new ArrayList<>();
        int kill = 0;
        if (step == 1) {
            kill = 2;
        } else if (step == 2) {
            kill = 1;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    if (a[i][j] == step && Math.abs(a[i - 1][j - 1]) == kill && a[i - 2][j - 2] == 0) {
                        points.add(new Point(i, j));
                    }
                } catch (Throwable ex) {
                    System.out.println("1.0");
                }
                try {
                    if (a[i][j] == step && Math.abs(a[i - 1][j + 1]) == kill && a[i - 2][j + 2] == 0) {
                        points.add(new Point(i, j));
                    }
                } catch (Throwable ex) {
                    System.out.println("2.0");
                }
                try {
                    if (a[i][j] == step && Math.abs(a[i + 1][j - 1]) == kill && a[i + 2][j - 2] == 0) {
                        points.add(new Point(i, j));
                    }
                } catch (Throwable ex) {
                    System.out.println("3.0");
                }
                try {
                    if (a[i][j] == step && Math.abs(a[i + 1][j + 1]) == kill && a[i + 2][j + 2] == 0) {
                        points.add(new Point(i, j));
                    }
                } catch (Throwable ex) {
                    System.out.println("4.0");
                }
            }
        }
        return points;
    }

    //потеря всех фигур или невозможность хода!
    public boolean win(int[][] a, int kill) {
        //Может бить
        if (canFight(a).size() != 0) {
            return false;
        }
        //Невозможность хода
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (kill == 1) {
                    //Если обычная шашка
                    if (a[i][j] > 0) {
                        //...не может ходить
                        try {
                            if (a[i][j] == kill && a[i + 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (a[i][j] == kill && a[i + 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        //Если дамка
                    } else if (a[i][j] < 0) {
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i + 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i + 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i - 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i - 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        if (canFightCat(a, i, j, kill).size() != 0) return false;
                    }
                } else if (step == 2) {
                    if (a[i][j] > 0) {
                        try {
                            if (a[i][j] == kill && a[i - 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (a[i][j] == kill && a[i - 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                    } else if (a[i][j] < 0) {
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i + 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i + 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i - 1][j - 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        try {
                            if (Math.abs(a[i][j]) == kill && a[i - 1][j + 1] == 0) return false;
                        } catch (Throwable ex) {
                            System.out.println(".");
                        }
                        if (canFightCat(a, i, j, kill).size() != 0) return false;
                    }
                }
            }
        }

        return true;

    }

    public boolean nomore(int[][] a, int kill) {
        //?
        for (int[] i : a) {
            for (int j : i) {
                if (Math.abs(j) == kill) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    //клик
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    //нажал
    public void mousePressed(MouseEvent e) {
        if (mode == 1 || mode == 2) {
            if (click == 1) {
                fromY = e.getPoint().x / 100;
                fromX = e.getPoint().y / 100;
                click = 2;
            } else if (click == 2) {
                toY = e.getPoint().x / 100;
                toX = e.getPoint().y / 100;
                boolean other = true;
                int kill = 0;
                if (step == 1) {
                    kill = 2;
                } else if (step == 2) {
                    kill = 1;
                }
                for (int i = 0; i < GameBoard.board.length; i++) {
                    for (int j = 0; j < GameBoard.board.length; j++) {
                        if ((GameBoard.board[i][j] < 0) && (Math.abs(GameBoard.board[i][j]) == step) && (canFightCat(GameBoard.board, i, j, kill).size() != 0)) {
                            other = false;
                            break;
                        }
                    }
                    if (!other) break;
                }
                if ((GameBoard.board[fromX][fromY] > 0) && (Math.abs(fromX - toX) == 2) && (Math.abs(fromY - toY) == 2)) {
                    fight(GameBoard.board, fromX, fromY, toX, toY);
                    System.out.println();
                    GameBoard.print();
                } else {
                    ArrayList<Point> fightArray = canFightCat(GameBoard.board, fromX, fromY, kill);
                    if (GameBoard.board[fromX][fromY] < 0 && fightArray.size() != 0 && fightArray.contains(new Point(toX, toY))) {
                        fightCat(GameBoard.board, fromX, fromY, toX, toY);
                    } else if (other)
                        move(GameBoard.board, fromX, fromY, toX, toY);
                    else JOptionPane.showMessageDialog(null, "Вы обязанны бить");
                }
                GameJFrame.rebuildFrame(GameBoard.board);
                click = 1;

                // Проверка победы
                if (!nomore(GameBoard.board, step)) {
                    if (win(GameBoard.board, step)) {
                        String player;
                        if (step == 2) player = "белых";
                        else player = "черных";
                        JOptionPane.showMessageDialog(null, "Победа " + player);
                        System.exit(0);
                    }
                } else {
                    String player;
                    if (step == 2) player = "белых";
                    else player = "черных";
                    JOptionPane.showMessageDialog(null, "Победа " + player);
                    System.exit(0);
                }
                int[][] arr = GameBoard.board;
                if (mode == 2 && step == white.getPlayer()) {
                    white.analizeBord();
                    changePlayer();
                }
                if (step != 2) {
                    changePlayer();
                }

                GameJFrame.rebuildFrame(GameBoard.board);
            }
        } else {
            if (step == black.getPlayer()) {
                black.analizeBord();
                changePlayer();
            }
            if (step != 1) {
                changePlayer();
            }
            GameJFrame.rebuildFrame(GameBoard.board);

            if (!nomore(GameBoard.board, step)) {
                if (win(GameBoard.board, step)) {
                    String player;
                    if (step == 2) player = "белых";
                    else player = "черных";
                    JOptionPane.showMessageDialog(null, "Победа " + player);
                    System.exit(0);
                }
            } else {
                String player;
                if (step == 2) player = "белых";
                else player = "черных";
                JOptionPane.showMessageDialog(null, "Победа " + player);
                System.exit(0);
            }
            if (step == white.getPlayer()) {
                white.analizeBord();
                changePlayer();
            }
            if (step != 2) {
                changePlayer();
            }
            GameJFrame.rebuildFrame(GameBoard.board);
            if (!nomore(GameBoard.board, step)) {
                if (win(GameBoard.board, step)) {
                    String player;
                    if (step == 2) player = "белых";
                    else player = "черных";
                    JOptionPane.showMessageDialog(null, "Победа " + player);
                    System.exit(0);
                }
            } else {
                String player;
                if (step == 2) player = "белых";
                else player = "черных";
                JOptionPane.showMessageDialog(null, "Победа " + player);
                System.exit(0);
            }
        }
    }

    @Override
    //отжал
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    //навел
    public void mouseEntered(MouseEvent e) {
        //показ доступных ходов?!
    }

    @Override
    //отвел
    public void mouseExited(MouseEvent e) {
        //показ доступных ходов?!
    }
}
