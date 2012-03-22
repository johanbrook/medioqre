package gui;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import constants.Direction;

import model.Position;

public class Actor {

	private Position position;
	private BufferedImage[] animationFrames;
	private BufferedImage currentFrame;
	
	public Actor(Position startingPosition) {
		this.position = new Position(startingPosition);
		
		animationFrames = new BufferedImage[4];
		try {
			animationFrames[0] = ImageIO.read(new File("rec/images/sprites/frank/frank_left.png"));
			animationFrames[1] = ImageIO.read(new File("rec/images/sprites/frank/frank_right.png"));
			animationFrames[2] = ImageIO.read(new File("rec/images/sprites/frank/frank_up.png"));
			animationFrames[3] = ImageIO.read(new File("rec/images/sprites/frank/frank_down.png"));
		
			currentFrame = animationFrames[3];
			
			for (BufferedImage i : animationFrames) {
				System.out.println(i.toString());
			}
		
			
			System.out.println("In here!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public BufferedImage getCurrentImage() {
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
