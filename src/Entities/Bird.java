package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bird {
    private int x, y;
    private int width, height;
    private int velocity;
    private double rotation;
    private BufferedImage image;
    private Rectangle bounds;

    public Bird(int x, int y, int width, int height) throws IOException {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = ImageIO.read(new File("./assets/bird.png"));
        this.velocity = 0;
        this.rotation = 0;
        this.bounds = new Rectangle(x, y, width, height);
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

    public boolean collidesWith(Rectangle rect) {
        return bounds.intersects(rect);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}