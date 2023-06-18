package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.GamePanel;

// Class for the floor entity
public class Floor {
    int x1, x2, x3; // Positions of the floor tiles
    int speed; // Speed at which the floor moves
    int width; // Width of the floor tile
    BufferedImage image; // Image of the floor tile
    Rectangle bounds; // Bounds of the floor entity

    /**
     * Constructor for the Floor class
     * 
     * @param speed The speed at which the floor moves
     */
    public Floor(int speed) {
        this.speed = speed;
        x1 = 0;

        try {
            this.image = ImageIO.read(new File("./assets/floor.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image");
        }

        // Set the width of the floor tile
        this.width = this.image.getWidth();

        // Set the positions of the floor tiles
        this.x2 = this.width;
        this.x3 = this.width * 2;
        
        // Set the bounds of the floor entity
        this.bounds = new Rectangle(0, GamePanel.HEIGHT - image.getHeight(), GamePanel.WIDTH,
                GamePanel.HEIGHT);
    }

    /**
     * Draws the floor tiles on the graphics object.
     * 
     * @param g The Graphics2D object on which to draw the floor tiles.
     */
    public void draw(Graphics2D g) {
        g.drawImage(image, x1, GamePanel.HEIGHT - image.getHeight(), null);
        g.drawImage(image, x2, GamePanel.HEIGHT - image.getHeight(), null);
        g.drawImage(image, x3, GamePanel.HEIGHT - image.getHeight(), null);
    }

    /**
     * Updates the positions of the floor tiles based on the current speed.
     * If a tile moves beyond the left edge of the screen, it is reset to the rightmost position.
     */
    public void update() {
        this.x1 -= this.speed;
        this.x2 -= this.speed;
        this.x3 -= this.speed;

        // Reset the positions of the floor tiles if they move beyond the left edge of the screen
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

    /**
     * Checks if the floor collides with the given rectangle.
     * 
     * @param rect The rectangle to check for collision.
     * @return True if the floor collides with the rectangle, false otherwise.
     */
    public boolean collidesWith(Rectangle rect) {
        return rect
                .intersects(new Rectangle(x1, GamePanel.HEIGHT - image.getHeight(), GamePanel.WIDTH, GamePanel.HEIGHT));
    }

    /**
     * Returns the height of the floor tile.
     * 
     * @return The height of the floor tile.
     */
    public int getHeight() {
        return this.image.getHeight();
    }

}
