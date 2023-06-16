package Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.GamePanel;

public class Background {
    int x1, x2, x3;
    int speed;
    int width;
    BufferedImage image;

    public Background(int speed) {
        this.speed = speed;
        x1 = 0;

        try {
            this.image = ImageIO.read(new File("./assets/bg.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image");
        }

        this.width = this.image.getWidth();

        this.x2 = this.width;
        this.x3 = this.width * 2;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, x1, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        g.drawImage(image, x2, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        g.drawImage(image, x3, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
    }

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
