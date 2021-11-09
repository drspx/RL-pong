package org.example;

import java.awt.*;

public class Player extends Entity{

    private final GamePanel gamePanel;

    public Player(int x, int y, int width, int height, Color c, GamePanel gamePanel) {
        super(x, y, width, height, c);
        this.gamePanel = gamePanel;
    }

    public void goUp() {
        goUp = true;
    }

    public void goDown() {
        goDown = true;
    }

    public void dontGoUp() {
        goUp = false;
    }

    public void dontGoDown() {
        goDown = false;
    }


}
