package tilemap;

import graphics.opengl.Sprite;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import datamanagement.SharedTextures;

public class Tile implements JSONSerializable, GLRenderableObject{

	private Rectangle rectangle;
	
	
	public Tile()
	{
		
	}
	
	public Tile(JSONObject o)
	{
		this.deserialize(o);
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		if (object != null && object.intersectsRectangle(target)) {

			GL2 gl = canvas.getGL().getGL2();

			float rX1 = (float) ((2.0f * object.getX()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rX2 = (float) (2.0f * (object.getX() + object.getWidth()) - (float) target
					.getWidth()) / (float) target.getWidth();
			float rY1 = (float) (2.0f * (object.getY() + object.getHeight()) - (float) target
					.getHeight()) / (float) target.getHeight();
			float rY2 = (float) (2.0f * object.getY() - (float) target
					.getHeight()) / (float) target.getHeight();

			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(1.0f, 0.0f, 1.0f);
			
			gl.glVertex2f(rX1, -rY2);
			gl.glVertex2f(rX2, -rY2);
			gl.glVertex2f(rX2, -rY1);
			gl.glVertex2f(rX1, -rY1);

			gl.glEnd();
		}
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
