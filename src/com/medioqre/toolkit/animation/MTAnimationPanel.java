package com.medioqre.toolkit.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import com.medioqre.jbarber.animation.MAnimation;

@SuppressWarnings("serial")
public class MTAnimationPanel extends JPanel {
	
	private MAnimation animation;
	
	private BufferedImage offScreen;
	private Graphics g;
	
	public MTAnimationPanel()
	{
		this.setPreferredSize(new Dimension(200,200));
		
		this.offScreen = new BufferedImage(this.getPreferredSize().width, this.getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
		g = offScreen.getGraphics();
	}
	public void setAnimation(String imageURL, Dimension size, double duration, boolean shouldLoop) {
		this.animation = new MAnimation((imageURL), size, duration, shouldLoop); 
	}
	
	public void render(double dt)
	{
		if (this.animation != null) {
			Image tmpImg = this.animation.getFrame(dt);
			Graphics graphics = this.getGraphics();
			if (tmpImg != null) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImage(tmpImg, 10, 10, 180, 180, null);
				
				graphics.drawImage(this.offScreen, 0, 0, this.getWidth(), this.getHeight(), null);
			}
		}
	}
}
