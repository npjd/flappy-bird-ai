package GameState;

import java.util.ArrayList;
import java.awt.Graphics2D;

public class GameStateManager {
    
    private ArrayList<GameState> gameStates;
    private int currentState;

    public GameStateManager(){
        gameStates = new ArrayList<GameState>();

        currentState = 0; 

        
        
        // TODO: add some game states
    }

    public void setState(int state){
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update(){
        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D g){
        gameStates.get(currentState).draw(g);
    }

    public void keyPressed(int k){
        gameStates.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k){
        gameStates.get(currentState).keyReleased(k);
    }

}
