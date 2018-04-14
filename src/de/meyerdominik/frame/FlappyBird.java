package de.meyerdominik.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import de.meyerdominik.api.Bird;
import de.meyerdominik.api.Pipe;
import de.meyerdominik.api.Renderer;

public class FlappyBird implements ActionListener, KeyListener{
	
	// Hauptframe
	public final int WIDTH 	= 1200;
	public final int HEIGHT 	= 800;
	protected final int AMOUNT_POPULATION = 1;
	
	protected int ticks = 0;
	protected int speed = 10;
	
	protected JFrame jmf;
	//protected Bird bird;
	public ArrayList<Bird> savedbirds;
	public ArrayList<Bird> birds;
	protected ArrayList<Pipe> pipes;
	protected Renderer renderer;
	protected Random rand;
	protected Timer timer;

	
	public FlappyBird() {
		start();
	}

	private void start() {
		jmf 		= new JFrame();
		pipes 		= new ArrayList<>(); 
		savedbirds	= new ArrayList<>();
		birds		= new ArrayList<>();
		renderer 	= new Renderer();
		rand		= new Random();
		timer 		= new Timer(20, this);
		
		jmf.setTitle("FlappyBird");
		// Exit on Close
		jmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jmf.setSize(WIDTH, HEIGHT);
		jmf.setResizable(false);
		jmf.add(renderer);
		jmf.addKeyListener(this);
		jmf.setVisible(true);
		
		createFirstPopulatoion();
		
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		
		timer.start();
	}
	
	private void createFirstPopulatoion() {
		for(int i = 0; i < AMOUNT_POPULATION; i++) {
			birds.add(new Bird(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20));
		}
		
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		for (Pipe pipe : pipes)
		{
			paintColumn(g, pipe);
		}
		
		g.setColor(Color.red);
		for(Bird bird : birds) {
			g.fillRect(bird.x, bird.y, bird.width, bird.height);
		}

	}

	private void paintColumn(Graphics g, Pipe pipe) {
		g.setColor(Color.green.darker());
		g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
	}
	
	public void addPipe(boolean start)
	{
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start)
		{
			pipes.add(new Pipe(WIDTH + width + pipes.size() * 300, HEIGHT - height - 120, width, height));
			pipes.add(new Pipe(WIDTH + width + (pipes.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else
		{
			pipes.add(new Pipe(pipes.get(pipes.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			pipes.add(new Pipe(pipes.get(pipes.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	// JEDER TICK
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ticks++;
		
		// Neuer Satz?
		if(allGameOver()) {
			reset();
		}
		
		for (int i = 0; i < pipes.size(); i++)
		{
			Pipe pipe = pipes.get(i);

			pipe.x -= speed;
		}
		
		// Neue Pipes
		for (int i = 0; i < pipes.size(); i++)
		{
			Pipe pipe = pipes.get(i);

			if (pipe.x + pipe.width < 0)
			{
				pipes.remove(pipe);

				if (pipe.y == 0)
				{
					addPipe(false);
				}
			}
		}
		
		for(Bird bird : birds) {
			// Y-Motion FlappyBird
			if (ticks % 2 == 0 && bird.yMotion < 15)
			{
				bird.yMotion += 2;
			}
			
	
			
			bird.y += bird.yMotion;
			
			// GameOver?
			for (Pipe pipe : pipes)
			{
				if (pipe.y == 0 && bird.x + bird.width / 2 > pipe.x + pipe.width / 2 - 10 && bird.x + bird.width / 2 < pipe.x + pipe.width / 2 + 10)
				{
					bird.score++;
				}
	
				if (pipe.intersects(bird))
				{
					bird.gameOver = true;
	
					if (bird.x <= pipe.x)
					{
						bird.x = pipe.x - bird.width;
	
					}
					else
					{
						if (pipe.y != 0)
						{
							bird.y = pipe.y - bird.height;
						}
						else if (bird.y < pipe.height)
						{
							bird.y = pipe.height;
						}
					}
				}
			}
			
			if (bird.y > HEIGHT - 120 || bird.y < 0)
			{
				bird.gameOver = true;
			}
	
			if (bird.y + bird.yMotion >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				bird.gameOver = true;
			}
			
			bird.think(pipes);
	}
		
		renderer.repaint();
	}
	

	private void reset() {
		pipes.clear();
		
		addPipe(true);
		addPipe(true);
		addPipe(true);
		addPipe(true);
		
		for(Bird bird : birds) {
			savedbirds.add(bird);
		}
		birds.clear();
		
		try {
			newPopulation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void newPopulation() throws Exception {
		calculateFitness();
		Bird best = pickBest();
		best.brain.updateWeights(11.9);
		best.gameOver = false;
		for(int i = 0; i < AMOUNT_POPULATION; i++) {
			birds.add(best);
		}
		
		
	}

	private Bird pickBest() {
		Bird best = getRandomSavedBird();
		for(Bird bird : savedbirds) {
			if(bird.fitness >= best.fitness) {
				best = bird;
			}
		}
		return best;
	}

	private Bird getRandomSavedBird() {
		return savedbirds.get(rand.nextInt(savedbirds.size()));
	}

	private void calculateFitness() {
		int sum = 1;
		for(Bird bird : savedbirds) {
			sum +=bird.score;
		}
		
		for(Bird bird : savedbirds) {
			bird.fitness = bird.score / sum;
		}
		
	}

	private boolean allGameOver() {
		boolean re = true;
		for(Bird bird : birds) {
			if(!bird.gameOver) {
				re = false;
			}
		}
		return re;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			for(Bird bird : birds) {
				bird.jump();
			}
		}
		
	}
	
}
