// Name: Nima Pourjafar
// Date: June 18th 2023
// Description: This is flappy AI, a game where you can train an AI to play flappy bird.

package Game;

import javax.swing.JFrame;

// Main class that runs the game
public class Game {
    
    /*
     * Main method that runs the game.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // create a new JFrame window
        JFrame window = new JFrame("Flappy AI");

        // set the content pane to be the game panel
        window.setContentPane(new GamePanel());
    
        // set the window to be visible
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);

    }
}
