package graphics.opengl;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.media.opengl.GLAutoDrawable;

import model.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

/**
 * A class used to graphically represent a game character.
 * 
 * It's made up by a set of animations that are used to represent different
 * states.
 * 
 * @author Barber
 */
public class Actor implements JSONSerializable, GLRenderableObject {

	// Animation
	private Animation currentAnimation;
	private Map<String, Animation> animations;

	// Name
	private String name;

	// Position
	private Rectangle rectangle;

	// Entity
	private Entity entity;

	// ************* Getters *************
	/**
	 * Get the x coordinate of the actor.
	 * 
	 * @return The x coordinate.
	 */
	public int getX()
	{
		return this.rectangle.getX();
	}

	/**
	 * Get the y coordinate of the actor.
	 * 
	 * @return The y coordinate.
	 */
	public int getY()
	{
		return this.rectangle.getY();
	}

	/**
	 * Get the width of the actor.
	 * 
	 * @return The width.
	 */
	public int getWidth()
	{
		return this.rectangle.getWidth();
	}

	/**
	 * Get the height of the actor.
	 * 
	 * @return The height.
	 */
	public int getHeight()
	{
		return this.rectangle.getHeight();
	}

	/**
	 * Get the rectangle representing the x, y, width and height of the actor.
	 * 
	 * @return The rectangle.
	 */
	public Rectangle getRectangle()
	{
		return this.rectangle;
	}

	public Animation getCurrentAnimation()
	{
		return this.currentAnimation;
	}

	/**
	 * Get a Map with the animations of the actor.
	 * 
	 * @return The animations.
	 */
	public Map<String, Animation> getAnimations()
	{
		return this.animations;
	}

	/**
	 * Get the name of the actor. Mostly used when creating actors and when
	 * loading them from file.
	 * 
	 * @return The name of the actor.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the entity that the actor is following.
	 * 
	 * @return The entity.
	 */
	public Entity getEntity()
	{
		return this.entity;
	}

	// ************* Setters *************
	/**
	 * Set the x coordinate of the actor.
	 * 
	 * @param x
	 *            The x coordinate.
	 */
	public void setX(int x)
	{
		this.rectangle.setX(x);
	}

	/**
	 * Set the y coordinate of the actor.
	 * 
	 * @param y
	 *            The y coordinate.
	 */
	public void setY(int y)
	{
		this.rectangle.setY(y);
	}

	/**
	 * Set the width of the actor.
	 * 
	 * @param width
	 *            The width.
	 */
	public void setWidth(int width)
	{
		this.rectangle.setWidth(width);
	}

	/**
	 * Set the height of the actor.
	 * 
	 * @param height
	 *            The height of the actor.
	 */
	public void setHeight(int height)
	{
		this.rectangle.setHeight(height);
	}

	/**
	 * Set the currently displayed animation.
	 * 
	 * @param animationName
	 *            The name of the animation to display.
	 */
	public void setCurrentAnimation(String animationName)
	{
		this.currentAnimation = this.animations.get(animationName);
	}

	/**
	 * Set the animations Map.
	 * 
	 * @param animations
	 *            The new animations.
	 */
	public void setAnimations(Map<String, Animation> animations)
	{
		this.animations = animations;
	}

	/**
	 * Set the name of the actor.
	 * 
	 * @param name
	 *            The name.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Set the entity that the actor should follow.
	 * 
	 * @param e
	 *            The Entity.
	 */
	public void setEntity(Entity e)
	{
		this.entity = e;
	}

	// ************* Animations *************
	/**
	 * Add an animation to the actor.
	 * 
	 * @param animation
	 *            The animation to add.
	 */
	public void addAnimation(Animation animation)
	{
		if (this.animations == null) {
			this.animations = new IdentityHashMap<String, Animation>();
		}

		this.animations.put(animation.getName(), animation);
	}

	// ************* Constructors *************
	/**
	 * Creates an actor with the given animations.
	 * 
	 * @param name
	 *            The name of the actor.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param animations
	 *            The animations.
	 */
	public Actor(String name, int x, int y, int width, int height,
			Map<String, Animation> animations)
	{
		this.name = name;
		this.rectangle = new Rectangle(x, y, width, height);
		this.animations = animations;
	}

	/**
	 * Creates an actor with no animations. Animations needs to be added
	 * separately.
	 * 
	 * @param name
	 *            The name.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 */
	public Actor(String name, int x, int y, int width, int height)
	{
		this(name, x, y, width, height,
				new IdentityHashMap<String, Animation>());
	}

	/**
	 * Creates an actor from the provided JSON object.
	 * 
	 * @param o
	 *            The JSON object.
	 */
	public Actor(JSONObject o)
	{
		this.deserialize(o);
		this.setCurrentAnimation("moveLeft");

	}

	/**
	 * Creates an actor that follows the provided entity.
	 * 
	 * @param o
	 *            JSON object.
	 * @param e
	 *            Entity.
	 */
	public Actor(JSONObject o, Entity e)
	{
		this(o);

	}

	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		this.currentAnimation.render(object, target, canvas);
	}

	@Override
	public JSONObject serialize()
	{

		JSONObject retObj = new JSONObject();
		try {
			retObj.put("name", this.name);
			retObj.put("x", this.rectangle.getX());
			retObj.put("y", this.rectangle.getY());
			retObj.put("width", this.rectangle.getWidth());
			retObj.put("height", this.rectangle.getHeight());

			JSONArray anim = new JSONArray();
			for (Animation a : this.animations.values()) {
				anim.put(a.serialize());
			}

			retObj.put("animations", anim);
		} catch (JSONException e) {
			System.out.println("Failed to serialize!");
			e.printStackTrace();
		}
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o)
	{
		try {
			this.name = o.getString("name");
			this.rectangle = new Rectangle(o.getInt("x"), o.getInt("y"),
					o.getInt("width"), o.getInt("height"));

			System.out.println("Loading: " + o.getInt("x") + ","
					+ o.getInt("y") + "," + o.getInt("width") + ","
					+ o.getInt("height"));

			JSONArray anim = o.getJSONArray("animations");
			this.animations = new TreeMap<String, Animation>();
			for (int i = 0; i < anim.length(); i++) {
				Animation a = new Animation(anim.getJSONObject(i));
				this.animations.put(a.getName(), a);
			}
		} catch (JSONException e) {
			System.out.println("Failed to deserialize!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * A more detailed version of the toString method.
	 * 
	 * @return A string representation of the actor.
	 */
	public String toStringFull()
	{
		return ("Actor: " + this.name + " Position: (" + this.rectangle.getX()
				+ "," + this.rectangle.getY() + ") Size (w,h): ("
				+ this.rectangle.getWidth() + "," + this.rectangle.getHeight() + ")");
	}

	public String toString()
	{
		return this.name;
	}
}
