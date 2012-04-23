package graphics.opengl;

import gui.SharedTextures;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.texture.Texture;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

/**
 * A class used to draw a certain part of a texture.
 * 
 * @author Barber
 * 
 */
public class Sprite implements JSONSerializable, GLRenderableObject {

	private String textureName;

	private Texture texture;
	private int x;
	private int y;
	private int width;
	private int height;

	// ************* Constructors *************
	/**
	 * Creates a sprite from a JSON object.
	 * 
	 * @param o
	 *            The JSON object.
	 */
	public Sprite(JSONObject o)
	{
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
	public Sprite(String textureName, int x, int y, int width, int height)
	{
		this.textureName = textureName;
		this.setTexture(this.textureName);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// ************* Other *************
	public String toString()
	{
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
	public void setTexture(String textureName)
	{
		this.textureName = textureName;
		this.texture = null;
	}

	/**
	 * Set the x coordinate of the sprite.
	 * 
	 * @param x
	 *            The x coordinate.
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Set the y coordinate of the sprite.
	 * 
	 * @param y
	 *            The y coordinate.
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Set the width of the sprite.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * Set the height of the sprite.
	 * 
	 * @param height
	 *            The height.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	// ************* Getters *************
	/**
	 * Get the name of the texture.
	 * 
	 * @return The name of the texture.
	 */
	public String getTextureName()
	{
		return this.textureName;
	}

	/**
	 * Get the x coordinate of the sprite.
	 * 
	 * @return The x coordinate.
	 */
	public int getX()
	{
		return this.x;
	}

	/**
	 * Get the y coordinate of the sprite.
	 * 
	 * @return The y coordinate.
	 */
	public int getY()
	{
		return this.y;
	}

	/**
	 * Get the width of the sprite.
	 * 
	 * @return The width.
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 * Get the height of the sprite.
	 * 
	 * @return The height.
	 */
	public int getHeight()
	{
		return this.height;
	}

	// ************* Interface methods *************
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		if (object.intersectsRectangle(target)) {

			GL2 gl = canvas.getGL().getGL2();

			if (this.texture == null)
				this.texture = SharedTextures.getSharedTextures().getTexture(
						this.textureName);

			float tX1 = (float) this.x / (float) texture.getWidth();
			float tX2 = ((float) this.x + (float) this.width)
					/ (float) texture.getWidth();
			float tY1 = (float) this.y / (float) texture.getHeight();
			float tY2 = ((float) this.y + (float) this.height)
					/ (float) texture.getHeight();

			float rX1 = (float) ((2.0f * object.getX()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rX2 = (float) (2.0f * (object.getX() + object.getWidth()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rY1 = (float) (2.0f * object.getY() - (float) target
					.getHeight()) / (float) target.getHeight();
			float rY2 = (float) (2.0f * (object.getY() + object.getHeight()) - (float) target
					.getHeight()) / (float) target.getHeight();

			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
					GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
					GL.GL_NEAREST);

			this.texture.bind(gl);
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glTexCoord2f(tX1, tY1);
			gl.glVertex2f(rX1, rY2);

			gl.glTexCoord2f(tX2, tY1);
			gl.glVertex2f(rX2, rY2);

			gl.glTexCoord2f(tX2, tY2);
			gl.glVertex2f(rX2, rY1);

			gl.glTexCoord2f(tX1, tY2);
			gl.glVertex2f(rX1, rY1);

			gl.glEnd();
		}
	}

	@Override
	public JSONObject serialize()
	{
		JSONObject retObj = new JSONObject();
		try {
			retObj.put("texture", this.textureName);
			retObj.put("x", this.x);
			retObj.put("y", this.y);
			retObj.put("width", this.width);
			retObj.put("height", this.height);
		} catch (JSONException e) {
			System.out.println("Failed to serialize Sprite!");
			e.printStackTrace();
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o)
	{
		try {
			this.textureName = o.getString("texture");
			this.texture = null;
			this.x = o.getInt("x");
			this.y = o.getInt("y");
			this.width = o.getInt("width");
			this.height = o.getInt("height");
		} catch (JSONException e) {
			System.out.println("Failed to deserialize Sprite!");
			e.printStackTrace();
		}
	}
}