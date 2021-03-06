package graphics.opengl.animation;

import graphics.json.JSONSerializable;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import tools.datamanagement.SharedTextures;

import com.jogamp.opengl.util.texture.Texture;


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

	private float[] color = new float[]{1.0f, 1.0f, 1.0f};

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
	public void setX(float x) {
		if (this.rectangle != null)
			this.rectangle.setX(x);
	}

	/**
	 * Set the y coordinate of the sprite.
	 * 
	 * @param y
	 *            The y coordinate.
	 */
	public void setY(float y) {
		if (this.rectangle != null)
			this.rectangle.setY(y);
	}

	/**
	 * Set the width of the sprite.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setWidth(float width) {
		if (this.rectangle != null)
			this.rectangle.setWidth(width);
	}

	/**
	 * Set the height of the sprite.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setHeight(float height) {
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

	/**
	 * Set the color used as a mask for the Sprite.
	 * 
	 * All values should be floating point values between 0 and 1.
	 * 
	 * @param r Red
	 * @param g Green
	 * @param b Blue
	 */
	public void setColor(float r, float g, float b) {
		this.color = new float[]{r, g, b};
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
	public float getX() {
		return this.rectangle.getX();
	}

	/**
	 * Get the y coordinate of the sprite.
	 * 
	 * @return The y coordinate.
	 */
	public float getY() {
		return this.rectangle.getY();
	}

	/**
	 * Get the width of the sprite.
	 * 
	 * @return The width.
	 */
	public float getWidth() {
		return this.rectangle.getWidth();
	}

	/**
	 * Get the height of the sprite.
	 * 
	 * @return The height.
	 */
	public float getHeight() {
		return this.rectangle.getHeight();
	}

	@Override
	public Rectangle getBounds() {
		return this.rectangle;
	}

	// ************* Interface methods *************
	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {
		if (object != null && object.intersectsRectangle(target)) {

			GL2 gl = canvas.getGL().getGL2();

			this.texture = SharedTextures.getSharedTextures().bindTexture(
					this.textureName, canvas);

			float tX1, tX2, tY1, tY2;

			if (this.texture.getMustFlipVertically()) {
				tX1 = this.rectangle.getX()
						/ texture.getWidth();
				tX2 = (this.rectangle.getX() + this.rectangle
						.getWidth()) / texture.getWidth();
				tY1 = this.rectangle.getY()
						/ texture.getHeight();
				tY2 = (this.rectangle.getY() + this.rectangle
						.getHeight()) / texture.getHeight();
			} else {
				tX1 = this.rectangle.getX()
						/ texture.getWidth();
				tX2 = (this.rectangle.getX() + this.rectangle
						.getWidth()) / texture.getWidth();
				tY1 = (texture.getHeight() - this.rectangle
						.getY()) / texture.getHeight();
				tY2 = (texture.getHeight() - (this.rectangle
						.getY() + this.rectangle.getHeight()))
						/ texture.getHeight();
			}

			float rX1 = ((2.0f * object.getX()) - target
					.getWidth()) /  target.getWidth();
			float rX2 =  ((2.0f * (object.getX() + object.getWidth()) -  target
					.getWidth()) /  target.getWidth());
			float rY1 =  (2.0f * (object.getY() + object.getHeight()) -  target
					.getHeight()) /  target.getHeight();
			float rY2 =  ((2.0f * object.getY() -  target
					.getHeight()) /  target.getHeight());

			gl.glTexParameteri(GL.GL_TEXTURE_2D, 
					GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_NEAREST);
			
			gl.glTexParameteri(GL.GL_TEXTURE_2D, 
					GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_NEAREST);
			
			gl.glAlphaFunc(GL2.GL_GREATER, 0.10f);

			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(this.color[0], this.color[1], this.color[2]);
			float renderZ = ( -zIndex) / 1000000f;
			
			gl.glTexCoord2f(tX1, tY1);
			gl.glVertex3f(rX1, -rY2, renderZ);

			gl.glTexCoord2f(tX2, tY1);
			gl.glVertex3f(rX2, -rY2, renderZ);

			gl.glTexCoord2f(tX2, tY2);
			gl.glVertex3f(rX2, -rY1, renderZ);

			gl.glTexCoord2f(tX1, tY2);
			gl.glVertex3f(rX1, -rY1, renderZ);

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
			tools.Logger.err("Failed to serialize Sprite!");
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
			tools.Logger.err("Failed to deserialize Sprite!");
			e.printStackTrace();
		}
	}

	@Override
	public void update(double dt) {
	}
}