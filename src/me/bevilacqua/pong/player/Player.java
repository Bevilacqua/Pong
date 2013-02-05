package me.bevilacqua.pong.player;

import me.bevilacqua.pong.MainGame;

public class Player {
	private int y = MainGame.HEIGHT * MainGame.SCALE / 2;
	private int score;
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
