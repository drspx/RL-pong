package org.example;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel gamePanel;

    public GameFrame() {
        bootUp();
    }

    public void bootUp() {
        gamePanel = new GamePanel();
        add(gamePanel);
        setTitle("pingpong");
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

}
