package de.meyerdominik.main;


import de.meyerdominik.frame.FlappyBird;
import de.meyerdominik.frame.Hilfe;

public class Main {

	public static FlappyBird game;
	public static Hilfe hilfe; 

	
	public static void main(String[] args) throws Exception {
		game = new FlappyBird();
		hilfe = new Hilfe();
		
	}

	public static void newGame() {
		game.timer.stop();
		game.jmf.setVisible(false);
		game.jmf.dispose();
		game = null;
		game = new FlappyBird();
		
	}

}
