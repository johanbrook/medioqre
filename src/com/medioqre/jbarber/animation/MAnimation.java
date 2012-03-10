package com.medioqre.jbarber.animation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

public class MAnimation {

	// Time
	private double animationDuration;
	private double currentAnimationTime;
	
	// Image
	private BufferedImage image;
	private BufferedImage[] frames;
	private boolean shouldAnimate = false;
	public boolean shouldLoop = false;
	
	public MAnimation(String imageURL, Dimension spriteSize, double durationMillis, boolean shouldLoop)
	{
		this.animationDuration = durationMillis;
		try {
			File f = new File(imageURL);
			this.image = ImageIO.read(new File(f.getAbsolutePath()));
		} catch (IIOException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.frames = new BufferedImage[(int) (this.image.getWidth()/spriteSize.getWidth())];
		for (int i = 0; i < this.image.getWidth()/spriteSize.getWidth(); i++) {
			this.frames[i] = this.image.getSubimage(i*spriteSize.width, 0, spriteSize.width, spriteSize.height);
		}
		
		this.shouldLoop = shouldLoop;
		
		this.startAnimation();
	}
	public void startAnimation()
	{
		this.currentAnimationTime = 0;
		this.shouldAnimate = true;
	}
	public void stopAnimation()
	{
		this.shouldAnimate = false;
	}
	public Image getFrame(double dt)
	{
		if (this.shouldAnimate) {
			this.currentAnimationTime += dt;
			
			if (this.currentAnimationTime > this.animationDuration) {
				if (!shouldLoop) {
					this.stopAnimation();
				} else {
					this.startAnimation();
				}
			} else {
				return this.frames[(int) ((this.currentAnimationTime / this.animationDuration) * this.frames.length)];
			}
		}
		return this.frames[0];
	}
}
