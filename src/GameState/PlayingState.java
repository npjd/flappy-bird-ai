package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import Entities.Background;
import Entities.Bird;
import Entities.Floor;
import Entities.Pipe;
import Game.GamePanel;

// state for just playing the game
public class PlayingState extends GameState {
    // instance variables
    private Background background;
    private Floor floor;
    private Bird bird;

    private ArrayList<Pipe> pipes;
    private int pipeSpeed = 4;

    private int score;

    /*
     * Constructor and runs init()
     * 
     * @param gsm: GameStateManager
     * 
     */
    public PlayingState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        try {
            // create background
            background = new Background(4);
            // create floor
            floor = new Floor(4);
            // create bird
            bird = new Bird(GamePanel.WIDTH / 2, 200, 52, 24, false);

            // create first pipe and list
            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));

            score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Updating state
     */
    public void update() {
        // update entities
        bird.update();
        floor.update();
        background.update();

        for (int i = 0; i < pipes.size(); i++) {

            Pipe pipe = pipes.get(i);

            // update pipe
            pipe.update();

            // if bird passed pipe, add new pipe and increment score
            if (pipe.getX() < bird.x && !pipe.isPassed()) {
                try {
                    pipe.setPassed(true);
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));
                    score++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // if pipe is offscreen, remove it
            if (pipe.isOffscreen()) {
                pipes.remove(pipe);
            }
            // if bird collides with pipe or floor, reset
            if (pipe.collidesWith(bird.getBounds()) || floor.collidesWith(bird.getBounds()) || bird.y < 0) {
                init();
            }
        }
    }

    /*
     * Drawing state
     * 
     * @param g Graphics2D
     */
    public void draw(Graphics2D g) {
        // draw background
        background.draw(g);
        // draw bird
        bird.draw(g);
        // draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        // draw floor
        floor.draw(g);
        // draw score
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Press space jump and control your bird", 10, GamePanel.HEIGHT - 50);
        g.drawString("Press Q to quit", 10, GamePanel.HEIGHT - 30);
    }

    /*
     * Key pressed
     * 
     * @param k: key pressed
     */
    public void keyPressed(int k) {
        // if space, jump
        if (k == KeyEvent.VK_SPACE) {
            bird.jump();
        }
        // if q, go to menu
        else if (k == KeyEvent.VK_Q) {
            gsm.setState(0);
        }
    }

    public void keyReleased(int k) {
        return;
    }
}