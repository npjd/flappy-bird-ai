package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Math.Matrix;
import Math.NeuralNetwork;

public class Bird {
    private int x, y;
    private int width, height;
    private int velocity;
    private double rotation;
    private BufferedImage image;
    private Rectangle bounds;

    private NeuralNetwork brain;

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

        this.isThinkingBird = isThinkingBird;

        if (isThinkingBird) {
            brain = new NeuralNetwork(5, 5, 2);
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
        y += velocity;
        velocity += 1;
        bounds.setLocation(x, y);
        rotation = Math.toRadians(Math.max(-90, Math.min(velocity * 3, 90)));
    }

    public void jump() {
        velocity = -10;
    }

    public void think(int distanceFromGroup, int distanceFromClosestPipe, int topHeight, int bottomHeight) {
        if (!this.isThinkingBird) {
            throw new RuntimeException("This bird is not a thinking bird");
        }

        double[] inputs = new double[5];
        inputs[0] = distanceFromGroup;
        inputs[1] = distanceFromClosestPipe;
        inputs[2] = topHeight;
        inputs[3] = bottomHeight;
        inputs[4] = this.y;

        double[][] inputArray = { inputs };

        Matrix inputMatrix = Matrix.fromArray(inputArray);


        try {
            Matrix outputs = brain.feedForward(inputMatrix);
            System.out.println(outputs.matrix[0][0] + " " + outputs.matrix[1][0]);
            if (outputs.matrix[0][0] > outputs.matrix[1][0]) {
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

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    
}