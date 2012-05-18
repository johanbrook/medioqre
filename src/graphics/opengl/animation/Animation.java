package graphics.opengl.animation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graphics.json.JSONSerializable;

public class Animation implements JSONSerializable {

	private int animationTag;

	private Sprite[] sprites;

	private boolean looping;

	private int duration;

	/**
	 * Creates an Animation with no Sprites and no animation tag.
	 * 
	 * Default is to loop the animation when the Sprites are added.
	 */
	public Animation() {
		this(new Sprite[]{}, 0, true);
	}

	/**
	 * Creates an Animation with the given Sprites, animation duration and
	 * whether to loop the animation or not.
	 * 
	 * @param sprites
	 *            The Sprites
	 * @param duration
	 *            The duration of the animation
	 * @param shouldLoop
	 *            Whether to loop the animation or not
	 */
	public Animation(Sprite[] sprites, int duration, boolean shouldLoop) {
		if (sprites == null)
			throw new IllegalArgumentException();

		this.sprites = sprites;
		this.looping = shouldLoop;
	}

	/**
	 * Creates an Animation and deserializes it from the given JSONObject.
	 * 
	 * @param o
	 *            JSONObject to deserialize from
	 * @throws JSONException
	 *             If the JSONObject doesn't contain some of the required keys
	 */
	public Animation(JSONObject o) throws JSONException {
		this();
		this.deserialize(o);
	}

	// Setters
	/**
	 * Set the animationTag.
	 * 
	 * @param animationTag
	 *            The new animationTag
	 */
	public void setAnimationTag(int animationTag) {
		this.animationTag = animationTag;
	}

	/**
	 * Set the sprites of the Animation. May be an empty array but not null.
	 * 
	 * @param sprites
	 *            The new sprites
	 * @throws IllegalArgumentException
	 *             If the given sprite array is null
	 */
	public void setSprites(Sprite[] sprites) {
		if (sprites == null)
			throw new IllegalArgumentException();

		this.sprites = sprites;
	}

	/**
	 * Adds a sprite to the last position of the sprite array.
	 * 
	 * @param sprite
	 *            The new Sprite
	 */
	public void addSprite(Sprite sprite) {

		Sprite[] tempSprites = new Sprite[this.sprites.length + 1];

		for (int i = 0; i < tempSprites.length - 1; i++) {
			tempSprites[i] = this.sprites[i];
		}

		tempSprites[tempSprites.length - 1] = sprite;
		this.sprites = tempSprites;
	}

	// Getters
	/**
	 * Get the animationTag.
	 * 
	 * @return The animationTag
	 */
	public int getAnimationTag() {
		return this.animationTag;
	}

	/**
	 * Get the sprites.
	 * 
	 * @return The sprites
	 */
	public Sprite[] getSprites() {
		return this.sprites;
	}

	private long timeLastFrame;
	private int timePassed;
	/**
	 * Returns the current sprite. May be null.
	 * 
	 * @return The current Sprite
	 */
	public Sprite getCurrentSprite() {
		long timeThisFrame = System.nanoTime() / 1000000;
		int dt = (int) (timeThisFrame - this.timeLastFrame);
		this.timePassed += dt;
		this.timeLastFrame = timeThisFrame;

		if (this.timePassed < this.duration) {
			if (this.sprites != null) return null;
			
			return this.sprites[(int) ((int) ((float) this.timePassed / (float) duration) * (float) (this.sprites.length - 1))];
		} else {
			if (this.looping) {
				this.startAnimation();
				return this.getCurrentSprite();
			}
			return (this.sprites == null ? null : this.sprites[0]);
		}
	}

	/**
	 * Starts the animation.
	 */
	public void startAnimation() {
		this.timeLastFrame = System.nanoTime() / 1000000;
		this.timePassed = 0;
	}

	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject retObj = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < (this.sprites == null ? 0 : this.sprites.length); i++) {
			if (this.sprites[i] == null) continue;
			
			jsonArray.put(i, this.sprites[i].serialize());
		}
		
		retObj.put("animationTag", this.animationTag);
		retObj.put("sprites", jsonArray);
		retObj.put("looping", this.looping);
		retObj.put("duration", this.duration);
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException {
		this.animationTag = o.getInt("animationTag");
		
		JSONArray jsonArray = o.getJSONArray("sprites");
		this.sprites = new Sprite[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			this.sprites[i] = new Sprite(jsonArray.getJSONObject(i));
		}
		
		this.looping = o.getBoolean("looping");
		this.duration = o.getInt("duration");
		
	}

}
