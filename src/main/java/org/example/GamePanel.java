package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = 700;
    static final Dimension DIMENSION = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    private static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 100;
    public static final int BALL_SIZE = 10;

    public Thread thread;
    public Player player1;
    public Player player2;
    public Ball ball;

    private List<Entity> entities = new ArrayList<>();
    public static boolean gameOver = false;

    private boolean isAIPlaying = true;
    private AI ai = new AI();
    private NNAI nnai = new NNAI(this);

    private int player1Score = 0;
    private int player2Score = 0;

    private boolean doubleTime = false;
    private boolean tripleTime = false;

    public PlayerStats stats = new PlayerStats();

    public GamePanel() {
        newEntities();

        setFocusable(true);
        addKeyListener(new KeyAdapterImpl());
        this.setPreferredSize(DIMENSION);

        thread = new Thread(this);
        thread.start();

    }

    private void newEntities() {
        entities = new ArrayList<>();
        this.gameOver = false;

        player1 = new Player(0, GAME_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, Color.red, this);
        player2 = new Player(GAME_WIDTH - PADDLE_WIDTH, GAME_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, Color.yellow, this);
        entities.add(player1);
        entities.add(player2);

        ball = new Ball(GAME_WIDTH / 2, GAME_HEIGHT / 2,
                BALL_SIZE, BALL_SIZE,
                Color.white,
                Math.random() > 0.5 ? 1 : -1,
                Math.random() > 0.5 ? 1 : -1, this);
        entities.add(ball);
    }

    private void newEntitiesTest() {
        entities = new ArrayList<>();
        this.gameOver = false;

        player1 = new Player(0, GAME_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, Color.red, this);
        player2 = new Player(GAME_WIDTH - PADDLE_WIDTH, GAME_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT, Color.yellow, this);
        entities.add(player1);
        entities.add(player2);

        for (int i = 0; i < 10; i++) {
            int g = GAME_HEIGHT / 10;
            entities.add(new Ball(GAME_WIDTH / 2, g * i, BALL_SIZE, BALL_SIZE, Color.white, 1, 0, this));
        }

    }

    @Override
    public void run() {
        long lastTick = System.currentTimeMillis();
        while (true) {
            if ((System.currentTimeMillis() - lastTick) > 20
                    || (doubleTime && (System.currentTimeMillis() - lastTick) > 1)
                    || tripleTime) {
                lastTick = System.currentTimeMillis();
                tick();
            }
        }
    }

    private void tick() {

        if (ball.isOut) {
            gameOver = true;
            player1.setColor(Color.white);
            player2.setColor(Color.white);
        }

        if (isAIPlaying) {
            ai.tick(ball, player1);
//            ai.tick(ball, player2);
            nnai.tick(ball, player2);
        }

        if (!gameOver) {
            moveEntities();
        } else {
            setScore();
            newEntities();
        }
        repaint();
    }

    private void setScore() {
        if (ball.isOut && ball.x > GAME_WIDTH / 2) {
            player1Score++;
            stats.player1WonLastRound = true;
            stats.player2WonLastRound = false;
        } else if (ball.isOut && ball.x < GAME_WIDTH / 2) {
            player2Score++;
            stats.player1WonLastRound = false;
            stats.player2WonLastRound = true;
        } else {
            stats.player1WonLastRound = false;
            stats.player2WonLastRound = false;
        }
        System.out.println("player1:" + player1Score + " player2:" + player2Score);
    }

    private void moveEntities() {
        for (Entity entity : entities) {
            entity.doPendingMotions();
        }
    }


    @Override
    public void paint(Graphics g) {
        Image image = createImage(getWidth(), getHeight());
        Graphics graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);

    }

    private void draw(Graphics graphics) {
        for (Entity entity : entities) {
            entity.draw(graphics);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private class KeyAdapterImpl implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if ((int) e.getKeyChar() == 27) { //press escape
                System.out.println("exit");
                System.exit(0);
            }
            if (e.getKeyChar() == 'r') {
                newEntities();
            }
            if (e.getKeyChar() == 't') {
                newEntitiesTest();
            }
            if (e.getKeyChar() == 'g') {
                tripleTime = !tripleTime;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                player1.goUp();
            }
            if (e.getKeyChar() == 'z') {
                player1.goDown();
            }
            if (e.getKeyChar() == '6') {
                player2.goUp();
            }
            if (e.getKeyChar() == '3') {
                player2.goDown();
            }
            if (e.getKeyChar() == 'f') {
                doubleTime = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                player1.dontGoUp();
            }
            if (e.getKeyChar() == 'z') {
                player1.dontGoDown();
            }
            if (e.getKeyChar() == '6') {
                player2.dontGoUp();
            }
            if (e.getKeyChar() == '3') {
                player2.dontGoDown();
            }
            if (e.getKeyChar() == 'f') {
                doubleTime = false;
            }

        }
    }
}
