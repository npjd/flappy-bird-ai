package Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.GamePanel;

// Class for the animated background
public class Background {
    
    // State variables
    int x1, x2, x3; // X positions of the background images
    int speed; // Speed at which the background moves
    int width; // Width of the background image
    BufferedImage image; // Image used for the background

    // Constructor
    public Background(int speed) {
        this.speed = speed;
        x1 = 0;

        try {
            this.image = ImageIO.read(new File("./assets/bg.png")); // Load the background image
        } catch (IOException e) {
            System.out.println("Error loading background image");
        }

        this.width = this.image.getWidth();

        this.x2 = this.width;
        this.x3 = this.width * 2;
    }

    /**
     * Draws the background images on the graphics object.
     * 
     * @param g The Graphics2D object on which to draw the background images.
     */
    public void draw(Graphics2D g) {
        g.drawImage(image, x1, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null); // Draw the first background image
        g.drawImage(image, x2, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null); // Draw the second background image
        g.drawImage(image, x3, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null); // Draw the third background image
    }

    /**
     * Updates the positions of the background images based on the current speed.
     * If an image moves beyond the left edge of the screen, it is reset to the rightmost position.
     */
    public void update() {
        this.x1 -= this.speed;
        this.x2 -= this.speed;
        this.x3 -= this.speed;

        if (this.x1 <= -this.width) {
            this.x1 = this.x3 + this.width;
        }

        if (this.x2 <= -this.width) {
            this.x2 = this.x1 + this.width;
        }

        if (this.x3 <= -this.width) {
            this.x3 = this.x2 + this.width;
        }
    }
}
