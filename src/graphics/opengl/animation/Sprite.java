package graphics.opengl.animation;

import org.json.JSONException;
import org.json.JSONObject;

import graphics.json.JSONSerializable;
import graphics.opengl.core.Position;
import graphics.opengl.core.Rectangle;
import graphics.opengl.core.Size;

public class Sprite implements JSONSerializable {

	private Rectangle textureRectangle;
	private Position offset;

	// Constructors
	/**
	 * Creates an empty Sprite with no variables set.
	 */
	public Sprite() {
		this.textureRectangle = new Rectangle(0, 0, 0, 0);
		this.offset = new Position(0, 0);
	}

	/**
	 * Cretes a Sprite and deserializes it.
	 * @param o The JSONObject to deserialize from
	 * @throws JSONException If the JSONObject doesn't contain some of the required keys 
	 */
	public Sprite(JSONObject o) throws JSONException {
		this();
		this.deserialize(o);
	}

	// Setters
	/**
	 * Set the rectangle used to decide what part of a texture should be drawn.
	 *  
	 * @param rectangle The rectangle
	 */
	public void setRectangle(Rectangle rectangle) {
		this.textureRectangle = rectangle;
	}

	public void setOffset(Position p) {
		if (p == null) throw new IllegalArgumentException();
		
		this.offset = p;
	}
	
	// Getters
	/**
	 * Get the rectangle.
	 * 
	 * @return The rectangle
	 */
	public Rectangle getRectangle() {
		return this.textureRectangle;
	}
	
	public Position getOffset() {
		return this.offset;
	}
	
	// Methods
	/**
	 * Gets the points with instructions regarding how to render the sprite. 
	 * 
	 * The first two values are the texture coordinates, the two following 
	 * are the vertex coordinates and the last two are the texture offset.
	 * 
	 * The texture and offset measurements are in PIXELS, and thus have to 
	 * be scaled into floats between 0 and 1.
	 * 
	 * The vertex coordinates are either 0 or 1 and should be scaled as needed.
	 * 
	 * @return
	 */
	public float[] getVertexPoints() {
		float[] points = new float[6];
		
		points[0] = this.textureRectangle.getX();
		points[1] = this.textureRectangle.getY();
		points[2] = this.textureRectangle.getWidth();
		points[3] = this.textureRectangle.getHeight();
		points[4] = this.offset.getX();
		points[5] = this.offset.getY();
		
		return points;
	}
	
	// Interface
	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject retObj = new JSONObject();

		if (this.textureRectangle != null) {
			JSONObject jsonRect = new JSONObject();
			jsonRect.put("x", this.textureRectangle.getX());
			jsonRect.put("y", this.textureRectangle.getY());
			jsonRect.put("width", this.textureRectangle.getWidth());
			jsonRect.put("height", this.textureRectangle.getHeight());
			jsonRect.put("offsX", this.offset.getX());
			jsonRect.put("offsY", this.offset.getY());

			retObj.put("rect", jsonRect);
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException {
		JSONObject jsonRect = o.getJSONObject("rect");
		int x = jsonRect.getInt("x");
		int y = jsonRect.getInt("y");
		int width = jsonRect.getInt("width");
		int height = jsonRect.getInt("height");
		int offsX = jsonRect.getInt("offsX");
		int offsY = jsonRect.getInt("offsY");
		this.textureRectangle = new Rectangle(x, y, width, height);
		this.offset = new Position(offsX, offsY);
	}

}
