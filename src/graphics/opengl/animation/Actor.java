package graphics.opengl.animation;

import com.jogamp.opengl.util.texture.Texture;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import graphics.json.JSONSerializable;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;

import model.CollidableObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.datamanagement.SharedTextures;

public class Actor implements JSONSerializable, GLRenderableObject {

	private String textureName;

	// Animation
	private Animation[] animations;
	private Animation currentAnimation;

	private Rectangle renderingSize;

	private CollidableObject collidableObject;

	private boolean showCollisionBox = false;

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
	 * @throws JSONException
	 *             If the JSONObject doesn't contain some of the required keys
	 */
	public Actor(JSONObject o) throws JSONException {
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

	public CollidableObject getCollidableObject() {
		return this.collidableObject;
	}

	public boolean isShowingCollisionBox() {
		return this.showCollisionBox;
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
	public void deserialize(JSONObject o) throws JSONException {

		this.textureName = o.getString("textureName");

		JSONArray jsonAnimations = o.getJSONArray("animations");
		this.animations = new Animation[jsonAnimations.length()];
		for (int i = 0; i < jsonAnimations.length(); i++) {
			this.animations[i] = new Animation(jsonAnimations.getJSONObject(i));
		}
	}

	@Override
	public void render(Rectangle object, Rectangle target,
			GLAutoDrawable canvas, float zIndex) {

		if (this.currentAnimation == null
				|| this.currentAnimation.getCurrentSprite() == null
				|| this.currentAnimation.getCurrentSprite().getVertexPoints() == null
				|| this.renderingSize == null) {
			return;
		}

		Texture texture = SharedTextures.getSharedTextures().bindTexture(this.textureName, canvas);
		GL2 gl = canvas.getGL().getGL2();
		
		
		float[] renderingpoints = this.currentAnimation.getCurrentSprite()
				.getVertexPoints();
		float tx = renderingpoints[0] / (float) texture.getWidth();
		float ty = renderingpoints[1] / (float) texture.getHeight();
		float tw = renderingpoints[2] / (float) texture.getWidth();
		float th = renderingpoints[3] / (float) texture.getHeight();
		
		// TODO Clear this if never used.
		float tox = renderingpoints[4];
		float toy = renderingpoints[5];
		
		float rx = this.renderingSize.getX() / target.getX();
		float ry = this.renderingSize.getY() / target.getY();
		float rw = this.renderingSize.getWidth() / target.getWidth();
		float rh = this.renderingSize.getHeight() / target.getHeight();
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(tx, ty);
		gl.glVertex3f(rx, ry, 0);
		
		gl.glTexCoord2f(tx + tw, ty);
		gl.glVertex3f(rx + rw, ry, 0);
		
		gl.glTexCoord2f(tx + tw, ty + th);
		gl.glVertex3f(rx + rw, ry + rh, 0);
		
		gl.glTexCoord2f(tx, ty + th);
		gl.glVertex3f(rx, ry + rh, 0);
		
		gl.glEnd();
		

	}
	@Override
	public void update(double dt) {}

	@Override
	public Rectangle getBounds() {
		return null;
	}

}
