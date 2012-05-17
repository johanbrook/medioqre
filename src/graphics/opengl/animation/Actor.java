package graphics.opengl.animation;

import graphics.json.JSONSerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Actor implements JSONSerializable {

	private String textureName;

	// Animation
	private Animation[] animations;
	private Animation currentAnimation;

	// Constructors
	/**
	 * Creates an Actor with no animations.
	 */
	public Actor() {

	}

	/**
	 * Creates an Actor and instantiates it with the information in the
	 * JSONObject.
	 * 
	 * @param o
	 *            JSONObject with Actor info
	 */
	public Actor(JSONObject o) throws JSONException{
		this.deserialize(o);
	}

	// Setters
	/**
	 * Set all the animations of this Actor.
	 * 
	 * @param animations
	 *            The animations
	 */
	public void setAnimations(Animation[] animations) {
		this.animations = animations;
	}

	/**
	 * Adds an animation to the actor. If the actor has an animation with the
	 * same animationTag that animation will be replaced by this new one.
	 * 
	 * @param animation
	 *            The animation to add
	 */
	public void addAnimation(Animation animation) {

		if (this.animations != null) {
			for (int i = 0; i < this.animations.length; i++) {
				if (this.animations[i].getAnimationTag() == animation
						.getAnimationTag()) {
					this.animations[i] = animation;
					return;
				}
			}
		}

		Animation[] tempAnimations = new Animation[this.animations == null
				? 1
				: this.animations.length + 1];

		for (int i = 0; i < tempAnimations.length - 1; i++) {
			tempAnimations[i] = this.animations[i];
		}

		tempAnimations[tempAnimations.length - 1] = animation;
		this.animations = tempAnimations;
	}

	/**
	 * Set the name of the texture that this actor uses for its animations.
	 * 
	 * Note that the texture needs to contain every frame that the actor uses
	 * for its animations.
	 * 
	 * The texture has to be in SRGB .png format.
	 * 
	 * @param textureName
	 *            The name of the resource to load (without any file extension)
	 */
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}

	// Getters
	/**
	 * Get the a pointer to the animations of this Actor.
	 * 
	 * @return The animations of this Actor
	 */
	public Animation[] getAnimations() {
		return this.animations;
	}

	/**
	 * Get the animation corresponding to the given animationTag.
	 * 
	 * @param animationTag
	 *            The animationTag
	 * @return The animation
	 * @throws NoSuchAnimationException
	 *             If no animation is found for the given tag
	 */
	public Animation getAnimation(int animationTag) {
		if (this.animations != null) {
			for (int i = 0; i < this.animations.length; i++) {
				if (this.animations[i].getAnimationTag() == animationTag)
					return this.animations[i];
			}
		}
		throw new NoSuchAnimationException();
	}

	/**
	 * Get the name of the texture used by this Actor.
	 * 
	 * @return The name of the resource to load (without any file extension)
	 */
	public String getTextureName() {
		return this.textureName;
	}

	// Interfaces
	@Override
	public JSONObject serialize() throws JSONException {

		JSONObject retObj = new JSONObject();
		JSONArray jsonAnimations = new JSONArray();

		for (Animation a : this.animations) {
			jsonAnimations.put(a.serialize());
		}

		retObj.put("textureName", this.textureName);
		retObj.put("animations", jsonAnimations);

		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException{

		this.textureName = o.getString("textureName");

		JSONArray jsonAnimations = o.getJSONArray("animations");
		this.animations = new Animation[jsonAnimations.length()];
		for (int i = 0; i < jsonAnimations.length(); i++) {
			this.animations[i] = new Animation(jsonAnimations.getJSONObject(i));
		}
	}

}
