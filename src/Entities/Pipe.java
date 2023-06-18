package Entities;

import java.awt.Graphics;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Game.GamePanel;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

// Class for the pipe entity
public class Pipe {

    // instance variables
    private int x, y;
    private int width, height;
    private int gap;
    private int speed;
    private BufferedImage topImage, bottomImage, pipeExtension;
    private Rectangle topBounds, bottomBounds;
    private boolean isPassed;

    /**
     * Constructor for the Pipe class
     * 
     * @param x      The x-coordinate of the pipe's position
     * @param y      The y-coordinate of the pipe's position
     * @param width  The width of the pipe
     * @param height The height of the pipe
     * @param gap    The gap between the top and bottom pipes
     * @param speed  The speed at which the pipe moves
     * @throws IOException if there is an error reading the pipe's image
     */
    public Pipe(int x, int y, int width, int height, int gap, int speed) throws IOException {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gap = gap;
        this.speed = speed;
        this.topImage = ImageIO.read(new File("./assets/bottom_pipe.png"));
        this.bottomImage = ImageIO.read(new File("./assets/top_pipe.png"));
        this.pipeExtension = ImageIO.read(new File("./assets/pipe_extension.png"));
        // im setting the top bound really high just in case some birds leak through the top (which for some reason happens )
        this.topBounds = new Rectangle(x, -1000, width, y + height + 1000);
        this.bottomBounds = new Rectangle(x, y + height + gap, width, GamePanel.HEIGHT - (y + gap));
        this.isPassed = false;
    }

    /*
     * Updates the pipe's position
     */
    public void update() {
        // move the pipe to the left by speed
        x -= speed;
        // update the bounds
        topBounds.setLocation(x, -1000);
        bottomBounds.setLocation(x, y + height + gap);
    }

    /*
     * Draws the pipe
     * 
     * @param g the Graphics object used to draw the pipe
     */
    public void draw(Graphics g) {

        // im checking to see if the pipe "starts" on-screen or not, and adding the pipe extension if it does
        if (y > 0) {
            g.drawImage(pipeExtension, x, 0, width, y, null);
            g.drawImage(topImage, x, y, width, height, null);
        } else {
            g.drawImage(topImage, x, y, width, height, null);
        }
        // same thing with checking if the bottom is offscreen
        if (y + gap + 2 * height < GamePanel.HEIGHT) {
            g.drawImage(bottomImage, x, y + height + gap, width, height, null);
            // flipping the pipe extension image
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-pipeExtension.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            g.drawImage(op.filter(pipeExtension, null), x, y + gap + height * 2, width,
                    GamePanel.HEIGHT - (y + gap + height), null);
        } else {
            g.drawImage(bottomImage, x, y + height + gap, width, height, null);
        }

        // note: i really should've just had one image and also flip it for the bottom pipe like im doing the extension. But im lazy....
    }

    /*
     * Checks if the pipe is offscreen
     * 
     * @return true if the pipe is offscreen, false otherwise
     */
    public boolean isOffscreen() {
        return x + width < 0;
    }

    /*
     * Generates a random pipe
     * 
     * @param startX The x-coordinate of the pipe's starting position
     * @param width  The width of the pipe
     * @param height The height of the pipe
     * @param gap    The gap between the top and bottom pipes
     * @param speed  The speed at which the pipe moves
     * @return a Pipe object
     * @throws IOException if there is an error reading the pipe's image
     */
    public static Pipe generatePipe(int startX, int width, int height, int gap, int speed)
            throws IOException {
        int x = startX;
        int y = (int) (Math.random() * (GamePanel.HEIGHT - 100 - height - gap));
        return new Pipe(x, y, width, height, gap, speed);
    }

    /*
     * Checks if the pipe collides with a rectangle
     * 
     * @param rect the rectangle to check collision with
     * @return true if the pipe collides with the rectangle, false otherwise
     */
    public boolean collidesWith(Rectangle rect) {
        return topBounds.intersects(rect) || bottomBounds.intersects(rect);
    }

    /*
     * Gets the x position of the pipe
     *
     * @return x position of the pipe
     */
    public int getX() {
        return x;
    }

    /*
     * Gets the y position of the top pipe
     *
     * @return y position of the top pipe
     */
    public int getTopHeight() {
        return y + height;
    }

    /*
     * Gets the y position of the bottom pipe
     *
     * @return y position of the bottom pipe
     */
    public int getBottomHeight() {
        return y + height + gap;
    }

    /*
     * Checks if the pipe has been passed
     *
     * @return if pipe is passed
     */
    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }
}