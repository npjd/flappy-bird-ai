package Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;
// JPanel that the game will run on
// Make GamePanel extend JPanel and implement runnable & key listener
public class GamePanel extends JPanel 
	implements Runnable, KeyListener{
	// variables for dimensions
	public static final int WIDTH = 500;
	public static final int HEIGHT = 800;
	public static final int SCALE = 1;
	
	// variables for game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// variables for drawing
	private BufferedImage image;
	private Graphics2D g;
	
	// game's game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		// Call JPanel super funciton
		super();
		// set the dimensions of our JPanel
		setPreferredSize(
			new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		// allows for user to select (or focus) the game window to use keyboard 
		setFocusable(true);
	}
	
	public void addNotify() {
		super.addNotify();
		// start the thread and key listener 
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		// create image that hosts all the graphics on the panel
		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		// create game state manager
		gsm = new GameStateManager();
		
	}
	// run function for game panel
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			// call gsm methods
			draw();
			update();
			// draw image to JPanel
			drawToScreen();
			
			// calculate the elapsed time since the start
			elapsed = System.nanoTime() - start;
			
			// calculate how long to sleep the thread
			wait = targetTime - elapsed / 1000000;
			// make sure timeout value is not negative
			if(wait < 0){
				wait = 5;
			}
			// sleep thread to follow FPS
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	// method to update the state
	private void update() {
		gsm.update();
	}
	// method to draw state to image
	private void draw() {
		gsm.draw(g);
	}
	// method to draw image to screen
	private void drawToScreen() {

		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,
				WIDTH * SCALE, HEIGHT * SCALE,
				null);
		g2.dispose();
	}
	
	// implements key press methods
	// all key events get passed to the game state manager in order to see how the current game state handles key events
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}

}



