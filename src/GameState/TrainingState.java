package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import Entities.Background;
import Entities.Bird;
import Entities.Floor;
import Entities.Pipe;
import Game.GamePanel;

public class TrainingState extends GameState {
    private Background background;
    private Floor floor;
    private ArrayList<Bird> birds;
    private ArrayList<Bird> savedBirds;
    private int numBirds = 100;
    private ArrayList<Pipe> pipes;
    private int score;

    public TrainingState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        try {
            background = new Background(4);
            floor = new Floor(4);

            birds = new ArrayList<>();
            savedBirds = new ArrayList<>();

            for (int i = 0; i < numBirds; i++) {
                birds.add(new Bird(50, 200, 52, 24, true));
            }

            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, 4));

            score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Iterator<Bird> birdIterator = birds.iterator();
        while (birdIterator.hasNext()) {
            Bird bird = birdIterator.next();
            bird.update();
            bird.think(
                    pipes.get(0).getX() - bird.getX(),
                    pipes.get(0).getTopHeight(),
                    pipes.get(0).getBottomHeight(),
                    bird.getY() - GamePanel.HEIGHT - floor.getHeight());

            for (Pipe pipe : pipes) {
                if (pipe.collidesWith(bird.getBounds()) || floor.collidesWith(bird.getBounds())) {
                    savedBirds.add(bird);
                    birdIterator.remove();
                    break;
                }
            }
        }

        floor.update();
        background.update();

        Iterator<Pipe> pipeIterator = pipes.iterator();
        while (pipeIterator.hasNext()) {
            Pipe pipe = pipeIterator.next();
            pipe.update();
            if (pipe.isOffscreen()) {
                pipeIterator.remove();
                try {
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, 5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                score++;
            }
        }

        if (birds.isEmpty()) {
            init();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        background.draw(g);
        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }
        floor.draw(g);
        for (Bird bird : birds) {
            bird.draw(g);
        }
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void keyPressed(int k) {
        return;
    }

    @Override
    public void keyReleased(int k) {
        return;
    }
}