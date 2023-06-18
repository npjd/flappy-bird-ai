package GameState;

import java.awt.Graphics2D;

// abstract class for the game's various states
public abstract class GameState {

    // game state manager for current state, it's declared as protected so that sub
    // classes can access it's methods

    protected GameStateManager gsm;
   
    // various functions for game state for each individual state to extend off of

    abstract public void init();

    abstract public void update();

    abstract public void draw(Graphics2D g);

    abstract public void keyPressed(int k);

    abstract public void keyReleased(int k);
}
