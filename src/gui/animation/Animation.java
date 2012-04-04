package gui.animation;

import graphics.bitmap.Bitmap;

public class Animation {

	private String name;
	private double duration;
	private double timePassed;
	private Bitmap[] frames;
	private Bitmap currentFrame;
	
	public Animation(String name, double duration, Bitmap[] frames)
	{
		this.name = name;
		this.duration = duration;
		this.frames = frames;
		this.currentFrame = frames[0];
	}
	
	public void updateAnimation(double dt)
	{
		this.timePassed += (dt / 10);
		if (this.timePassed>duration) timePassed = 0;
	}
	public Bitmap getCurrentFrame() {
		this.currentFrame = frames[(int)(timePassed / (duration / frames.length))];
		return this.currentFrame;
	}
	public String getName()
	{
		return this.name;
	}
	public Animation clone()
	{
		Animation newAnimation = new Animation(this.name, this.duration, this.frames);
		return newAnimation;
	}
}
