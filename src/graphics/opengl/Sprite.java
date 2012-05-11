package graphics.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Color3f;

import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.texture.Texture;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import datamanagement.SharedTextures;

/**
 * A class used to draw a certain part of a texture.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Sprite implements JSONSerializable, GLRenderableObject {

	private String textureName;

	private Texture texture;
	private Rectangle rectangle;

	private Color3f color = new Color3f(1.0f, 1.0f, 1.0f);

	// ************* Constructors *************
	/**
	 * Creates a sprite from a JSON object.
	 * 
	 * @param o
	 *            The JSON object.
	 */
	public Sprite(JSONObject o) {
		this.deserialize(o);
	}

	/**
	 * Creates a sprite.
	 * 
	 * @param textureName
	 *            The name of the texture to use.
	 * @param x
	 *            The x coordinate of the sprite.
	 * @param y
	 *            The y coordinate of the sprite.
	 * @param width
	 *            The width of the sprite.
	 * @param height
	 *            The height of the sprite.
	 */
	public Sprite(String textureName, int x, int y, int width, int height) {
		this.textureName = textureName;
		this.setTexture(this.textureName);
		this.rectangle = new Rectangle(x, y, width, height);
	}

	// ************* Other *************
	public String toString() {
		return this.textureName;
	}

	// ************* Setters *************
	/**
	 * Set the texture.
	 * 
	 * The new texture name is stored and the actual texture is swapped the next
	 * time the sprite is drawn.
	 * 
	 * @param textureName
	 */
	public void setTexture(String textureName) {
		this.textureName = textureName;
		this.texture = null;
	}

	/**
	 * Set the x coordinate of the sprite.
	 * 
	 * @param x
	 *            The x coordinate.
	 */
	public void setX(int x) {
		if (this.rectangle != null)
			this.rectangle.setX(x);
	}

	/**
	 * Set the y coordinate of the sprite.
	 * 
	 * @param y
	 *            The y coordinate.
	 */
	public void setY(int y) {
		if (this.rectangle != null)
			this.rectangle.setY(y);
	}

	/**
	 * Set the width of the sprite.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setWidth(int width) {
		if (this.rectangle != null)
			this.rectangle.setWidth(width);
	}

	/**
	 * Set the height of the sprite.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setHeight(int height) {
		if (this.rectangle != null)
			this.rectangle.setHeight(height);
	}

	/**
	 * Set the bounds of the sprite.
	 * 
	 * @param r
	 *            The rectangle representing the bounds
	 */
	public void setBounds(Rectangle r) {
		this.rectangle = r;
	}

	public void setColor(float r, float g, float b) {
		this.color.x = r;
		this.color.y = g;
		this.color.z = b;
	}

	// ************* Getters *************
	/**
	 * Get the name of the texture.
	 * 
	 * @return The name of the texture.
	 */
	public String getTextureName() {
		return this.textureName;
	}

	/**
	 * Get the x coordinate of the sprite.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return this.rectangle.getX();
	}

	/**
	 * Get the y coordinate of the sprite.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return this.rectangle.getY();
	}

	/**
	 * Get the width of the sprite.
	 * 
	 * @return The width.
	 */
	public int getWidth() {
		return this.rectangle.getWidth();
	}

	/**
	 * Get the height of the sprite.
	 * 
	 * @return The height.
	 */
	public int getHeight() {
		return this.rectangle.getHeight();
	}

	@Override
	public Rectangle getBounds() {
		return this.rectangle;
	}

	// ************* Interface methods *************
	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, int zIndex) {
		if (object != null && object.intersectsRectangle(target)) {

			GL2 gl = canvas.getGL().getGL2();

			this.texture = SharedTextures.getSharedTextures().bindTexture(
					this.textureName, canvas);

			float tX1, tX2, tY1, tY2;

			if (this.texture.getMustFlipVertically()) {
				tX1 = (float) this.rectangle.getX()
						/ (float) texture.getWidth();
				tX2 = ((float) this.rectangle.getX() + (float) this.rectangle
						.getWidth()) / (float) texture.getWidth();
				tY1 = (float) this.rectangle.getY()
						/ (float) texture.getHeight();
				tY2 = ((float) this.rectangle.getY() + (float) this.rectangle
						.getHeight()) / (float) texture.getHeight();
			} else {
				tX1 = (float) this.rectangle.getX()
						/ (float) texture.getWidth();
				tX2 = ((float) this.rectangle.getX() + (float) this.rectangle
						.getWidth()) / (float) texture.getWidth();
				tY1 = ((float) texture.getHeight() - (float) this.rectangle
						.getY()) / (float) texture.getHeight();
				tY2 = ((float) texture.getHeight() - ((float) this.rectangle
						.getY() + (float) this.rectangle.getHeight()))
						/ (float) texture.getHeight();
			}

			float rX1 = (float) ((2.0f * object.getX()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rX2 = (float) (2.0f * (object.getX() + object.getWidth()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rY1 = (float) (2.0f * (object.getY() + object.getHeight()) - (float) target
					.getHeight()) / (float) target.getHeight();
			float rY2 = (float) (2.0f * object.getY() - (float) target
					.getHeight()) / (float) target.getHeight();

			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_NEAREST);
			gl.glAlphaFunc(GL2.GL_GREATER, 0.10f);

			// gl.glEnable(GL.GL_BLEND);
			// gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(this.color.x, this.color.y, this.color.z);

			gl.glTexCoord2f(tX1, tY1);
			gl.glVertex3f(rX1, -rY2, ((float) -zIndex) / 10000f);

			gl.glTexCoord2f(tX2, tY1);
			gl.glVertex3f(rX2, -rY2, ((float) -zIndex) / 10000f);

			gl.glTexCoord2f(tX2, tY2);
			gl.glVertex3f(rX2, -rY1, ((float) -zIndex) / 10000f);

			gl.glTexCoord2f(tX1, tY2);
			gl.glVertex3f(rX1, -rY1, ((float) -zIndex) / 10000f);

			gl.glEnd();
		}
	}

	@Override
	public JSONObject serialize() {
		JSONObject retObj = new JSONObject();
		try {
			retObj.put("texture", this.textureName);
			retObj.put("x", this.rectangle.getX());
			retObj.put("y", this.rectangle.getY());
			retObj.put("width", this.rectangle.getWidth());
			retObj.put("height", this.rectangle.getHeight());
		} catch (JSONException e) {
			System.out.println("Failed to serialize Sprite!");
			e.printStackTrace();
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) {
		try {
			this.textureName = o.getString("texture");
			this.texture = null;
			this.rectangle = new Rectangle(o.getInt("x"), o.getInt("y"),
					o.getInt("width"), o.getInt("height"));
		} catch (JSONException e) {
			System.out.println("Failed to deserialize Sprite!");
			e.printStackTrace();
		}
	}

	@Override
	public void update(double dt) {
	}
}