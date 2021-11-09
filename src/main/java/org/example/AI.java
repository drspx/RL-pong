package org.example;

public class AI {

    int counter = 0;

    public void tick(Ball ball, Player player1) {

        if (ball.y > player1.y + GamePanel.PADDLE_HEIGHT * 0.2 ) {
            player1.goDown();
            player1.dontGoUp();
            return;
        } else {
            player1.dontGoDown();
        }

        if (ball.y < player1.y + GamePanel.PADDLE_HEIGHT * 0.8 ) {
            player1.goUp();
            player1.dontGoDown();
        } else {
            player1.dontGoUp();
        }
    }
}
