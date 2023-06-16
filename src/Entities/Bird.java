package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Math.Matrix;
import Math.NeuralNetwork;

public class Bird implements Comparable<Bird> {
    public int x, y;
    private int width, height;
    private int velocity;
    private double rotation;
    private BufferedImage image;
    private Rectangle bounds;

    public double score;
    public double fitness;

    public NeuralNetwork brain;

    private boolean isThinkingBird;

    public Bird(int x, int y, int width, int height, boolean isThinkingBird) throws IOException {
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

        if (isThinkingBird) {
            brain = new NeuralNetwork(4, 4, 2);
        } else {
            brain = null;
        }
    }

    public void draw(Graphics2D g) {
        g.rotate(rotation, x + width / 2, y + height / 2);
        g.drawImage(image, x, y, null);
        g.rotate(-rotation, x + width / 2, y + height / 2);
    }

    public void update() {
        score += 1;
        y += velocity;
        velocity += 1;
        bounds.setLocation(x, y);
        rotation = Math.toRadians(Math.max(-90, Math.min(velocity * 3, 90)));
    }

    public void jump() {
        velocity = -10;
    }

    public void think(ArrayList<Pipe> pipes,
            int distanceFromGround) {
        if (!this.isThinkingBird) {
            throw new RuntimeException("This bird is not a thinking bird");
        }

        Pipe closestPipe = null;
        double diff;
        double record = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pipes.size(); i++) {
            diff = pipes.get(i).getX() - this.x;
            if (diff > 0 && diff < record) {
                record = diff;
                closestPipe = pipes.get(i);
            }
        }

        if (closestPipe == null) {
            System.out.println("closest pipe is null");
            return;
        }

        double[][] inputs = new double[4][1];

        // inputs[0][0] = closestPipe.getX() - this.x + this.image.getWidth();
        // inputs[1][0] = closestPipe.getTopHeight();
        // inputs[2][0] = closestPipe.getBottomHeight();
        // inputs[3][0] = distanceFromGround;

        inputs[0][0] = Math.random();
        inputs[1][0] = Math.random();
        inputs[2][0] = Math.random();
        inputs[3][0] = Math.random();
        

        Matrix inputMatrix = Matrix.fromArray(inputs);

        inputMatrix.sigmoid();

        try {
            Matrix outputs = brain.feedForward(inputMatrix);
            System.out.println(outputs.matrix[0][0] + " " + outputs.matrix[1][0]);
            if (outputs.matrix[0][0] < outputs.matrix[1][0]) {
                this.jump();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error feeding forward");
            return;
        }

    }

    public boolean collidesWith(Rectangle rect) {
        return bounds.intersects(rect);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int compareTo(Bird otherBird) {
        if (this.fitness > otherBird.fitness) {
            return -1;
        } else if (this.fitness < otherBird.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Bird breed(Bird bird1, Bird bird2) throws IOException {
        Bird child = new Bird(50, 200, 52, 24, true);
        child.brain = NeuralNetwork.crossOver(bird1.brain, bird2.brain);
        return child;
    }

}
