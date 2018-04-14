package de.meyerdominik.api;

import java.awt.Graphics;

import javax.swing.JPanel;

import de.meyerdominik.main.Main;

public class Renderer extends JPanel {


	private static final long serialVersionUID = 1271454761093811277L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Main.game.repaint(g);
	}
}
