/**
*	AppController.java
*
*	@author Johan
*/

package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.GameModel;

public class AppController implements KeyListener {
	
	public static final int GAME_SPEED = 10;
	
	private GameModel game;
	private boolean[] keys;
	private boolean isKeyPressed = false;
	
	public AppController(){
		this.game = new GameModel();
		this.keys = new boolean[526];
	}
	
	public static void main(String[] args) {
		javax.swing.JFrame f = new javax.swing.JFrame();
		f.setPreferredSize(new java.awt.Dimension(200,200));
		f.addKeyListener(new AppController());
		f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		f.pack();
		f.setVisible(true);
	}

	private void sendKeyAction(KeyEvent evt){
		
		if(checkKey(KeyEvent.VK_W) && checkKey(KeyEvent.VK_A)) {
			System.out.println("W + A");
		}
		
		else if(checkKey(KeyEvent.VK_W) && checkKey(KeyEvent.VK_D)) {
			System.out.println("W + D");
		}
		
		else if(checkKey(KeyEvent.VK_A) && checkKey(KeyEvent.VK_S)) {
			System.out.println("A + S");
		}
		
		else if(checkKey(KeyEvent.VK_S) && checkKey(KeyEvent.VK_D)) {
			System.out.println("S + D");
		}
		
		else if(checkKey(KeyEvent.VK_A)) {
			System.out.println("A");
		}
		
		else if(checkKey(KeyEvent.VK_W)) {
			System.out.println("W");
		}
		
		else if(checkKey(KeyEvent.VK_S)) {
			System.out.println("S");
		}
		
		else if(checkKey(KeyEvent.VK_D)) {
			System.out.println("D");
		}
		
		if(this.isKeyPressed)
			System.out.println("Pressed");
		else
			System.out.println("Released");
	}
	
	private boolean checkKey(int i) {
		if(this.keys.length > i)
			return this.keys[i];
		
		return false;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		this.keys[e.getKeyCode()] = true;
		this.isKeyPressed = true;
		sendKeyAction(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys[e.getKeyCode()] = false;
		this.isKeyPressed = false;
		sendKeyAction(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	
}
