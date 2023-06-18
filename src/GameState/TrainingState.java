package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
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

// state for training the neural network
public class TrainingState extends GameState {

    // instance variables

    // bacgkround and floor
    private Background background;
    private Floor floor;

    // birds active and birds saved
    private ArrayList<Bird> birds;
    private ArrayList<Bird> savedBirds;
    // birds to run each generation
    private int numBirds = 100;
    // best bird so far
    private Bird bestBird;

    // pipes
    private ArrayList<Pipe> pipes;
    private int pipeSpeed = 4;

    // score and generation count
    private int score;
    private int generationCount = 1;

    // game panel instance
    private GamePanel gamePanel;

    /*
     * Constructor and runs init()
     * 
     * @param gsm: GameStateManager
     * 
     */
    public TrainingState(GameStateManager gsm) {

        // set gsm and get game panel
        this.gsm = gsm;
        this.gamePanel = gsm.getGamePanel();

        // make arrays
        birds = new ArrayList<>();
        savedBirds = new ArrayList<>();

        // random number generator
        Random random = new Random();

        // create birds for generation at random positions
        for (int i = 0; i < numBirds; i++) {
            try {
                birds.add(new Bird(GamePanel.WIDTH / 2, 50 + random.nextInt(350), 52, 24, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // set best bird to first bird
        bestBird = birds.get(0);

        // run init
        init();
    }

    /*
     * Initialize state
     */
    public void init() {
        try {
            // create background and floor
            background = new Background(4);
            floor = new Floor(4);

            // create first pipe and list
            pipes = new ArrayList<>();
            pipes.add(Pipe.generatePipe(GamePanel.WIDTH, 50, 200, 150, pipeSpeed));

            // set score to 0
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
        floor.update();

        background.update();

        // update pipes and birds
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);

            bird.update();
            bird.think(pipes);

            // check if bird collides with top or bottom we save and kill it for later
            if (bird.y < 0 || bird.y > GamePanel.HEIGHT - floor.getHeight()) {
                savedBirds.add(bird);
            } else {
                // check if bird collides with pipe
                for (int j = 0; j < pipes.size(); j++) {
                    Pipe pipe = pipes.get(j);
                    // if it does we save and kill it for later
                    if (pipe.collidesWith(bird.getBounds())) {
                        savedBirds.add(bird);
                        break;
                    }
                }
            }

        }

        // update pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);

            pipe.update();

            // check if birds pass pipe ( all birds are on same x axis)
            if (pipe.getX() < birds.get(0).x && !pipe.isPassed()) {
                try {
                    // if they do we set pipe to passed and add a new pipe
                    pipe.setPassed(true);
                    pipes.add(Pipe.generatePipe(GamePanel.WIDTH + 50, 50, 200, 150, pipeSpeed));
                    score++;
                    // set all birds to have a score of 100 to incentivize passing pipes
                    for (int j = 0; j < birds.size(); j++) {
                        // make sure a bird isnt in our death queue
                        if (!savedBirds.contains(birds.get(j))) {
                            birds.get(j).score += 100;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // if pipe is offscreen we remove it
            if (pipe.isOffscreen()) {
                pipes.remove(pipe);
            }
        }

        // kill all birds we are saving. We have to do this in this way in order to
        // prevent some sort of memory collision
        for (Bird bird : savedBirds) {
            birds.remove(bird);
        }

        // if all birds are dead we make a new generation
        if (birds.isEmpty()) {
            newGeneration();
        }

    }

    /*
     * Increases games speed
     */
    public void increaseFps() {
        // get current fps and increase it by 10 (cap of 250)
        int currentFps = gamePanel.getFPS();
        if (currentFps < 250) {
            gamePanel.setFPS(currentFps + 10);
        }
    }

    /*
     * Redces games speed
     */
    public void decreaseFps() {
        // get current fps and decrease it by 10 (min of 60)
        int currentFps = gamePanel.getFPS();
        if (currentFps > 60) {
            gamePanel.setFPS(currentFps - 10);
        }
    }


    public void newGeneration() {
        // normalize fitness
        normalizeFitness();

        // increase generation count
        generationCount++;

        // sort birds by fitness
        Collections.sort(savedBirds);

        // get best bird from last generation and best bird from this generation
        Bird generationBestBird = savedBirds.get(savedBirds.size() - 1);

        // add those birds to our next generation but mutated
        birds.add(bestBird.copyAndMutate(0.4));
        birds.add(generationBestBird.copyAndMutate(0.4));

        // shuffle saved birds
        Collections.shuffle(savedBirds);

        // add the rest of the birds to our next generation
        for (int i = 0; i < savedBirds.size() - 2; i++) {
            birds.add(selectBird());
        }

        // set best bird to the best bird from this generation
        if (generationBestBird.score > bestBird.score) {
            bestBird = generationBestBird;
        }

        // clear saved birds
        savedBirds.clear();

        // reset state
        init();
    }

    /*
     * Normalize fitness
     */
    public void normalizeFitness() {
        // square the score of each bird
        for (Bird bird : savedBirds) {
            bird.score = Math.pow(bird.score, 2);
        }

        // get sum of all scores
        double sum = 0;
        for (Bird bird : savedBirds) {
            sum += bird.score;
        }

        // divide each birds score by the total sum
        for (Bird bird : savedBirds) {
            bird.fitness = bird.score / sum;
        }
    }

    /*
     * Selects a bird from the saved birds list based on fitness
     * 
     * @return Bird - the selected bird
     */
    public Bird selectBird() {
        // get a random number between 0 and 1
        int index = 0;
        double rand = Math.random();
        // while the random number is greater than 0 we subtract the fitness of the bird
        while (rand > 0) {
            rand -= savedBirds.get(index).fitness;
            // increase index
            index++;
        }
        // decrease index
        index--;
        // return a copy of the bird at the index and mutate it
        return savedBirds.get(index).copyAndMutate(0.4);
    }

    /*
     * Draws the state
     * 
     * @param Graphics2D g - the graphics object
     */
    public void draw(Graphics2D g) {

        g.setFont(new Font("Arial", Font.PLAIN, 12));

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

    /*
     * Handles key presses
     * 
     * @param int k - the key pressed
     */
    public void keyPressed(int k) {
        // if up arrow is pressed we increase fps
        if (k == KeyEvent.VK_UP) {
            increaseFps();
        } 
        // if down arrow is pressed we decrease fps
        else if (k == KeyEvent.VK_DOWN) {
            decreaseFps();
        } 
        // if q is pressed we quit
        else if (k == KeyEvent.VK_Q) {
            System.out.println("quitting");
            // reset frames
            gamePanel.setFPS(60);
            // saving the best bird (you can do this yourself to testyour own best bird)
            // bestBird.brain.save("best_bird.ser");
            gsm.setState(0);
        }
        return;
    }

    public void keyReleased(int k) {
        return;
    }
}