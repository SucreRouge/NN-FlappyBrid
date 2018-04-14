package de.meyerdominik.api;

import java.awt.Rectangle;

public class Bird extends Rectangle{

	public int score = 0;
	public int yMotion = 0;
	
	private static final long serialVersionUID = -5275986007573639520L;
	
	public Bird(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
}
