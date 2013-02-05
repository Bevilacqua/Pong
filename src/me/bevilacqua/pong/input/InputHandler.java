package me.bevilacqua.pong.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
	
	private boolean W , UP , S , DOWN , LEFT , RIGHT;
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) this.W = true; 
		if(e.getKeyCode() == KeyEvent.VK_UP) this.UP = true;
		if(e.getKeyCode() == KeyEvent.VK_S) this.S = true;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) this.DOWN = true;
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) this.W = false; 
		if(e.getKeyCode() == KeyEvent.VK_UP) this.UP = false;
		if(e.getKeyCode() == KeyEvent.VK_S) this.S = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) this.DOWN = false;
	}
	
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public boolean getUp() {
		return this.UP;
	}
	
	public boolean getDown() {
		return this.DOWN;
	}
	
	public boolean getW() {
		return this.W;
	}
	
	public boolean getS() {
		return this.S;
	}

	public boolean getLEFT() {
		return LEFT;
	}

	public boolean getRIGHT() {
		return RIGHT;
	}

}