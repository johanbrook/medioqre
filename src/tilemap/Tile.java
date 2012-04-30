package tilemap;

import java.util.Random;

import graphics.opengl.Sprite;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import datamanagement.SharedTextures;

public class Tile implements JSONSerializable, GLRenderableObject{

//	private Rectangle rectangle;
	private Sprite sprite;
	private int type;
	boolean collidable;
	
	
	// Testing shit
	// ************************************************** WARNING **************************************************
	// ********************************************* SHOULD BE REMOVED *********************************************
	private Random random = new Random(); 
	
	public Tile(Sprite sprite, int type, boolean collidable)
	{
		this.type = type;
		this.sprite = sprite;
	}
	
	public boolean isCollidable()
	{
		return this.collidable;
	}
	public void setCollidable(boolean collidable)
	{
		this.collidable = collidable;
	}
	
	public Tile(JSONObject o)
	{
		this.deserialize(o);
	}
	
	// Getters 
	public int getType()
	{
		return this.type;
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		
		if (this.sprite != null)
			this.sprite.render(object, target, canvas);
		
//		if (object != null && object.intersectsRectangle(target)) {
//
//			GL2 gl = canvas.getGL().getGL2();
//
//			float rX1 = (float) ((2.0f * object.getX()) - (float) target
//					.getWidth()) / (float) target.getWidth();
//			float rX2 = (float) (2.0f * (object.getX() + object.getWidth()) - (float) target
//					.getWidth()) / (float) target.getWidth();
//			float rY1 = (float) (2.0f * (object.getY() + object.getHeight()) - (float) target
//					.getHeight()) / (float) target.getHeight();
//			float rY2 = (float) (2.0f * object.getY() - (float) target
//					.getHeight()) / (float) target.getHeight();
//
//			gl.glBegin(GL2.GL_QUADS);
//			gl.glColor3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
//			
//			gl.glVertex2f(rX1, -rY2);
//			gl.glVertex2f(rX2, -rY2);
//			gl.glVertex2f(rX2, -rY1);
//			gl.glVertex2f(rX1, -rY1);
//
//			gl.glEnd();
//		}
	}

	@Override
	public void update(double dt)
	{}

	@Override
	public Rectangle getBounds()
	{
//		return this.rectangle;
		return this.sprite.getBounds();
	}

	@Override
	public JSONObject serialize()
	{
		JSONObject retObj = new JSONObject();
		try {
			retObj.put("sprite", this.sprite.serialize());
			retObj.put("type", this.type);
			return retObj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deserialize(JSONObject o)
	{
		try {
			this.sprite = new Sprite(o.getJSONObject("sprite"));
			this.type = o.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
