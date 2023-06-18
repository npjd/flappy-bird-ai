package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

import Entities.Background;
import Game.GamePanel;

public class MenuState extends GameState {

    private Background background;
    private String[] options = { "Play", "Train", "God", "Quit" };
    private int currentSelection = 0;
    private String title = "Flappy Bird AI";
    private String author = "by @nimapourjafar";
    private Font titleFont;
    private Font font;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        background = new Background(2);
        titleFont = new Font("Arial", Font.PLAIN, 35);
        font = new Font("Arial", Font.PLAIN, 22);
    }

    public void update() {
        background.update();
    }

    public void draw(Graphics2D g) {

        background.draw(g);
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        g.drawString(title, 150, 100);

        g.setFont(font);

        for (int i = 0; i < options.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 200, 340 + i * 25);
        }

        g.setColor(Color.BLACK);
        g.drawString(author, 150, GamePanel.HEIGHT - 20);


    }

    public void keyPressed(int k) {
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
