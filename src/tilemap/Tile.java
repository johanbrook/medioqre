package tilemap;

import graphics.opengl.Sprite;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

public class Tile implements JSONSerializable, GLRenderableObject{

	private Sprite sprite;
	private int type;
	boolean collidable;
	
	
	public Tile(Sprite sprite, int type, boolean collidable)
	{
		this.type = type;
		this.sprite = sprite;
	}
	
	public boolean isCollidable()
	{
		return this.collidable;
	}
	public void setCollidable(boolean collidable)
	{
		this.collidable = collidable;
	}
	
	public Tile(JSONObject o)
	{
		this.deserialize(o);
	}
	
	// Setters
	public void setType(int type)
	{
		this.type = type;
	}
	
	// Getters 
	public int getType()
	{
		return this.type;
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas, int zIndex)
	{
		
		if (this.sprite != null)
			this.sprite.render(object, target, canvas, 0);
	}

	@Override
	public void update(double dt)
	{}

	@Override
	public Rectangle getBounds()
	{
		return this.sprite.getBounds();
	}

	@Override
	public JSONObject serialize()
	{
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
	public void deserialize(JSONObject o)
	{
		try {
			this.sprite = new Sprite(o.getJSONObject("sprite"));
			this.type = o.getInt("type");
			this.collidable = o.getBoolean("collidable");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
