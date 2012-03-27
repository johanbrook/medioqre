package gui;


import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import constants.Direction;

import model.Position;

public class Actor {

	private Position position;
	private Bitmap[] animationFrames;
	private Bitmap currentFrame;
	
	public Actor(Position startingPosition) {
		this.position = new Position(startingPosition);
		
		animationFrames = new Bitmap[4];
		try {
			BufferedImage img0 = ImageIO.read(new File("rec/images/sprites/frank/tank_left.png"));
			animationFrames[0] = new Bitmap(img0.getWidth(), img0.getHeight());
			animationFrames[0].pixels = BitmapTool.getARGBarrayFromDataBuffer(img0.getRaster(), img0.getWidth(), img0.getHeight());
			
			BufferedImage img1 = ImageIO.read(new File("rec/images/sprites/frank/tank_right.png"));
			animationFrames[1] = new Bitmap(img1.getWidth(), img1.getHeight());
			animationFrames[1].pixels = BitmapTool.getARGBarrayFromDataBuffer(img1.getRaster(), img1.getWidth(), img1.getHeight());
			
			BufferedImage img2 = ImageIO.read(new File("rec/images/sprites/frank/tank_up.png"));
			animationFrames[2] = new Bitmap(img2.getWidth(), img2.getHeight());
			animationFrames[2].pixels = BitmapTool.getARGBarrayFromDataBuffer(img2.getRaster(), img2.getWidth(), img2.getHeight());
			
			BufferedImage img3 = ImageIO.read(new File("rec/images/sprites/frank/tank_down.png"));
			animationFrames[3] = new Bitmap(img3.getWidth(), img3.getHeight());
			animationFrames[3].pixels = BitmapTool.getARGBarrayFromDataBuffer(img3.getRaster(), img3.getWidth(), img3.getHeight());
			
			
			currentFrame = animationFrames[3];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public Bitmap getCurrentFrame() {
		return currentFrame;
	}
	
	
	public Position getPosition() {
		return this.position;
	}
	public void setDirection(Direction direction)
	{
		switch(direction) {
		case WEST :
			currentFrame = animationFrames[0];
			break;
		case EAST :
			currentFrame = animationFrames[1];
			break;
		case NORTH :
			currentFrame = animationFrames[2];
			break;
		case SOUTH :
			currentFrame = animationFrames[3];
			break;
		}
	}
	
	public void setPosition(Position pos) {
		this.position = pos;
	}
}
