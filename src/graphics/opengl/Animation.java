package graphics.opengl;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

/**
 * A class used to animate.
 * 
 * It cycles through an array of frames.
 * 
 * @author Barber
 * 
 */
public class Animation implements JSONSerializable, GLRenderableObject {

	private String name;
	private Sprite[] frames;
	private Sprite currentSprite;
	private double durationMillis;

	private double timePassed;

	// ************* Constructors *************
	/**
	 * Creates an actor from a JSON object.
	 * 
	 * @param o The JSON object.
	 */
	public Animation(JSONObject o)
	{
		this.deserialize(o);
	}

	/**
	 * Creates an actor.
	 * 
	 * @param name The name.
	 * @param frames The frames.
	 * @param duration The duration of the animation.
	 */
	public Animation(String name, Sprite[] frames, double duration)
	{
		this.name = name;
		this.frames = frames;
		this.durationMillis = duration;
	}

	// ************* Getters *************
	/**
	 * Get the name of the animation.
	 * 
	 * @return The name of the animation.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the duration of the animation.
	 * 
	 * @return The duration of the animation.
	 */
	public double getDuration()
	{
		return this.durationMillis;
	}

	/**
	 * Get the frames of the animation.
	 * 
	 * @return The frames of the animation.
	 */
	public Sprite[] getFrames()
	{
		return this.frames;
	}

	// ************* Setters *************
	/**
	 * Set the name of the animation.
	 * 
	 * @param name The name of the animation.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Set the duration of the animation.
	 * 
	 * @param durationMillis The duration.
	 */
	public void setDuration(double durationMillis)
	{
		this.durationMillis = durationMillis;
	}

	/**
	 * Set the frames of the animations.
	 * 
	 * @param frames The new frames.
	 */
	public void setFrames(Sprite[] frames)
	{
		this.frames = frames;
	}

	// ************* Methods *************
	/**
	 * Add a frame to the animation.
	 * 
	 * @param frame The new frame.
	 */
	public void addFrame(Sprite frame)
	{
		Sprite[] newFrames = new Sprite[this.frames.length + 1];
		for (int i = 0; i < this.frames.length; i++) {
			newFrames[i] = this.frames[i];
		}
		newFrames[newFrames.length - 1] = new Sprite("texture", 0, 0, 0, 0);
		this.frames = newFrames;
	}

	@Override
	public void update(double dt)
	{
		this.timePassed += dt;
		if (this.timePassed >= durationMillis)
			this.timePassed = 0;
	}

	// ************* Other *************
	public String toString()
	{
		return this.name;
	}

	// ************* Interface methods *************
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		this.currentSprite = this.frames[(int) ((this.timePassed / this.durationMillis) * (double) (this.frames.length))];
		this.currentSprite.render(object, target, canvas);
	}

	@Override
	public JSONObject serialize()
	{
		JSONObject retObj = new JSONObject();
		try {
			retObj.put("name", this.name);
			retObj.put("duration", this.durationMillis);
			JSONArray array = new JSONArray();

			if (frames != null) {
				for (Sprite s : frames) {
					array.put(s.serialize());
				}
			}
			retObj.put("frames", array);

		} catch (JSONException e) {
			System.out.println("Failed to serialize Animation: " + this.name);
			e.printStackTrace();
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o)
	{
		try {
			this.name = o.getString("name");
			this.durationMillis = o.getDouble("duration");
			JSONArray array = o.getJSONArray("frames");
			this.frames = new Sprite[array.length()];
			for (int i = 0; i < array.length(); i++) {
				this.frames[i] = new Sprite(array.getJSONObject(i));
			}
		} catch (JSONException e) {
			System.out.println("Failed to deserialize Animation!");
			e.printStackTrace();
		}
	}

	@Override
	public Rectangle getBounds()
	{
		if (this.currentSprite != null)
			return this.currentSprite.getBounds();
		return null;
	}

}
