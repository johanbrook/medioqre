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
			System.out.println("In here!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		animationFrames[0] = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
		animationFrames[0].getGraphics().setColor(Color.BLUE);
		animationFrames[0].getGraphics().fillRect(0, 0, 32, 64);
		animationFrames[1] = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
		animationFrames[1].getGraphics().setColor(Color.GREEN);
		animationFrames[1].getGraphics().fillRect(0, 0, 32, 64);
		animationFrames[2] = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
		animationFrames[2].getGraphics().setColor(Color.RED);
		animationFrames[2].getGraphics().fillRect(0, 0, 32, 64);
		animationFrames[3] = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
		animationFrames[3].getGraphics().setColor(Color.YELLOW);
		animationFrames[3].getGraphics().fillRect(0, 0, 32, 64);
		
		currentFrame = animationFrames[3];
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
