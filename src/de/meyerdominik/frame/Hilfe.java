package de.meyerdominik.frame;

import javax.swing.JFrame;
import javax.swing.SpringLayout;

import de.meyerdominik.main.Main;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Hilfe extends JFrame{
	

	private static final long serialVersionUID = 2620349721118797353L;
	
	public ArrayList<double[][][]> weights;

	public Hilfe() {
		setTitle("Hilfe");
		setSize(200,100);
		setResizable(false);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.newGame();
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnRestart, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnRestart, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnRestart);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
