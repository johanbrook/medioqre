package gui;

import java.util.Collection;
import java.util.LinkedList;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

import core.GLRenderableObject;
import core.Rectangle;

public class Screen implements GLRenderableObject {

	private LinkedList<GLRenderableObject>[] layers;
	private Rectangle screenSize;

	public Screen(Rectangle screenSize)
	{
		this.screenSize = screenSize;
	}

	public Screen(int x, int y, int width, int height)
	{
		this(new Rectangle(x, y, width, height));
	}

	public void addDrawableToLayer(GLRenderableObject ro, int zIndex)
	{
		if (this.layers == null) {
			this.layers = new LinkedList[zIndex + 1];
		} else if (this.layers.length < zIndex) {
			LinkedList[] newLayers = new LinkedList[zIndex];
			for (int i = 0; i < this.layers.length; i++) {
				newLayers[i] = this.layers[i];
			}
		}
		if (this.layers[zIndex] == null) {
			this.layers[zIndex] = new LinkedList<GLRenderableObject>();
		}
		this.layers[zIndex].add(ro);
	}

	public boolean removeDrawableToLayer(GLRenderableObject ro)
	{
		if (ro == null)
			return false;
		if (this.layers == null)
			return false;

		for (int i = 0; i < this.layers.length; i++) {
			if (this.layers[i].remove(ro))
				return true;
		}
		return false;
	}

	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		if (layers == null)
			return;

		for (int i = 0; i < layers.length; i++) {
			Collection<GLRenderableObject> tmpLayer = layers[i];
			if (tmpLayer != null) {
				for (GLRenderableObject glR : tmpLayer) {
					if (glR != null) {
						glR.update(16);
						glR.render(glR.getBounds(), this.screenSize, canvas);
					}
				}
			}
		}
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public Rectangle getBounds()
	{
		return this.screenSize;
	}

}
