package gui;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

import core.GLRenderableObject;
import core.Rectangle;

public class Screen implements GLRenderableObject {

	private GLRenderableObject[][] layers;
	private Rectangle screenSize;
	private Rectangle renderRect;
	private Point screenOffset;

	public Screen(Rectangle screenSize)
	{
		this.screenSize = screenSize;
		this.screenOffset = new Point(0, 0);
	}

	public Screen(int x, int y, int width, int height)
	{
		this(new Rectangle(x, y, width, height));
	}

	public void addDrawableToLayer(GLRenderableObject ro, int zIndex)
	{
		// If there are no layers at all
		if (this.layers == null) {
			this.layers = new GLRenderableObject[zIndex + 1][];
		}
		// If there is no layer at the zIndex
		else if (this.layers.length <= zIndex) {
			GLRenderableObject[][] newLayers = new GLRenderableObject[zIndex + 1][];

			for (int i = 0; i < this.layers.length; i++) {
				newLayers[i] = this.layers[i];
			}
			this.layers = newLayers;
		}

		// If there is nothing in the layer at the zIndex
		if (this.layers[zIndex] == null) {
			this.layers[zIndex] = new GLRenderableObject[5];
		}
		int firstEmoty = -1;
		for (int i = 0; i < this.layers[zIndex].length; i++) {
			if (this.layers[zIndex][i] == null) {
				firstEmoty = i;
				i = this.layers[zIndex].length;
			}
		}
		if (firstEmoty == -1) {
			GLRenderableObject[] newArray = new GLRenderableObject[this.layers[zIndex].length + 5];
			for (int i = 0; i < this.layers[zIndex].length; i++) {
				newArray[i] = this.layers[zIndex][i];
			}
			firstEmoty = this.layers[zIndex].length;
			this.layers[zIndex] = newArray;
		}

		this.layers[zIndex][firstEmoty] = ro;
	}

	public boolean removeDrawableFromLayer(GLRenderableObject ro)
	{
		// if (ro == null)
		// return false;
		// if (this.layers == null)
		// return false;
		//
		// for (int i = 0; i < this.layers.length; i++) {
		// if (this.layers[i].remove(ro))
		// return true;
		// }
		return false;
	}

	public void setViewPort(Point p)
	{
		this.screenOffset.setLocation(p.x - this.screenSize.getWidth() / 2, p.y
				- this.screenSize.getHeight() / 2);
	}

	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		if (layers == null)
			return;

		if (this.screenSize == null)
			System.out.println("Screen size is null.");

		if (renderRect == null)
			this.renderRect = new Rectangle(0, 0, 0, 0);

		for (int i = 0; i < layers.length; i++) {
			GLRenderableObject[] tmpLayer = layers[i];
			if (tmpLayer != null) {
				for (GLRenderableObject glR : tmpLayer) {
					if (glR != null) {
						glR.update(16);
						if (glR.getBounds() != null) {

							int x = glR.getBounds().getX();
							int y = glR.getBounds().getY();
							int width = glR.getBounds().getWidth();
							int height = glR.getBounds().getHeight();

							this.renderRect.setX(x - this.screenOffset.x);
							this.renderRect.setY(y - this.screenSize.getY() / 2
									- this.screenOffset.y);
							this.renderRect.setWidth(width);
							this.renderRect.setHeight(height);

							glR.render(this.renderRect, this.screenSize, canvas);
						}
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
