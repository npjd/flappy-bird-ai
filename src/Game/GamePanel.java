package Game;

import javax.swing.JPanel;

import GameState.GameStateManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private GameStateManager gsm;
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

        gsm = new GameStateManager();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        boolean running = true;

        long start;
        long elapsed;
        long wait;

        while (running) {

            start = System.nanoTime();

            gsm.update();
            gsm.draw(graphics);
            
            getGraphics().drawImage(image,0,0,null);
            getGraphics().dispose();

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


    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    public void keyPressed(KeyEvent e) {
        gsm.keyPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        gsm.keyReleased(e.getKeyCode());
    }

}
