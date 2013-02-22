package me.bevilacqua.pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import me.bevilacqua.pong.input.InputHandler;
import me.bevilacqua.pong.player.Player;

public class MainGame extends Canvas implements Runnable {

	public static final  int WIDTH = 420;
	public static final  int HEIGHT = WIDTH / 16 * 9;
	public static final int SCALE  = 2; 
	private static final String TITLE = "Pong";
	public static BufferedImage image = new BufferedImage(WIDTH , HEIGHT , BufferedImage.TYPE_INT_RGB); //The image the game runs on but you cant edit it without a raster
//	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); //Converts the buffered image into an array of integers to hold pixel data 
	private static final long serialVersionUID = 1L;
	private boolean running = false;
	
	private JFrame frame;
	private InputHandler input;
	private static Player player1;
	private static Player player2;
	private Random random = new Random();
	
	private Dimension size = new Dimension(WIDTH * SCALE , HEIGHT * SCALE);
		
	private int ballX = 39 , ballY = (HEIGHT * SCALE / 2) + 24;
	boolean bleft , bright , bup , bdown; //ball directions
	private static String p1Name;
	private static String p2Name;
	private static boolean isPlaying;
	
	MainGame() {
		setSize(size);
		setPreferredSize(size);
		
		input = new InputHandler();
		player1 = new Player();
		player2 = new Player();
		frame = new JFrame();
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(TITLE);
		
		player1.setScore(-1); //Ball starts at player 1 paddle giving them a free point this removes that
		
		addKeyListener(input);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		
		isPlaying = true;
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		requestFocus();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;
			while (delta >= 1) {
				shouldRender = true;
				ticks++;
				tick();
				delta--;
			}
			
			if(shouldRender) {
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				frame.setTitle(TITLE + " | Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private void render() {
		
		String WinMessage = " has won!";
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		if(isPlaying) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
			g.setColor(Color.yellow);
			g.drawString(player1.getScore() + " - " + player2.getScore(), WIDTH * SCALE / 2, 10);
			g.setColor(Color.white);
			g.drawString(p1Name, 10 , player1.getY() + 30);
			g.fillRect(30, player1.getY(), 10, 50);
			g.drawString(p2Name , (WIDTH * SCALE - 15) , player2.getY() + 30);
			g.fillRect(WIDTH * SCALE - 30 , player2.getY(), 10 , 50);
			g.fillOval(ballX, ballY, 5 , 5);
		}
		else {
			String winner = (player1.getScore() > player2.getScore() ? p1Name : p2Name);
			g.setColor(Color.white);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
			g.drawString(winner + WinMessage, WIDTH * SCALE / 2 + 2, HEIGHT * SCALE / 2);
			g.drawString("Press F8 to Exit!", WIDTH * SCALE / 2 , HEIGHT * SCALE / 2 + 16);

		}
		
		g.fillRect(0, 435, 1000, 3);
		g.drawString("Created by: Jacob Bevilacqua                                       Controls:  " + p1Name + ": W/S    " + p2Name + ": ArrowKeys     |F8 TO EXIT|", 25, 465);
		g.dispose();
		bs.show();
	}

	private void tick() {
		ballTick();
		if(player1.getY() > 0) if (input.getW() == true) player1.setY(player1.getY() - 2); 
		if(player1.getY() < 385)  if (input.getS() == true) player1.setY(player1.getY() + 2);
		
		if(player2.getY() > 0) if (input.getUp() == true) player2.setY(player2.getY() - 2); 
		if(player2.getY() < 385)	 if (input.getDown() == true) player2.setY(player2.getY() + 2);
		System.out.println(ballX + " " + ballY + " up: " + bup + " down: " + bdown);
		if(player1.getScore() >= 20 || player2.getScore() >= 20) isPlaying = false; 
		System.out.println(player1.getScore() + " " + isPlaying);
	}

	private void ballTick() {
		
//RULE 1:Ball Hits Left Barrier
		if(ballX <= 39) {
//			System.out.println(ballY + " " + player1.getY() + " " + (player1.getY() + 50));
			if(ballY >= player1.getY() && ballY <= (player1.getY() + 50) || ballY == player1.getY()) {
				player1.setScore(player1.getScore() + 1);
				ballX++;
				ballY++;
				bright = true; bleft = false;
				if(player1.getY() >= 215) {
					bup = false; bdown = true;
				}
				else if(player1.getY() <= 215) {
					bup = true; bdown = false;
				}
			}
			else {
				player2.setScore(player2.getScore() + 4);
				player1.setY(HEIGHT * SCALE / 2);
				player2.setY(HEIGHT * SCALE / 2);
				ballX = 39;
				ballY = (HEIGHT * SCALE / 2) + 24;
			}
		}
		
//Rule 1:Ball Hits Right barrier		
		else if(ballX >= 805) {
			if(ballY >= player2.getY() && ballY <= (player2.getY() + 50) || ballY == player2.getY()) {
				player2.setScore(player2.getScore() + 1);
				ballX--;
				ballY++;
				bright = false; bleft = true;
				if(player2.getY() >= 215){
					bup = false; bdown = true;
				}
				else if(player2.getY() <= 215){
					bup = true; bdown = false;
				}
			}	
			else {
				player1.setScore(player1.getScore() + 4);
				player1.setY(HEIGHT * SCALE / 2);
				player2.setY(HEIGHT * SCALE / 2);
				ballX = 39;
				ballY = (HEIGHT * SCALE / 2) + 24;
			}
		}
//Rule 2:Ball hit floor
		else if(ballY >= 430) {
			bup = true; bdown = false;
			if(bleft) ballX--; 
			if(bright) ballX++;
			ballY--;
		}
//Rule 3:Ball hit ceiling
		else if(ballY <= 0) {
			bup = false; bdown = true;
			if(bleft) ballX--; 
			if(bright) ballX++;
			ballY++;
		}
//Regular movement
		else {
			if(bup) ballY -= 3 - random.nextInt(3);
			if(bdown) ballY += 2 + random.nextInt(3);
			if(bleft) ballX -= 3;
			if(bright)ballX += 3;
		}
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}
	
	public static void main(String args[]) {	
		p1Name = JOptionPane.showInputDialog("Player 1 Enter Your Initials   ");
		p2Name = JOptionPane.showInputDialog("Player 2 Enter Your Initials   ");
		p1Name = p1Name.substring(0, 2);
		p2Name = p2Name.substring(0, 2);

		new MainGame().start();
	}
	
}