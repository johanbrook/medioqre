package graphics.opengl.animation;

import org.json.JSONException;
import org.json.JSONObject;

import graphics.json.JSONSerializable;

public class Animation implements JSONSerializable{

	private int animationTag;
	private Sprite[] sprites;
	
	/**
	 * Creates an Animation with no sprites and no animation tag.
	 */
	public Animation() {
		
	}
	
	/**
	 * Creates an Animation and deserializes it from the given JSONObject.
	 * 
	 * @param o JSONObject to deserialize from
	 * @throws JSONException If the JSONObject doesn't contain some of the required keys
	 */
	public Animation(JSONObject o) throws JSONException{
		this.deserialize(o);
	}
	
	// Setters
	/**
	 * Set the animationTag.
	 * 
	 * @param animationTag The new animationTag
	 */
	public void setAnimationTag(int animationTag) {
		this.animationTag = animationTag;
	}
	
	/**
	 * Set the sprites of the Animation.
	 * 
	 * @param sprites The new sprites
	 */
	public void setSprites(Sprite[] sprites) {
		this.sprites = sprites;
	}
	
	/**
	 * Adds a sprite to the last position of the sprite array.
	 * 
	 * @param sprite The new Sprite
	 */
	public void addSprite(Sprite sprite)  {

		Sprite[] tempSprites = new Sprite[this.sprites == null
				? 1
				: this.sprites.length + 1];

		for (int i = 0; i < tempSprites.length - 1; i++) {
			tempSprites[i] = this.sprites[i];
		}

		tempSprites[tempSprites.length - 1] = sprite;
		this.sprites = tempSprites;
	}
	
	// Getters
	/**
	 * Get the animationTag.
	 * @return The animationTag
	 */
	public int getAnimationTag() {
		return this.animationTag;
	}
	
	/**
	 * Get the sprites.
	 * @return The sprites
	 */
	public Sprite[] getSprites() {
		return this.sprites;
	}
	
	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject retObj = new JSONObject();
		
		retObj.put("animationTag", this.animationTag);
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException{
		this.animationTag = o.getInt("animationTag");
	}

}
