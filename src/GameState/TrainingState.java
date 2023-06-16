package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

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
    private int numBirds = 10;
    private Bird bestBird;

    private ArrayList<Pipe> pipes;

    private int score;
    private double mutationRate = 0.5;
    private int generationCount = 1;

    public TrainingState(GameStateManager gsm) {
        this.gsm = gsm;
        birds = new ArrayList<>();
        savedBirds = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < numBirds; i++) {
            try {
                birds.add(new Bird(50, 50 + random.nextInt(350), 52, 24, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bestBird = birds.get(0);
        init();
    }

    public void init() {
        try {
            background = new Background(4);
            floor = new Floor(4);

            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, 4));
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 200, 50, 200, 150, 4));

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
                    pipes,
                    bird.y - GamePanel.HEIGHT - floor.getHeight());

            Iterator<Pipe> pipeIterator = pipes.iterator();
            while (pipeIterator.hasNext()) {
                Pipe pipe = pipeIterator.next();
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
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, 4));
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 200, 50, 200, 150, 4));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                score++;
            }
        }

        if (birds.isEmpty()) {
            newGeneration();

        }
    }

    public void newGeneration() {

        normalizeFitness();

        generationCount++;

        Collections.sort(savedBirds);

        Bird generationBestBird = savedBirds.get(savedBirds.size() - 1);

        birds.add(bestBird);
        birds.add(generationBestBird);

        for (int i = 0; i < (numBirds - 2) / 2; i++) {
            Bird parentA = selectBird();
            Bird parentB = selectBird();
            Bird child;
            try {
                child = Bird.breed(parentA, parentB);
                child.brain.mutate(mutationRate);
                child.y = 50 + new Random().nextInt(350);
                birds.add(child);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (generationBestBird.score > bestBird.score) {
            bestBird = generationBestBird;
        }

        savedBirds.clear();

        init();
    }

    public void normalizeFitness() {
        for (Bird bird : savedBirds) {
            bird.score = Math.pow(bird.score, 2);
        }

        double sum = 0;
        for (Bird bird : savedBirds) {
            sum += bird.score;
        }

        for (Bird bird : savedBirds) {
            bird.fitness = bird.score / sum;
        }
    }

    public Bird selectBird() {
        double rand = Math.random();
        double sum = 0;
        for (Bird bird : savedBirds) {
            sum += bird.fitness;
            if (sum > rand) {
                return bird;
            }
        }
        return null;
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
        g.drawString("Generation: " + generationCount, 10, 40);
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