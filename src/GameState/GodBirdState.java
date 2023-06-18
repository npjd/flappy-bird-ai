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

public class GodBirdState extends GameState {

    private Background background;
    private Floor floor;
    private Bird godBird;
    private Bird playerBird;

    private ArrayList<Pipe> pipes;
    private int pipeSpeed = 4;

    private int score;

    public GodBirdState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        try {
            background = new Background(4);
            floor = new Floor(4);
            godBird = new Bird(GamePanel.WIDTH / 2, 200, 52, 24, true);
            NeuralNetwork brain = NeuralNetwork.load("best_bird.ser");
            godBird.brain = brain;
            playerBird = new Bird(GamePanel.WIDTH / 2, 200, 52, 24, false);

            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));

            score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        godBird.update();
        playerBird.update();
        godBird.think(pipes);
        floor.update();
        background.update();

        for (int i = 0; i < pipes.size(); i++) {

            Pipe pipe = pipes.get(i);

            pipe.update();

            if (pipe.getX() < godBird.x && !pipe.isPassed()) {
                try {
                    pipe.setPassed(true);
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));
                    score++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (pipe.isOffscreen()) {
                pipes.remove(pipe);
            }
            if (pipe.collidesWith(godBird.getBounds()) || floor.collidesWith(godBird.getBounds()) || godBird.y < 0) {
                init();
            }
            
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        background.draw(g);
        godBird.draw(g);
        playerBird.draw(g);
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        floor.draw(g);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_UP) {
            playerBird.jump();
        }
        else if (k == KeyEvent.VK_Q) {
            gsm.setState(0);
        }
        return;
    }

    @Override
    public void keyReleased(int k) {
        return;
    }

}
