package org.example;

import java.awt.*;
import java.util.List;

public class Entity extends Rectangle {

    public Color paint;

    boolean goUp = false;
    boolean goDown = false;

    public Entity(int x, int y, int width, int height, Color c) {
        super(x, y, width, height);
        this.paint = c;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(this.paint);
        graphics.fillRect(x, y, width, height);
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

    public void doPendingMotions() {
        if (goUp && this.y>=0) {
            y = y - 10;
        }
        if (goDown && this.y+GamePanel.PADDLE_HEIGHT<=GamePanel.GAME_HEIGHT) {
            y = y + 10;
        }
    }

    public void setColor(Color c) {
        this.paint = c;
    }
}
