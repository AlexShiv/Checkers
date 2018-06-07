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
    private ArrayList<Point> comparelistMoves;
    private int len;
    private Mover mover;

    public AI(int player) {
        this.player = player;
        if (player == 1) enemy = 2;
        else if (player == 2) enemy = 1;
        mover = new Mover();
        listMoves = new ArrayList<>();
        comparelistMoves = new ArrayList<>();
        len = 0;
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
        Node root = new Node(new Point(x, y), 0, null);
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
            int a = x;
            int b = y;

            // danger!!! index out of range
            if (point.x - x < 0 && point.y - y < 0) {
                while (GameBoard.board[a - 1][b - 1] != Math.abs(enemy)) {
                    a--;
                    b--;
                }
                if (GameBoard.board[a - 1][b - 1] > 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 1, root));
                } else if (GameBoard.board[a - 1][b - 1] < 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 3, root));
                }
            } else if (point.x - x > 0 && point.y - y < 0) {
                while (GameBoard.board[a + 1][b - 1] != Math.abs(enemy)) {
                    a++;
                    b--;
                }
                if (GameBoard.board[a + 1][b - 1] > 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 1, root));
                } else if (GameBoard.board[a + 1][b - 1] < 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 3, root));
                }
            } else if (point.x - x < 0 && point.y - y > 0) {
                while (GameBoard.board[a - 1][b + 1] != Math.abs(enemy)) {
                    a--;
                    b++;
                }
                if (GameBoard.board[a - 1][b + 1] > 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 1, root));
                } else if (GameBoard.board[a - 1][b + 1] < 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 3, root));
                }
            } else if (point.x - x > 0 && point.y - y > 0) {
                while (GameBoard.board[a + 1][b + 1] != Math.abs(enemy)) {
                    a++;
                    b++;
                }
                if (GameBoard.board[a + 1][b + 1] > 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 1, root));
                } else if (GameBoard.board[a - 1][b + 1] < 0) {
                    root.addPosition(new Node(new Point(point.x, point.y), 3, root));
                }
            }
        }
        for (Node node : root.position) {
            int[][] arr = copyBoard(GameBoard.board);
            if (arr[x][y] > 0) {
                mover.fight(arr, x, y, node.coord.x, node.coord.y);
            } else if (arr[x][y] < 0) {
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
                        listNode.add(new Node(new Point(point.x, point.y), 1, node));
                    } else if (copyBoard[a - 1][b - 1] < 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 3, node));
                    }
                } else if (point.x - node.coord.x > 0 && point.y - node.coord.y < 0) {
                    while (copyBoard[a + 1][b - 1] != Math.abs(enemy)) {
                        a++;
                        b--;
                    }
                    if (copyBoard[a + 1][b - 1] > 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 1, node));
                    } else if (copyBoard[a + 1][b - 1] < 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 3, node));
                    }
                } else if (point.x - node.coord.x < 0 && point.y - node.coord.y > 0) {
                    while (copyBoard[a - 1][b + 1] != Math.abs(enemy)) {
                        a--;
                        b++;
                    }
                    if (copyBoard[a - 1][b + 1] > 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 1, node));
                    } else if (copyBoard[a - 1][b + 1] < 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 3, node));
                    }
                } else if (point.x - node.coord.x > 0 && point.y - node.coord.y > 0) {
                    while (copyBoard[a + 1][b + 1] != Math.abs(enemy)) {
                        a++;
                        b++;
                    }
                    if (copyBoard[a + 1][b + 1] > 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 1, node));
                    } else if (copyBoard[a - 1][b + 1] < 0) {
                        listNode.add(new Node(new Point(point.x, point.y), 3, node));
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

    public void calcValue(Node tree, Node root, int count) {
        count += tree.value;
        comparelistMoves.add(tree.coord);
        while (tree.position != null && tree.position.size() != 0) {
            calcValue(tree.position.get(0), root, count);
        }
        if (count > len) {
            len = count;
            listMoves = comparelistMoves;
            comparelistMoves = new ArrayList<>();
            tree.parent.position.remove(0);
        }
        if (root.position != null && root.position.size() != 0) {
            calcValue(root, root, 0);
        }
    }

    public void moveAI(int[][] board) {
        ArrayList<Point> posibleMove = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (Math.abs(board[i][j]) == player) {
                    if (player == 1) {
                        if (board[i][j] > 0) {
                            if (mover.canMove(GameBoard.board, i, j, i + 1, j + 1) || mover.canMove(GameBoard.board, i, j, i - 1, j + 1)) {
                                posibleMove.add(new Point(i, j));
                            }
                        } else if (board[i][j] < 0) {
                            try {
                                if (board[i + 1][j + 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai1");
                            }
                            try {
                                if (board[i - 1][j - 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai2");
                            }
                            try {
                                if (board[i + 1][j - 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai3");
                            }
                            try {
                                if (board[i - 1][j + 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai4");
                            }
                        }
                    } else if (player == 2) {
                        if (board[i][j] > 0) {
                            if (mover.canMove(GameBoard.board, i, j, i - 1, j - 1) || mover.canMove(GameBoard.board, i, j, i + 1, j - 1)) {
                                posibleMove.add(new Point(i, j));
                            }
                        } else if (board[i][j] < 0) {
                            try {
                                if (board[i + 1][j + 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai1");
                            }
                            try {
                                if (board[i - 1][j - 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai2");
                            }
                            try {
                                if (board[i + 1][j - 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai3");
                            }
                            try {
                                if (board[i - 1][j + 1] == 0) {
                                    posibleMove.add(new Point(i, j));
                                }
                            } catch (Throwable ex) {
                                System.out.println("ai4");
                            }
                        }
                    }
                }
            }
        }
        for (Point point : posibleMove) {
            int i = point.x;
            int j = point.y;
            if (player == 1) {
                if (board[i][j] > 0) {
                    try {
                        if (mover.canMove(GameBoard.board, i, j, i + 1, j + 1) && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j + 2] != enemy) {// || mover.canMove(GameBoard.board, i, j, i - 1, j + 1)
                            mover.move(GameBoard.board, i, j, i + 1, j + 1);
                            return;
                        }
                        if (mover.canMove(GameBoard.board, i, j, i - 1, j + 1) && GameBoard.board[i - 2][j] != enemy && GameBoard.board[i - 2][j + 2] != enemy && GameBoard.board[i][j + 2] != enemy) {// || mover.canMove(GameBoard.board, i, j, i - 1, j + 1)
                            mover.move(GameBoard.board, i, j, i - 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai");
                    }

                } else if (board[i][j] < 0) {
                    try {
                        if (board[i + 1][j + 1] == 0 && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j + 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i + 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai1");
                    }
                    try {
                        if (board[i - 1][j - 1] == 0 && GameBoard.board[i - 2][j - 2] != enemy && GameBoard.board[i][j - 2] != enemy && GameBoard.board[i - 2][j] != enemy) {
                            mover.move(GameBoard.board, i, j, i - 1, j - 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai2");
                    }
                    try {
                        if (board[i + 1][j - 1] == 0 && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j - 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i + 1, j - 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai3");
                    }
                    try {
                        if (board[i - 1][j + 1] == 0 && GameBoard.board[i - 2][j] != enemy && GameBoard.board[i - 2][j + 2] != enemy && GameBoard.board[i][j + 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i - 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai4");
                    }
                }
            } else if (player == 2) {
                if (board[i][j] > 0) {
                    try {
                        if (mover.canMove(GameBoard.board, i, j, i - 1, j - 1) && GameBoard.board[i - 2][j - 2] != enemy && GameBoard.board[i][j - 2] != enemy && GameBoard.board[i - 2][j] != enemy) {// || mover.canMove(GameBoard.board, i, j, i - 1, j + 1)
                            mover.move(GameBoard.board, i, j, i + 1, j + 1);
                            return;
                        }
                        if (mover.canMove(GameBoard.board, i, j, i + 1, j - 1) && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j - 2] != enemy) {// || mover.canMove(GameBoard.board, i, j, i - 1, j + 1)
                            mover.move(GameBoard.board, i, j, i - 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai");
                    }
                } else if (board[i][j] < 0) {
                    try {
                        if (board[i + 1][j + 1] == 0 && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j + 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i + 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai1");
                    }
                    try {
                        if (board[i - 1][j - 1] == 0 && GameBoard.board[i - 2][j - 2] != enemy && GameBoard.board[i][j - 2] != enemy && GameBoard.board[i - 2][j] != enemy) {
                            mover.move(GameBoard.board, i, j, i - 1, j - 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai2");
                    }
                    try {
                        if (board[i + 1][j - 1] == 0 && GameBoard.board[i + 2][j - 2] != enemy && GameBoard.board[i + 2][j] != enemy && GameBoard.board[i][j - 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i + 1, j - 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai3");
                    }
                    try {
                        if (board[i - 1][j + 1] == 0 && GameBoard.board[i - 2][j] != enemy && GameBoard.board[i - 2][j + 2] != enemy && GameBoard.board[i][j + 2] != enemy) {
                            mover.move(GameBoard.board, i, j, i - 1, j + 1);
                            return;
                        }
                    } catch (Throwable ex) {
                        System.out.println("ai4");
                    }
                }
            }
        }
        int a = (int) (Math.random() * posibleMove.size() - 1);
        Point p = posibleMove.get(a);
        if (GameBoard.board[p.x][p.y] < 0) {
            int b = (int) (Math.random() * 5);
            if (GameBoard.board[p.x - 1][p.y - 1] == 0) {
                while (!(p.x - b >= 0 && p.x - b < 8 && p.y - b >= 0 && p.y - b < 8 && mover.canMove(GameBoard.board, p.x, p.y, p.x - b, p.y - b))) {
                    b = (int) (Math.random() * 5);
                }
                mover.move(GameBoard.board, p.x, p.y, p.x - b, p.y - b);
                return;
            } else if (GameBoard.board[p.x + 1][p.y - 1] == 0) {
                while (!(p.x - b >= 0 && p.x - b < 8 && p.y - b >= 0 && p.y - b < 8 && mover.canMove(GameBoard.board, p.x, p.y, p.x + b, p.y - b))) {
                    b = (int) (Math.random() * 5);
                }
                mover.move(GameBoard.board, p.x, p.y, p.x + b, p.y - b);
                return;
            } else if (GameBoard.board[p.x + 1][p.y + 1] == 0) {
                while (!(p.x - b >= 0 && p.x - b < 8 && p.y - b >= 0 && p.y - b < 8 && mover.canMove(GameBoard.board, p.x, p.y, p.x + b, p.y + b))) {
                    b = (int) (Math.random() * 5);
                }
                mover.move(GameBoard.board, p.x, p.y, p.x + b, p.y + b);
                return;
            } else if (GameBoard.board[p.x - 1][p.y + 1] == 0) {
                while (!(p.x - b >= 0 && p.x - b < 8 && p.y - b >= 0 && p.y - b < 8 && mover.canMove(GameBoard.board, p.x, p.y, p.x - b, p.y + b))) {
                    b = (int) (Math.random() * 5);
                }
                mover.move(GameBoard.board, p.x, p.y, p.x - b, p.y + b);
                return;
            }
        } else if (GameBoard.board[p.x][p.y] > 0) {
            if (player == 1) {

            }
        }
    }
}