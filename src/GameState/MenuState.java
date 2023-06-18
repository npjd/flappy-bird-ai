package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

import Entities.Background;
import Game.GamePanel;

// Game state for displaying menu
public class MenuState extends GameState {

    // instance variables
    private Background background;
    private String[] options = { "Play", "Train", "God", "Quit" };
    private int currentSelection = 0;
    private String title = "Flappy Bird AI";
    private String author = "by @nimapourjafar";
    private Font titleFont;
    private Font font;

    /*
     * Constructor for MenuState
     * 
     * @param gsm the GameStateManager
     */
    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    /*
     * Initializes the MenuState
     */
    public void init() {
        // setting instance variables
        background = new Background(2);
        titleFont = new Font("Arial", Font.PLAIN, 35);
        font = new Font("Arial", Font.PLAIN, 22);
    }

    /*
     * updates the MenuState
     */
    public void update() {
        // we only have to update background
        background.update();
    }

    /*
     * Draws our state
     * 
     * @param g the Graphics2D object
     * 
     * 
     */
    public void draw(Graphics2D g) {
        // draw bg first so its behind everything
        background.draw(g);
        // draw title
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        g.drawString(title, 150, 100);

        // set font back to normal
        g.setFont(font);

        g.drawString("Control using up or down arrows", 100, 250);
        // loop through options and draw them
        for (int i = 0; i < options.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 220, 340 + i * 25);
        }

        // draw author
        g.setColor(Color.BLACK);
        g.drawString(author, 150, GamePanel.HEIGHT - 20);

    }

    /*
     * Handles key presses
     * 
     * @param k the key that was pressed
     */
    public void keyPressed(int k) {
        // if click enter, select the current choice
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        // if click up, decrement the current choice and wrap around if necessary
        else if (k == KeyEvent.VK_UP) {
            currentSelection--;
            if (currentSelection == -1) {
                currentSelection = options.length - 1;
            }
        }
        // if click down, increment the current choice and wrap around if necessary
        else if (k == KeyEvent.VK_DOWN) {
            currentSelection++;
            if (currentSelection == options.length) {
                currentSelection = 0;
            }
        }
    }

    public void keyReleased(int k) {
        return;
    }

    // launch the respective game state depending on which choice was selected
    private void select() {
        if (currentSelection == 0) {
            gsm.setState(1);
        } else if (currentSelection == 1) {
            gsm.setState(2);
        } else if (currentSelection == 2) {
            gsm.setState(3);
        } else if (currentSelection == 3) {
            // quit
            System.exit(0);
        }
    }

}
