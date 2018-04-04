package com.company;

import javax.swing.*;
import java.awt.*;

public class GameJFrame {

    private static int rows = 8;
    private static int columns = 8;
    private static Color color1 = Color.BLACK;
    private static Color color2 = Color.WHITE;
    private static JFrame frame;

    public static void buildFrame() {
        Color color;
        frame = new JFrame();
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setTitle("Ход черных");
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container pane = frame.getContentPane();
        pane.setLayout(new GridLayout(rows, columns));//как будут отображаться элементы на фрейме!
        pane.addMouseListener(new Mover());
        for (int i = 0; i < rows; i++) {
            if (i % 2 == 0) {
                color = color2;
            } else {
                color = color1;
            }

            for (int j = 0; j < columns; j++) {
                JPanel panel = new JPanel();
                panel.setBackground(color);
                if (color == color1) {
                    color = color2;
                } else {
                    color = color1;
                }
                pane.add(panel);
            }
        }

        rebuildFrame(GameBoard.board);
    }

    public static void rebuildFrame(int[][] a) {
        clearBord();
        Container pane = frame.getContentPane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JPanel jPanel = (JPanel) pane.getComponent(i * 8 + j);
                if (a[i][j] == 1) {
                    JLabel piece = new JLabel(new ImageIcon("image/white.png"));
                    jPanel.add(piece);
                }
                if (a[i][j] == 2) {
                    JLabel piece = new JLabel(new ImageIcon("image/black.png"));
                    jPanel.add(piece);
                }
                if (a[i][j] == -1) {
                    JLabel piece = new JLabel(new ImageIcon("image/whiteCat.png"));
                    jPanel.add(piece);
                }
                if (a[i][j] == -2) {
                    JLabel piece = new JLabel(new ImageIcon("image/blackCat.png"));
                    jPanel.add(piece);
                }
            }
        }
        pane.repaint();
        pane.revalidate();
    }

    private static void clearBord() {
        Container pane = frame.getContentPane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JPanel jPanel = (JPanel) pane.getComponent(i * 8 + j);
                jPanel.removeAll();
            }
        }
    }

    public static void setTitle(int step) {
        if (step == 1) {
            frame.setTitle("Ход белых");
        } else if (step == 2) {
            frame.setTitle("Ход черных");
        }
    }
}