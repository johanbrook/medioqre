package graphics.opengl;

import factory.ObjectFactory;

import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;

import core.GLRenderableObject;
import core.Rectangle;

/**
 * A class used for drawing on screen.
 * 
 * It supports multiple layers and its viewport can be moved around for
 * rendering a certain part of the scene.
 * 
 * @author John Barbero Unenge
 * 
 */
public class GLScreen implements GLRenderableObject {

	private GLRenderableObject[][] layers;
	private Rectangle screenSize;
	private Rectangle renderRect;
	private Point screenOffset;

	// TODO Remove this!
	private float angle = 0;

	/**
	 * Creates a screen with the given screensize.
	 * 
	 * @param screenSize
	 *            The screensize
	 */
	public GLScreen(Rectangle screenSize) {
		this.screenSize = screenSize;
		this.screenOffset = new Point(0, 0);
	}

	/**
	 * Creates a screen with the given screensize.
	 * 
	 * @param x
	 *            The x position of the screen
	 * @param y
	 *            The y position of the screen
	 * @param width
	 *            The width of the screen
	 * @param height
	 *            The height of the screen
	 */
	public GLScreen(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}

	/**
	 * Adds a GLRenderableObject to the layer at the given z index.
	 * 
	 * @param ro
	 *            GLRenderableObject to add to the layer
	 * @param zIndex
	 *            The zIndex to add it to
	 */
	public void addDrawableToLayer(GLRenderableObject ro, int zIndex) {
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

	/**
	 * Remove a GLRenderableObject from the screen.
	 * 
	 * @param ro
	 *            The GLRenderableObject to remove
	 * @return A boolean representing if the GLRenderableObject was found or not
	 */
	public boolean removeDrawableFromLayer(GLRenderableObject ro) {
		boolean retVal = false;
		if (ro == null || this.layers == null)
			return retVal;

		for (int i = 0; i < this.layers.length; i++) {
			for (int j = 0; j < this.layers[i].length; j++) {
				if (this.layers[i][j] == ro) {
					this.layers[i][j] = null;
					retVal = true;
				}
			}
		}
		return retVal;
	}

	/**
	 * Set the viewport of the screen a given point.
	 * 
	 * @param p
	 *            The point to set the viewport
	 */
	public void setViewPort(Point p) {
		this.screenOffset.setLocation(p.x - this.screenSize.getWidth() / 2, p.y
				- this.screenSize.getHeight() / 2);
	}

	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {

		GL2 gl = canvas.getGL().getGL2();

		if (layers == null)
			return;

		if (this.screenSize == null)
			System.out.println("Screen size is null.");

		if (renderRect == null)
			this.renderRect = new Rectangle(0, 0, 0, 0);

		for (int i = layers.length - 1; i >= 0; i--) {
			GLRenderableObject[] tmpLayer = layers[i];
			if (tmpLayer != null) {
				for (GLRenderableObject glR : tmpLayer) {
					if (glR != null) {
						glR.update(16);
						if (glR.getBounds() != null) {

							float x = glR.getBounds().getX();
							float y = glR.getBounds().getY();
							float width = glR.getBounds().getWidth();
							float height = glR.getBounds().getHeight();

							this.renderRect.setX(x - this.screenOffset.x);
							this.renderRect.setY(y - this.screenSize.getY() / 2
									- this.screenOffset.y);
							this.renderRect.setWidth(width);
							this.renderRect.setHeight(height);

							glR.render(this.renderRect, this.screenSize,
									canvas, zIndex);
							if (glR instanceof Actor) {
								Actor a = (Actor) glR;
								if (a.isShowingCollisionBox() && a.getCollidableObject() != null) {
									float rX1, rX2, rY1, rY2;
									rX1 = (2f * ((float) a.getCollidableObject().getCollisionBox().x - (float) this.screenOffset.getX()) - (float) this.screenSize.getWidth()) / (float) this.screenSize.getWidth();
									rX2 = (2f * ((float) a.getCollidableObject().getCollisionBox().x + (float) a.getCollidableObject().getCollisionBox().width - (float) this.screenOffset.getX()) - (float) this.screenSize.getWidth()) / (float) this.screenSize.getWidth();
									rY1 = (2f * ((float) a.getCollidableObject().getCollisionBox().y - (float) this.screenOffset.getY()) - (float) this.screenSize.getHeight()) / (float) this.screenSize.getHeight();
									rY2 = (2f * ((float) a.getCollidableObject().getCollisionBox().y + (float) a.getCollidableObject().getCollisionBox().height - (float) this.screenOffset.getY()) - (float) this.screenSize.getHeight()) / (float) this.screenSize.getHeight();
									float zInd = (-(float)a.getCollidableObject().getCollisionBox().getY() - (float)a.getCollidableObject().getCollisionBox().getHeight()) / 1000000f - 0.01f;
									
									gl.glDisable(GL.GL_TEXTURE_2D);
									
									gl.glBegin(GL2.GL_QUADS);
									gl.glColor4f(1.0f, 0.0f, 1.0f, 0.5f);
									gl.glVertex3f(rX1, -rY2, zInd);
									gl.glVertex3f(rX2, -rY2, zInd);
									gl.glVertex3f(rX2, -rY1, zInd);
									gl.glVertex3f(rX1, -rY1, zInd);
									gl.glEnd();
									gl.glEnable(GL.GL_TEXTURE_2D);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public Rectangle getBounds() {
		return this.screenSize;
	}
}

