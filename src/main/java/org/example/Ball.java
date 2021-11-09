package org.example;

import java.awt.*;

public class Ball extends Entity {

    public boolean isOut = false;

    double vX;
    double vY;

    private final GamePanel gamePanel;

    public Ball(int x, int y, int width, int height, Color color, int vX, int vY, GamePanel gamePanel) {
        super(x, y, width, height, color);
        this.vX = vX;
        this.vY = vY;
        this.gamePanel = gamePanel;
    }

    @Override
    public void doPendingMotions() {
        gamePanel.stats.reset();

        if (y < 0) { //ball is bouncing wall
            vY = -vY * 1.02;
        }
        if (y > GamePanel.GAME_HEIGHT - GamePanel.BALL_SIZE) { //ball is bouncing wall
            vY = -vY * 1.02;
        }
        //ball is bouncing paddle 1
        if (x < GamePanel.BALL_SIZE  //ball is at the edge of the screen at player 1's side.
                && y > gamePanel.player1.y //ball is above top of player 1's paddle
                && y < gamePanel.player1.y + GamePanel.PADDLE_HEIGHT //ball is below bottom of player 1's paddle
        ) {
            vX = -vX;
            x = (int) (x + vX * 10);
            y = (int) (y + vY * 10);
            gamePanel.stats.setPlayer1JustHitBall(true);
        }
        if (x > GamePanel.GAME_WIDTH - GamePanel.BALL_SIZE * 2 //ball is at edge of screen by player 2's side
                && y > gamePanel.player2.y //ball is above top of player 2's paddle
                && y < gamePanel.player2.y + GamePanel.PADDLE_HEIGHT // ball is below player 2's paddle
        ) {
            vX = -vX;
            x = (int) (x + vX * 10);
            y = (int) (y + vY * 10);
            gamePanel.stats.setPlayer2JustHitBall(true);
        }
        x = (int) (x + vX * 5);
        y = (int) (y + vY * 5);

        if (x <= 0) {
            isOut = true;
            gamePanel.stats.player1WonLastRound = true;
        }
        if (x >= GamePanel.GAME_WIDTH) {
            isOut = true;
            gamePanel.stats.player2WonLastRound = true;
        }

    }
}
