package graphics.opengl.animation;

import org.json.JSONException;
import org.json.JSONObject;

import graphics.json.JSONSerializable;
import graphics.opengl.core.Rectangle;

public class Sprite implements JSONSerializable {

	private Rectangle rect;

	// Constructors
	public Sprite() {

	}

	public Sprite(JSONObject o) throws JSONException {
		this.deserialize(o);
	}

	// Setters
	public void setRectangle(Rectangle rectangle) {
		this.rect = rectangle;
	}

	// Getters
	public Rectangle getRectangle() {
		return this.rect;
	}
	// Interface
	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject retObj = new JSONObject();

		if (this.rect != null) {
			JSONObject jsonRect = new JSONObject();
			jsonRect.put("x", this.rect.getX());
			jsonRect.put("y", this.rect.getY());
			jsonRect.put("width", this.rect.getWidth());
			jsonRect.put("height", this.rect.getHeight());

			retObj.put("rect", jsonRect);
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException {
		JSONObject jsonRect = o.getJSONObject("rect");
		int x = jsonRect.getInt("x");
		int y = jsonRect.getInt("x");
		int width = jsonRect.getInt("x");
		int height = jsonRect.getInt("x");
		this.rect = new Rectangle(x, y, width, height);
	}

}
