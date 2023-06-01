package Game;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;

    private Thread thread;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    public GamePanel() {
        super();

        setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));

        setFocusable(true);
    }

    public void addNotify() {
        super.addNotify();
        // start the thread and key listener
        if (thread == null) {
            thread = new Thread(this);
            // addKeyListener(this);
            thread.start();
        }
    }

    public void run() {

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        boolean running = true;

        long start;
        long elapsed;
        long wait;

        while (running) {

            start = System.nanoTime();

            // update
            // draw
            // draw to actual panel

            elapsed = System.nanoTime() - start;

            wait = targetTime - elapsed / 1000000;

            if (wait < 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
