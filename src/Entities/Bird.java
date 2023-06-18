package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Game.GamePanel;
import Math.Matrix;
import Math.NeuralNetwork;

/**
 * Class for the bird entity (either player controlled or AI controlled).
 */
public class Bird implements Comparable<Bird> {

    // instance variables

    // position
    public int x, y;
    // size
    private int width, height;
    private int velocity;
    // variables for image, rotation, and boudns
    private double rotation;
    private BufferedImage image;
    private Rectangle bounds;

    public double score;
    public double fitness;

    // brain for AI controlled birds
    public NeuralNetwork brain;

    // variables for AI controlled birds
    private boolean isThinkingBird;

    /**
     * Constructs a Bird object.
     *
     * @param x              the x-coordinate of the bird's position
     * @param y              the y-coordinate of the bird's position
     * @param width          the width of the bird's image
     * @param height         the height of the bird's image
     * @param isThinkingBird indicates if the bird is a thinking bird or not
     * @throws IOException if there is an error reading the bird's image
     */

    public Bird(int x, int y, int width, int height, boolean isThinkingBird) throws IOException {
        // set all instance variables and load image
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = ImageIO.read(new File("./assets/bird.png"));
        this.velocity = 0;
        this.rotation = 0;
        this.bounds = new Rectangle(x, y, width, height);
        this.score = 0;

        this.isThinkingBird = isThinkingBird;

        // if the bird is a thinking bird, initialize its brain
        if (isThinkingBird) {
            brain = new NeuralNetwork(5, 5, 2);
        } else {
            brain = null;
        }
    }

    /**
     * Returns the x-coordinate of the bird's position.
     *
     * @param g the Graphics2D object to draw the bird
     * 
     * @return the x-coordinate of the bird's position
     */
    public void draw(Graphics2D g) {
        g.rotate(rotation, x + width / 2, y + height / 2);
        g.drawImage(image, x, y, null);
        g.rotate(-rotation, x + width / 2, y + height / 2);
    }

    /**
     * Updates the birds position, velocity, and bounds.
     */
    public void update() {
        // increase score for each distance travelled for fitness
        score += 1;
        // update position and velocity
        y += velocity;
        velocity += 1;
        // update bounds
        bounds.setLocation(x, y);
        // change the rotatiton based on how fast the bird is moving
        rotation = Math.toRadians(Math.max(-90, Math.min(velocity * 3, 90)));
    }

    /**
     * Makes bird jump by changing its velocity.
     */
    public void jump() {
        velocity = -10;
    }

    /**
     * Used to decide whether bird should jump or not
     * 
     * @param pipes the list of pipes in the game
     */
    public void think(ArrayList<Pipe> pipes) {
        // check if bird is a thinking bird
        if (!this.isThinkingBird) {
            throw new RuntimeException("This bird is not a thinking bird");
        }

        if (pipes.size() == 0) {
            return;
        }

        // find the closest pipe
        Pipe closestPipe = pipes.get(0);
        double diff;
        double record = Double.POSITIVE_INFINITY;
        // loop through and recored difference between bird and pipe
        for (int i = 0; i < pipes.size(); i++) {
            diff = pipes.get(i).getX() - this.x;
            if (diff > 0 && diff < record) {
                record = diff;
                closestPipe = pipes.get(i);
            }
        }

        // setup input matrix
        double[][] inputs = new double[5][1];

        // first input: x distance between bird and pipe
        inputs[0][0] = (closestPipe.getX() - this.x + this.image.getWidth()) / ((double) GamePanel.WIDTH);
        // second input: y distance between bird and top of pipe
        inputs[1][0] = (closestPipe.getTopHeight()) / ((double) GamePanel.HEIGHT);
        // third input: y distance between bird and bottom of pipe
        inputs[2][0] = (closestPipe.getBottomHeight()) / ((double) GamePanel.HEIGHT);
        // fourth input: y position of bird
        inputs[3][0] = this.y / ((double) GamePanel.HEIGHT);
        // fifth input: velocity of bird
        inputs[4][0] = this.velocity / 10.0;

        // all of these values are normalized by the width and height of the game panel
        // in order to make the inputs small enough for the sigmoid function to work

        // creating a matrix from the inputs
        Matrix inputMatrix = Matrix.fromArray(inputs);

        // normalizing the matrix using the sigmoid function
        inputMatrix.sigmoid();

        try {
            // feed forward
            Matrix outputs = brain.feedForward(inputMatrix);
            // check probabilities of jumping and not jumping in order to decide whether to
            // jump or not
            if (outputs.matrix[0][0] < outputs.matrix[1][0]) {
                this.jump();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error feeding forward");
            return;
        }

    }

    /**
     * Checks if the bird collides with a rectangle.
     *
     * @param rect the rectangle to check collision with
     * @return true if the bird collides with the rectangle, false otherwise
     */
    public boolean collidesWith(Rectangle rect) {
        return bounds.intersects(rect);
    }

    /**
     * Returns the bounds of the bird.
     *
     * @return the bounds of the bird
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Compares fitness of two birds.
     * 
     * @param other the other bird to compare to
     * 
     * @return -1 if this bird has a higher fitness, 1 if the other bird has a
     *         higher fitness, 0 if they have the same fitness
     */
    public int compareTo(Bird other) {
        if (this.fitness > other.fitness) {
            return -1;
        } else if (this.fitness < other.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns a copy of the bird with a mutated brain.
     * 
     * @param mutationRate the rate at which the brain mutates
     * 
     * @return a copy of the bird with a mutated brain
     */
    public Bird copyAndMutate(double mutationRate) {
        try {
            Random random = new Random();
            Bird copy = new Bird(GamePanel.WIDTH / 2, 50 + random.nextInt(350), width, height, this.isThinkingBird);
            copy.brain = this.brain.copyAndMutate(mutationRate);
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error copying and mutating");
            return null;
        }
    }

}
