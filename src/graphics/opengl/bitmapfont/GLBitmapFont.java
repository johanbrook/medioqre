package graphics.opengl.bitmapfont;

import graphics.json.JSONSerializable;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Logger;
import tools.timer.CounterObject;
import tools.timer.CounterTool;


public class GLBitmapFont implements JSONSerializable, GLRenderableObject {

	private Map<Character, GLLetter> letters;
	private String text;
	private Rectangle rectangle;
	private int letterWidth;

	public GLBitmapFont(String text, Rectangle bounds,
			Map<Character, GLLetter> letters, int letterWidth) {
		this.text = text;
		this.rectangle = bounds;
		this.letters = letters;
		this.letterWidth = letterWidth;
	}
	public GLBitmapFont(JSONObject o) {
		this.deserialize(o);
	}

	public boolean addLetter(GLLetter l) {

		if (this.letters.get(l.getCharacter()) == null) {
			this.letters.put(l.getCharacter(), l);
			return true;
		}
		return false;
	}

	// Setters
	public void setText(String text) {
		this.text = text;
	}

	public void setBounds(Rectangle bounds) {
		this.rectangle = bounds;
	}

	public void setColor(float r, float g, float b) {
		for (GLLetter letter : this.letters.values()) {
			letter.setColor(r, g, b);
		}
		
	}
	
	public void setLetterWidth(int letterWidth) {
		this.letterWidth = letterWidth;
	}
	
	// Getters

	// ---

	// Interface methods

	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {

		int i = 0;
		for (char c : this.text.toCharArray()) {
			GLRenderableObject renderable = this.letters.get(c);

			if (renderable == null)
				return;

			Rectangle renderRect = new Rectangle(this.getBounds().getX() + i
					* this.letterWidth, this.getBounds().getY(),
					this.letterWidth, this.getBounds().getHeight());
			renderable.render(renderRect, target, canvas, zIndex);
			i++;
		}
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public Rectangle getBounds() {
		return this.rectangle;
	}

	@Override
	public JSONObject serialize() {

		JSONObject retObj = null;

		try {
			retObj = new JSONObject();

			JSONArray lettersArray = new JSONArray();
			for (GLLetter l : this.letters.values()) {
				lettersArray.put(l.serialize());
			}
			retObj.put("letters", lettersArray);

			retObj.put("text", text);
			JSONObject rect = new JSONObject();
			rect.put("x", this.rectangle.getX());
			rect.put("y", this.rectangle.getY());
			rect.put("width", this.rectangle.getWidth());
			rect.put("height", this.rectangle.getHeight());

			retObj.put("rect", rect);
			retObj.put("letterwidth", letterWidth);
		} catch (JSONException e) {
			Logger.log("Failed to serialize BitmapFont!");
			e.printStackTrace();
		}

		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) {
		try {
			JSONArray lettersArray;
			lettersArray = o.getJSONArray("letters");
			this.letters = new HashMap<Character, GLLetter>();
			for (int i = 0; i < lettersArray.length(); i++) {
				GLLetter l = new GLLetter(lettersArray.getJSONObject(i));
				this.letters.put(l.getCharacter(), l);
			}
			this.text = o.getString("text");
			JSONObject rect = o.getJSONObject("rect");
			this.rectangle = new Rectangle((float) rect.getInt("x"), (float) rect.getInt("y"), (float) rect.getInt("width"), (float) rect.getInt("height"));
			
			this.letterWidth = o.getInt("letterwidth");
		} catch (JSONException e) {
			Logger.log("Failed to deserialize BitmapFont!");
			e.printStackTrace();
		}
	}
}
