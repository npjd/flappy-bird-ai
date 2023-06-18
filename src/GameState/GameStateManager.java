package GameState;

import java.util.ArrayList;

import Game.GamePanel;

import java.awt.Graphics2D;

public class GameStateManager {

    private ArrayList<GameState> gameStates;
    private int currentState;
    private GamePanel gamePanel;



    public GameStateManager(GamePanel gamePanel) {
        gameStates = new ArrayList<GameState>();

        this.gamePanel = gamePanel;

        currentState = 0;

        gameStates.add(new MenuState(this));

        gameStates.add(new PlayingState(this));
        gameStates.add(new TrainingState(this));
        gameStates.add(new GodBirdState(this));

    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update() {
        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }

    public void keyPressed(int k) {
        gameStates.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates.get(currentState).keyReleased(k);
    }

}
