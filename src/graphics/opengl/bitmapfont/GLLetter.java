package graphics.opengl.bitmapfont;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import tools.Logger;

import graphics.json.JSONSerializable;
import graphics.opengl.animation.Sprite;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;

public class GLLetter implements GLRenderableObject, JSONSerializable {

	private char character;
	private Sprite sprite;

	public GLLetter(Sprite sprite, char character) {
		this.sprite = sprite;
		this.character = character;
	}
	public GLLetter(JSONObject o) {
		this.deserialize(o);
	}

	// Setters

	public void setColor(float r, float g, float b)
	{
		this.sprite.setColor(r, g, b);
	}
	// Getters
	public Sprite getSprite() {
		return this.sprite;
	}
	public char getCharacter()
	{
		return this.character;
	}

	
	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {
		this.sprite.render(object, target, canvas, zIndex);
	}
	@Override
	public void update(double dt) {}
	@Override
	public Rectangle getBounds() {
		return this.sprite.getBounds();
	}

	@Override
	public JSONObject serialize() {
		JSONObject retObj = null;
		
		try {
			retObj = new JSONObject();
			retObj.put("char", (int) this.character);
			retObj.put("sprite", this.sprite.serialize());
		} catch (JSONException e) {
			Logger.log("Couldn't serialize Letter!");
			e.printStackTrace();
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) {
		try {
			this.character = (char) o.getInt("char");
			this.sprite = new Sprite(o.getJSONObject("sprite"));
		} catch (JSONException e) {
			Logger.log("Couldn't deserialize Letter!");
			e.printStackTrace();
		}
	}
}
