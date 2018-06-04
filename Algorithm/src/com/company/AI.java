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
    private int count;

    public AI(int player) {
        this.player = player;
        if (player == 1) enemy = 2;
        else if (player == 2) enemy = 1;
        mover = new Mover();
        listMoves = new ArrayList<>();
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

    public ArrayList<Point> canMoveChess() {
        int[][] a = GameBoard.board;
        ArrayList<Point> canMove = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (Math.abs(a[i][j]) == player && player == 1) {
                    if (mover.canMove(a, i, j, i + 1, j + 1) || mover.canMove(a, i, j, i + 1, j - 1)) {
                        canMove.add(new Point(i, j));
                    }
                } else if (Math.abs(a[i][j]) == player && player == 2) {
                    if (mover.canMove(a, i, j, i - 1, j - 1) || mover.canMove(a, i, j, i - 1, j + 1)) {
                        canMove.add(new Point(i, j));
                    }
                }
            }
        }
        return canMove;
    }

    public int[][] copyBoard(int[][] cop) {
        int[][] boardCopy = new int[cop.length][cop.length];
        for (int i = 0; i < cop.length; i++) {
            boardCopy[i] = cop[i].clone();
        }
        return boardCopy;
    }

    public void analizeBord() {
        ArrayList<Point> fight = canFightChees();
        if (fight.size() != 0) {
            // если есть кому бить, анализируем те фигуры
            int x = -1;
            int y = -1;
            int count = 0;

        } else {
            // ... иначе ходим
        }
    }

    public Node buildTree(int x, int y) {
        Node root = new Node(new Point(x, y), 0);
        ArrayList<Point> pos = new ArrayList<>();
        if (GameBoard.board[x][y] < 0) {
            pos = mover.canFightCat(GameBoard.board, x, y, enemy);
        } else if (GameBoard.board[x][y] > 0) {
            Point[] arr = mover.sightFight(GameBoard.board, x, y, enemy);
            for (Point point : arr) {
                if (point != null) {
                    pos.add(point);
                }
            }
        }
        for (Point point : pos) {
            int a = point.x;
            int b = point.y;

            // danger!!! index out of range
            if (point.x - x < 0 && point.y - y < 0) {
                while (GameBoard.board[a - 1][b - 1] != Math.abs(enemy)) {
                    a--;
                    b--;
                }
                if (GameBoard.board[a - 1][b - 1] > 0) {
                    root.addPosition(new Node(new Point(a - 1, b - 1), 1));
                } else if (GameBoard.board[a - 1][b - 1] < 0) {
                    root.addPosition(new Node(new Point(a - 1, b - 1), 3));
                }
            } else if (point.x - x > 0 && point.y - y < 0) {
                while (GameBoard.board[a + 1][b - 1] != Math.abs(enemy)) {
                    a++;
                    b--;
                }
                if (GameBoard.board[a + 1][b - 1] > 0) {
                    root.addPosition(new Node(new Point(a + 1, b - 1), 1));
                }else if (GameBoard.board[a + 1][b - 1] < 0) {
                    root.addPosition(new Node(new Point(a + 1, b - 1), 3));
                }
            } else if (point.x - x < 0 && point.y - y > 0) {
                while (GameBoard.board[a - 1][b + 1] != Math.abs(enemy)) {
                    a--;
                    b++;
                }
                if (GameBoard.board[a - 1][b + 1] > 0) {
                    root.addPosition(new Node(new Point(a - 1, b + 1), 1));
                }else if (GameBoard.board[a - 1][b + 1] < 0) {
                    root.addPosition(new Node(new Point(a - 1, b + 1), 3));
                }
            } else if (point.x - x > 0 && point.y - y > 0) {
                while (GameBoard.board[a + 1][b + 1] != Math.abs(enemy)) {
                    a++;
                    b++;
                }
                if (GameBoard.board[a + 1][b + 1] > 0) {
                    root.addPosition(new Node(new Point(a + 1, b + 1), 1));
                }else if (GameBoard.board[a - 1][b + 1] < 0) {
                    root.addPosition(new Node(new Point(a + 1, b + 1), 3));
                }
            }
        }
        for (Node node : root.position) {
            int[][] arr = copyBoard(GameBoard.board);
            if (arr[x][y] > 0) {
                mover.fight(arr, x, y, node.coord.x, node.coord.y);
            } else if(arr[x][y] < 0){
                mover.fightCat(arr, x, y, node.coord.x, node.coord.y);
            }
            node.position = buildTree(node, arr);
        }
        return root;
    }

    public ArrayList<Node> buildTree(Node node, int[][] copyBoard) {
        ArrayList<Point> pos = new ArrayList<>();
        ArrayList<Node> listNode = new ArrayList<>();
        if (copyBoard[node.coord.x][node.coord.y] < 0) {
            pos = mover.canFightCat(copyBoard, node.coord.x, node.coord.y, enemy);
        } else if (copyBoard[node.coord.x][node.coord.y] > 0) {
            Point[] arr = mover.sightFight(copyBoard, node.coord.x, node.coord.y, enemy);
            for (Point point : arr) {
                if (point != null) {
                    pos.add(point);
                }
            }
        }
        if (pos.size() == 0) return null;
        else {
            for (Point point : pos) {
                int a = point.x;
                int b = point.y;

                // danger!!! index out of range
                if (point.x - node.coord.x < 0 && point.y - node.coord.y < 0) {
                    while (copyBoard[a - 1][b - 1] != Math.abs(enemy)) {
                        a--;
                        b--;
                    }
                    if (copyBoard[a - 1][b - 1] > 0) {
                        listNode.add(new Node(new Point(a - 1, b - 1), 1));
                    } else if (copyBoard[a - 1][b - 1] < 0) {
                        listNode.add(new Node(new Point(a - 1, b - 1), 3));
                    }
                } else if (point.x - node.coord.x > 0 && point.y - node.coord.y < 0) {
                    while (copyBoard[a + 1][b - 1] != Math.abs(enemy)) {
                        a++;
                        b--;
                    }
                    if (copyBoard[a + 1][b - 1] > 0) {
                        listNode.add(new Node(new Point(a + 1, b - 1), 1));
                    } else if (copyBoard[a + 1][b - 1] < 0) {
                        listNode.add(new Node(new Point(a + 1, b - 1), 3));
                    }
                } else if (point.x - node.coord.x < 0 && point.y - node.coord.y > 0) {
                    while (copyBoard[a - 1][b + 1] != Math.abs(enemy)) {
                        a--;
                        b++;
                    }
                    if (copyBoard[a - 1][b + 1] > 0) {
                        listNode.add(new Node(new Point(a - 1, b + 1), 1));
                    } else if (copyBoard[a - 1][b + 1] < 0) {
                        listNode.add(new Node(new Point(a - 1, b + 1), 3));
                    }
                } else if (point.x - node.coord.x > 0 && point.y - node.coord.y > 0) {
                    while (copyBoard[a + 1][b + 1] != Math.abs(enemy)) {
                        a++;
                        b++;
                    }
                    if (copyBoard[a + 1][b + 1] > 0) {
                        listNode.add(new Node(new Point(a + 1, b + 1), 1));
                    } else if (copyBoard[a - 1][b + 1] < 0) {
                        listNode.add(new Node(new Point(a + 1, b + 1), 3));
                    }
                }
            }
            for (Node node1 : listNode) {
                int[][] mas = copyBoard(copyBoard);
                node1.position = buildTree(node1, mas);
            }
            return listNode;
        }
        //
    }

}