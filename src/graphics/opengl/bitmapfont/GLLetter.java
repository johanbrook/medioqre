package graphics.opengl.bitmapfont;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import tools.Logger;

import graphics.json.JSONSerializable;
import graphics.opengl.animation.Sprite;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;

public class GLLetter implements JSONSerializable {

	private char character;
	private Sprite sprite;
	private float[] color = {1f,1f,1f};

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
		this.color = new float[]{r,g,b};
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
