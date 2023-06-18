package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.Color;

import Entities.Background;
import Entities.Bird;
import Entities.Pipe;
import Entities.Floor;
import Game.GamePanel;
import Math.NeuralNetwork;

// State for running god bird 
public class GodBirdState extends GameState {

    // Entities
    private Background background;
    private Floor floor;
    private Bird godBird;

    // Pipes
    private ArrayList<Pipe> pipes;
    private int pipeSpeed = 4;

    // Score
    private int score;

    /*
     * Constructor + runs init()
     * 
     * @param gsm: GameStateManager
     * 
     */
    public GodBirdState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    /*
     * Initializes entities
     * 
     */
    public void init() {
        try {
            // Initialize entities
            background = new Background(4);
            floor = new Floor(4);
            godBird = new Bird(GamePanel.WIDTH / 2, 200, 52, 24, true);
            // Load neural network
            NeuralNetwork brain = NeuralNetwork.load("best_bird.ser");
            // Set neural network
            godBird.brain = brain;

            // add first pipe and add to list
            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));

            score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Updates entities
     * 
     */
    public void update() {
        // update all entities
        godBird.update();
        godBird.think(pipes);
        floor.update();
        background.update();

        // loop through pipes
        for (int i = 0; i < pipes.size(); i++) {

            // get pipe
            Pipe pipe = pipes.get(i);

            // update pipe
            pipe.update();

            // check if pipe is passed
            if (pipe.getX() < godBird.x && !pipe.isPassed()) {
                try {
                    // increment score, set pipe as passed, and add new pipe
                    pipe.setPassed(true);
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));
                    score++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // check if pipe is offscreen
            if (pipe.isOffscreen()) {
                pipes.remove(pipe);
            }

            // check if god bird or player bird collides with pipe or floor
            if (pipe.collidesWith(godBird.getBounds()) || floor.collidesWith(godBird.getBounds()) || godBird.y < 0) {
                init();
            }

        }
    }

    /*
     * Draws entities
     * 
     * @param g: Graphics2D
     * 
     */
    public void draw(Graphics2D g) {
        // just drawing everything
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        background.draw(g);
        godBird.draw(g);
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        floor.draw(g);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Press Q to quit", 10, GamePanel.HEIGHT - 30);
    }

    /*
     * Handles key presses
     * 
     * @param k: key pressed
     * 
     */
    public void keyPressed(int k) {
        // if q is pressed, go back to menu
        if (k == KeyEvent.VK_Q) {
            gsm.setState(0);
        }
        return;
    }

    public void keyReleased(int k) {
        return;
    }

}
