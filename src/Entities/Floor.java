package Entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.GamePanel;

public class Floor {
    int x1, x2,x3;
    int speed;
    int width;
    BufferedImage image;
    Rectangle bounds;

    public Floor(int speed) {
        this.speed = speed;
        x1 = 0;

        try {
            this.image = ImageIO.read(new File("./assets/floor.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image");
        }

        this.width = this.image.getWidth();

        this.x2 = this.width - 10;

        this.x3 = this.x2 - 10;

        this.bounds = new Rectangle(GamePanel.WIDTH, GamePanel.HEIGHT - image.getHeight(), GamePanel.WIDTH, GamePanel.HEIGHT);
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, x1, GamePanel.HEIGHT - image.getHeight() , null);
        g.drawImage(image, x2, GamePanel.HEIGHT - image.getHeight(), null);
        g.drawImage(image, x3, GamePanel.HEIGHT - image.getHeight(), null);
    }

    public void update() {
        this.x1 -= this.speed;
        this.x2 -= this.speed;
        this.x3 -= this.speed;
        if (this.x1 + this.width < 0) {
            this.x1 = this.x3 + this.width - 10;
        }
        if (this.x2 + this.width < 0) {
            this.x2 = this.x2 + this.width - 10;
        }
        if (this.x3 + this.width < 0) {
            this.x3 = this.x1 + this.width - 10;
        }
    }

    public boolean collidesWith(Rectangle rect){
        return rect.intersects(new Rectangle(x1, GamePanel.HEIGHT - image.getHeight(), GamePanel.WIDTH, GamePanel.HEIGHT));
    }
}
