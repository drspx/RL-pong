package org.example;

public class PlayerStats {

    public boolean player1WonLastRound = false;
    public boolean player2WonLastRound = false;

    private boolean player1JustHitBall = false;
    private boolean player2JustHitBall = false;

    public void setPlayer1JustHitBall(boolean player1JustHitBall) {
        this.player1JustHitBall = player1JustHitBall;
        this.player2JustHitBall = !player1JustHitBall;
    }

    public void setPlayer2JustHitBall(boolean player2JustHitBall) {
        this.player2JustHitBall = player2JustHitBall;
        this.player1JustHitBall = !player2JustHitBall;
    }

    public boolean didPlayer1JustHitBall() {
        return player1JustHitBall;
    }

    public boolean didPlayer2JustHitBall() {
        return player2JustHitBall;
    }

    public void reset() {
        player1JustHitBall = false;
        player2JustHitBall = false;
        player1WonLastRound = false;
        player2WonLastRound = false;
    }
}
