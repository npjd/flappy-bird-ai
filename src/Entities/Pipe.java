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

public class Pipe {
    private int x, y;
    private int width, height;
    private int gap;
    private int speed;
    private BufferedImage topImage, bottomImage, pipeExtension;
    private Rectangle topBounds, bottomBounds;
    private boolean isPassed;

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
        this.topBounds = new Rectangle(x, -1000, width, y + height + 1000);
        this.bottomBounds = new Rectangle(x, y + height + gap, width, GamePanel.HEIGHT - (y + gap));
        this.isPassed = false;
    }

    public void update() {
        x -= speed;
        topBounds.setLocation(x, -1000);
        bottomBounds.setLocation(x, y + height + gap);
    }

    public void draw(Graphics g) {

        if (y > 0) {
            g.drawImage(pipeExtension, x, 0, width, y, null);
            g.drawImage(topImage, x, y, width, height, null);
        } else {
            g.drawImage(topImage, x, y, width, height, null);
        }
        if (y + gap + 2 * height < GamePanel.HEIGHT) {
            g.drawImage(bottomImage, x, y + height + gap, width, height, null);
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-pipeExtension.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            g.drawImage(op.filter(pipeExtension, null), x , y + gap + height * 2, width,
                    GamePanel.HEIGHT - (y + gap + height), null);
        } else {
            g.drawImage(bottomImage, x, y + height + gap, width, height, null);
        }

    }

    public boolean isOffscreen() {
        return x + width < 0;
    }

    public static Pipe generatePipe(int startX, int width, int height, int gap, int speed)
            throws IOException {
        int x = startX;
        int y = 50 + (int) (Math.random() * (GamePanel.HEIGHT - 100 - height - gap));
        return new Pipe(x, y, width, height, gap, speed);
    }

    public boolean collidesWith(Rectangle rect) {
        return topBounds.intersects(rect) || bottomBounds.intersects(rect);
    }

    public int getX() {
        return x;
    }

    public int getTopHeight() {
        return y+height;
    }

    public int getBottomHeight() {
        return y+height+gap;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }
}