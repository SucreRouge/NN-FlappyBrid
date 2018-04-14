package de.meyerdominik.api;

import java.awt.Rectangle;
import java.util.ArrayList;

import de.meyerdominik.api.nn.Network;
import de.meyerdominik.main.Main;

public class Bird extends Rectangle{

	public int score = 0;
	public int yMotion = 0;
	public int fitness = 0;
	public boolean gameOver = false;
	public Network brain;
	
	private static final long serialVersionUID = -5275986007573639520L;
	
	public Bird(int x, int y, int width, int height) {
		super(x, y, width, height);
		// IN 1) y pos. of bird 2) x of pipe 3) y of upper pipe 4) y of lower pipe 
		brain = new Network(4,1);
	}
	
	public Bird(int x, int y, int width, int height, Network nn) {
		super(x, y, width, height);
		// IN 1) y pos. of bird 2) x of pipe 3) y of upper pipe 4) y of lower pipe 
		brain = nn;
		nn.mutate(Main.game.MUTATE_RATE);
	}
	
	public void jump()
	{

		if (!gameOver){
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}
	
	public void think(ArrayList<Pipe> pipes) {
		if(gameOver) { return; }
		
		// Find closest pipe
		Pipe closest = null;
		int closestDistance = 9999999;
		for(Pipe pipe : pipes) {
			int distance = pipe.x - this.x;
			if(distance < closestDistance && distance > 0) {
				closest = pipe;
				closestDistance = distance;
			}
		}
		Pipe found = null;
		Pipe upper = null;
		Pipe lower = null;
		for(Pipe pipe : pipes) {
			if(pipe.x == closest.x) {
				if(found == null) {
					found = pipe;
				} else {
					if(found.y > pipe.y) {
						lower = pipe;
						upper = found;
					} else {
						lower = found;
						upper = pipe;
					}
				}
			}
		}
		
		// get Inputs
		double[] input = new  double[4];
		input[0] = (double) this.y / (double) Main.game.HEIGHT;
		input[1] = (double) upper.y / (double) Main.game.HEIGHT;
		input[2] = (double) lower.y / (double) Main.game.HEIGHT;
		input[3] = (double) closest.x / (double) Main.game.HEIGHT;
		
//		System.out.println(Arrays.toString(input));
		
		double output[] = brain.calculate(input);
//		System.out.println("OUT: " + Arrays.toString(output));
		if(output[0] > 0.5) {
			jump();
		}
		
	}
}
