package gui;


import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import constants.Direction;


public class Actor {

	private Point position;
	private Bitmap[] animationFrames;
	private Bitmap currentFrame;
	
	public Actor(Point startingPosition) {
		this.position = new Point(startingPosition);
		
		animationFrames = new Bitmap[8];
		try {
//			BufferedImage img0 = ImageIO.read(new File("rec/images/sprites/frank/frank_left.png"));
//			animationFrames[0] = new Bitmap(img0.getWidth(), img0.getHeight());
//			animationFrames[0].pixels = BitmapTool.getARGBarrayFromDataBuffer(img0.getRaster(), img0.getWidth(), img0.getHeight());
//			
//			BufferedImage img1 = ImageIO.read(new File("rec/images/sprites/frank/frank_right.png"));
//			animationFrames[1] = new Bitmap(img1.getWidth(), img1.getHeight());
//			animationFrames[1].pixels = BitmapTool.getARGBarrayFromDataBuffer(img1.getRaster(), img1.getWidth(), img1.getHeight());
//			
//			BufferedImage img2 = ImageIO.read(new File("rec/images/sprites/frank/frank_up.png"));
//			animationFrames[2] = new Bitmap(img2.getWidth(), img2.getHeight());
//			animationFrames[2].pixels = BitmapTool.getARGBarrayFromDataBuffer(img2.getRaster(), img2.getWidth(), img2.getHeight());
//			
//			BufferedImage img3 = ImageIO.read(new File("rec/images/sprites/frank/frank_down.png"));
//			animationFrames[3] = new Bitmap(img3.getWidth(), img3.getHeight());
//			animationFrames[3].pixels = BitmapTool.getARGBarrayFromDataBuffer(img3.getRaster(), img3.getWidth(), img3.getHeight());
			
			BufferedImage img0 = ImageIO.read(new File("rec/images/sprites/frank/f_n.png"));
			animationFrames[0] = new Bitmap(img0.getWidth(), img0.getHeight());
			animationFrames[0].pixels = BitmapTool.getARGBarrayFromDataBuffer(img0.getRaster(), img0.getWidth(), img0.getHeight());
			
			BufferedImage img1 = ImageIO.read(new File("rec/images/sprites/frank/f_nea.png"));
			animationFrames[1] = new Bitmap(img1.getWidth(), img1.getHeight());
			animationFrames[1].pixels = BitmapTool.getARGBarrayFromDataBuffer(img1.getRaster(), img1.getWidth(), img1.getHeight());
			
			BufferedImage img2 = ImageIO.read(new File("rec/images/sprites/frank/f_ea.png"));
			animationFrames[2] = new Bitmap(img2.getWidth(), img2.getHeight());
			animationFrames[2].pixels = BitmapTool.getARGBarrayFromDataBuffer(img2.getRaster(), img2.getWidth(), img2.getHeight());
			
			BufferedImage img3 = ImageIO.read(new File("rec/images/sprites/frank/f_sea.png"));
			animationFrames[3] = new Bitmap(img3.getWidth(), img3.getHeight());
			animationFrames[3].pixels = BitmapTool.getARGBarrayFromDataBuffer(img3.getRaster(), img3.getWidth(), img3.getHeight());
			
			BufferedImage img4 = ImageIO.read(new File("rec/images/sprites/frank/f_s.png"));
			animationFrames[4] = new Bitmap(img4.getWidth(), img4.getHeight());
			animationFrames[4].pixels = BitmapTool.getARGBarrayFromDataBuffer(img4.getRaster(), img4.getWidth(), img4.getHeight());
			
			BufferedImage img5 = ImageIO.read(new File("rec/images/sprites/frank/f_sw.png"));
			animationFrames[5] = new Bitmap(img5.getWidth(), img5.getHeight());
			animationFrames[5].pixels = BitmapTool.getARGBarrayFromDataBuffer(img5.getRaster(), img5.getWidth(), img5.getHeight());
			
			BufferedImage img6 = ImageIO.read(new File("rec/images/sprites/frank/f_w.png"));
			animationFrames[6] = new Bitmap(img6.getWidth(), img6.getHeight());
			animationFrames[6].pixels = BitmapTool.getARGBarrayFromDataBuffer(img6.getRaster(), img6.getWidth(), img6.getHeight());
			
			BufferedImage img7 = ImageIO.read(new File("rec/images/sprites/frank/f_nw.png"));
			animationFrames[7] = new Bitmap(img7.getWidth(), img7.getHeight());
			animationFrames[7].pixels = BitmapTool.getARGBarrayFromDataBuffer(img7.getRaster(), img7.getWidth(), img7.getHeight());
			
			
			currentFrame = animationFrames[4];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	public Bitmap getCurrentFrame() {
		System.out.println("Get frame.");
		return currentFrame;
	}
	
	
	public Point getPosition() {
		return this.position;
	}
	public void setDirection(Direction direction)
	{
		switch(direction) {
		case NORTH :
			currentFrame = animationFrames[0];
			break;
		case NORTH_EAST :
			currentFrame = animationFrames[1];
			break;
		case EAST :
			currentFrame = animationFrames[2];
			break;
		case SOUTH_EAST :
			currentFrame = animationFrames[3];
			break;
		case SOUTH :
			currentFrame = animationFrames[4];
			break;
		case SOUTH_WEST :
			currentFrame = animationFrames[5];
			break;
		case WEST :
			currentFrame = animationFrames[6];
			break;
		case NORTH_WEST :
			currentFrame = animationFrames[7];
			break;
		}
	}
	
	public void setPosition(Point pos) {
		this.position = pos;
	}
}
