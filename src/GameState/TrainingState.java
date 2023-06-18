package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private ArrayList<Bird> deadBirds;
    private int numBirds = 100;
    private Bird bestBird;

    private ArrayList<Pipe> pipes;
    private int pipeSpeed = 4;

    private int score;
    private int generationCount = 1;

    private GamePanel gamePanel;

    
    public TrainingState(GameStateManager gsm) {

        this.gsm = gsm;
        this.gamePanel = gsm.getGamePanel();

        birds = new ArrayList<>();
        savedBirds = new ArrayList<>();
        deadBirds = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < numBirds; i++) {
            try {
                birds.add(new Bird(GamePanel.WIDTH / 2, 50 + random.nextInt(350), 52, 24, true));
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
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, pipeSpeed));

            score = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        floor.update();

        background.update();

        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);

            bird.update();
            bird.think(pipes);

            if (bird.y < 0 || bird.y > GamePanel.HEIGHT - floor.getHeight()) {
                savedBirds.add(bird);
                deadBirds.add(bird);
            } else {
                for (int j = 0; j < pipes.size(); j++) {
                    Pipe pipe = pipes.get(j);
                    if (pipe.collidesWith(bird.getBounds())) {
                        savedBirds.add(bird);
                        deadBirds.add(bird);
                        break;
                    }
                }
            }

        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);

            pipe.update();

            if (pipe.getX() < birds.get(0).x && !pipe.isPassed()) {
                try {
                    pipe.setPassed(true);
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));
                    score++;
                    for (int j = 0; j < birds.size(); j++) {
                        birds.get(j).score += 100;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (pipe.isOffscreen()) {
                pipes.remove(pipe);
            }
        }

        for (Bird bird : deadBirds) {
            birds.remove(bird);
        }

        if (birds.isEmpty()) {
            newGeneration();
        }

    }

    public void increaseFps() {
        int currentFps = gamePanel.getFPS();
        if (currentFps < 250) {
            gamePanel.setFPS(currentFps + 10);
        }
    }

    public void decreaseFps() {
        int currentFps = gamePanel.getFPS();
        if (currentFps > 60) {
            gamePanel.setFPS(currentFps - 10);
        }
    }

    public void newGeneration() {

        normalizeFitness();

        generationCount++;

        Collections.sort(savedBirds);

        Bird generationBestBird = savedBirds.get(savedBirds.size() - 1);

        birds.add(bestBird.copyAndMutate(0.4));
        birds.add(generationBestBird.copyAndMutate(0.4));

        System.out.println(savedBirds.size());

        for (int i = 0; i < savedBirds.size() - 2; i++) {
            birds.add(selectBird());
        }

        if (generationBestBird.score > bestBird.score) {
            bestBird = generationBestBird;
        }

        savedBirds.clear();
        deadBirds.clear();

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
        int index = 0;
        double rand = Math.random();
        while (rand > 0) {
            rand -= savedBirds.get(index).fitness;
            index++;
        }
        index--;
        return savedBirds.get(index).copyAndMutate(0.4);
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

        g.drawString("Speed (FPS): " + gamePanel.getFPS(), 10, GamePanel.HEIGHT - 40);
        g.drawString("Press up arrow to increase speed, down arrow to decrease", 10, GamePanel.HEIGHT - 30);

    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_UP) {
            increaseFps();
        }
        if (k == KeyEvent.VK_DOWN) {
            decreaseFps();
        }
        return;
    }

    @Override
    public void keyReleased(int k) {
        return;
    }
}