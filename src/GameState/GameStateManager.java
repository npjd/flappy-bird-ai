package GameState;

import java.util.ArrayList;

import Game.GamePanel;

import java.awt.Graphics2D;
// Game state manager

public class GameStateManager {

    // array list of game states
    private ArrayList<GameState> gameStates;
    // index of current state
    private int currentState;
    // game panel
    private GamePanel gamePanel;

    /*
     * constructor for game state manager
     * 
     * @param gamePanel
     * 
     */
    public GameStateManager(GamePanel gamePanel) {
        // initialize game states array list
        gameStates = new ArrayList<GameState>();

        // set game panel
        this.gamePanel = gamePanel;

        // set current state to 0
        currentState = 0;

        // add all states
        gameStates.add(new MenuState(this));
        gameStates.add(new PlayingState(this));
        gameStates.add(new TrainingState(this));
        gameStates.add(new GodBirdState(this));
    }

    /*
     * Getter for game panel
     * 
     * @return game panel
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /*
     * Setter for current and changes state
     * 
     * @param state
     * 
     * @return void
     */
    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    /*
     * Update current state
     * 
     */
    public void update() {
        gameStates.get(currentState).update();
    }

    /*
     * Draw current state
     */
    public void draw(Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }

    /*
     * Key pressed for current state
     */
    public void keyPressed(int k) {
        gameStates.get(currentState).keyPressed(k);
    }

    /*
     * Key pressed for current state
     */
    public void keyReleased(int k) {
        gameStates.get(currentState).keyReleased(k);
    }

}
