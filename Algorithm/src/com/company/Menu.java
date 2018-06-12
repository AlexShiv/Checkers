package com.company;

import javax.swing.*;

public class Menu extends JFrame {
    private JButton PVPButton;
    private JButton PVEButton;
    private JButton EVEButton;
    private JPanel rootPanel;

    public Menu() {
        setContentPane(rootPanel);
        setSize(600, 400);
        setResizable(false);
        setTitle("Меню");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //При запуске игры обновляет доску и (СДЕЛАТЬ) отключает ИИ для обоих игроков
        PVPButton.addActionListener(event -> {
            int[][] b = {
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {1, 3, 1, 3, 1, 3, 1, 3},
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {0, 3, 0, 3, 0, 3, 0, 3},
                    {3, 0, 3, 0, 3, 0, 3, 0},
                    {2, 3, 2, 3, 2, 3, 2, 3},
                    {3, 2, 3, 2, 3, 2, 3, 2},
                    {2, 3, 2, 3, 2, 3, 2, 3},
            };
            GameBoard.board = b;
            GameJFrame.buildFrame();
            Mover.mode = 1;
        });
        PVEButton.addActionListener(event ->{
            int[][] b = {
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {1, 3, 1, 3, 1, 3, 1, 3},
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {0, 3, 0, 3, 0, 3, 0, 3},
                    {3, 0, 3, 0, 3, 0, 3, 0},
                    {2, 3, 2, 3, 2, 3, 2, 3},
                    {3, 2, 3, 2, 3, 2, 3, 2},
                    {2, 3, 2, 3, 2, 3, 2, 3},
            };
            /*int[][] b = {
                    {3, 0, 3, 0, 3, 0, 3, 1},
                    {0, 3, 0, 3, 0, 3, 0, 3},
                    {3, 2, 3, 0, 3, 0, 3, 1},
                    {0, 3, 0, 3, 0, 3, 0, 3},
                    {3, 0, 3, 0, 3, 0, 3, 0},
                    {0, 3, 2, 3, 0, 3, 0, 3},
                    {3, 0, 3, 0, 3, 0, 3, 0},
                    {0, 3, 0, 3, 0, 3, 0, 3},
            };*/
            GameBoard.board = b;
            GameJFrame.buildFrame();
            Mover.mode = 2;
        });
        EVEButton.addActionListener(event -> {
            int[][] b = {
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {1, 3, 1, 3, 1, 3, 1, 3},
                    {3, 1, 3, 1, 3, 1, 3, 1},
                    {0, 3, 0, 3, 0, 3, 0, 3},
                    {3, 0, 3, 0, 3, 0, 3, 0},
                    {2, 3, 2, 3, 2, 3, 2, 3},
                    {3, 2, 3, 2, 3, 2, 3, 2},
                    {2, 3, 2, 3, 2, 3, 2, 3},
            };
            GameBoard.board = b;
            GameJFrame.buildFrame();
            Mover.mode = 3;
        });
    }


}
