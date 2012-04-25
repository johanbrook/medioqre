package tilemap;

import graphics.opengl.Sprite;
import gui.SharedTextures;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

public class Tile implements JSONSerializable, GLRenderableObject{

	private Rectangle rectangle;
	
	
	public Tile(int x, int y, int width, int height)
	{
		this.rectangle = new Rectangle(x, y, width, height);
	}
	
	public Tile(JSONObject o)
	{
		this.deserialize(o);
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
	}

	@Override
	public void update(double dt)
	{}

	@Override
	public Rectangle getBounds()
	{
		return this.rectangle;
	}

	@Override
	public JSONObject serialize()
	{
		return null;
	}

	@Override
	public void deserialize(JSONObject o)
	{
	}
}
