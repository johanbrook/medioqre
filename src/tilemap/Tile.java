package tilemap;

import graphics.opengl.Sprite;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
/**
 * A class used to represent a game tile.
 * 
 * @author John Barbero Unenge
 * 
 */
public class Tile implements JSONSerializable, GLRenderableObject, Comparable {

	private Sprite sprite;
	private int type;
	boolean collidable;

	/**
	 * Creates a tile with the given Sprite, type and whether it's collidable or
	 * not.
	 * 
	 * @param sprite
	 *            The sprite
	 * @param type
	 *            The type
	 * @param collidable
	 *            Whether it's collidable or not
	 */
	public Tile(Sprite sprite, int type, boolean collidable) {
		this.type = type;
		this.sprite = sprite;
	}

	/**
	 * Creates a tile from a JSONObject.
	 * 
	 * @param o
	 *            The JSONObject to use for creating the tile
	 */
	public Tile(JSONObject o) {
		this.deserialize(o);
	}

	// Setters
	/**
	 * Set whether the tile should be collidable.
	 * 
	 * @param collidable
	 *            True or false
	 */
	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public void setType(int type) {
		this.type = type;
	}

	// Getters
	/**
	 * Get the type of the tile.
	 * 
	 * @return The type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Get the sprite of the tile.
	 * 
	 * @return The sprite
	 */
	public Sprite getSprite() {
		return this.sprite;
	}

	/**
	 * Returns whether it's collidable or not.
	 * 
	 * @return True or false
	 */
	public boolean isCollidable() {
		return this.collidable;
	}

	// Interface methods
	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {

		if (this.sprite != null)
			this.sprite.render(object, target, canvas, 0);
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public Rectangle getBounds() {
		return this.sprite.getBounds();
	}

	@Override
	public JSONObject serialize() {
		JSONObject retObj = new JSONObject();
		try {
			retObj.put("sprite", this.sprite.serialize());
			retObj.put("type", this.type);
			retObj.put("collidable", this.collidable);
			return retObj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deserialize(JSONObject o) {
		try {
			this.sprite = new Sprite(o.getJSONObject("sprite"));
			this.type = o.getInt("type");
			this.collidable = o.getBoolean("collidable");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Object arg0) {
		int retVal = 0;
		
		if (arg0 instanceof Tile) {
			Tile t = (Tile) arg0;
			if (t.getBounds().getY() > this.getBounds().getY()) {
				retVal = -1;
			} else if (t.getBounds().getY() < this.getBounds().getY()) {
				retVal = 1;
			} else {
				if (t.getBounds().getX() > this.getBounds().getX()) {
					retVal = -1;
				} else if (t.getBounds().getX() < this.getBounds().getX()) {
					retVal = 1;
				}
			}
		}
		
		return retVal;
	}
}
