package Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Math.NeuralNetwork;

public class Bird {

    int x;
    int y;
    int width;
    int rotation;
    Integer score;
    double fitness;
    int pipeScore;
    int jumpPos;
    boolean isJumping;
    BufferedImage image;
    BufferedImage rotatedImage;
    double gravity;
    NeuralNetwork brain;
    final double gravityConst = 1.5;

    public Bird(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.gravity = 0;
        this.rotation = 0;
        this.jumpPos = this.y;
        this.isJumping = false;
        // load this image
        // this.image = ImageLoader.loadImage("/Images/bird.png");

        // this.rotatedImage = this.image;
        // this.brain = new NeuralNetwork(5, 8, 2);
        this.score = 0;
        this.fitness = 0.0;
        this.pipeScore = 0;

    }

    public void draw(Graphics2D g) {
        if (this.isJumping){
            this.isJumping = false;
            if (this.rotation > -30)
                this.rotation = -30;
        }
        else if (this.y > this.jumpPos){
            if (this.rotation < 80)
                this.rotation += 10;
        }

        // rotate iamge

        // g.drawImage(this.rotatedImage, this.x, this.y, this);
    }

}
