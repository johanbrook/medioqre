package tilemap;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import core.Size;

public class TileMap implements JSONSerializable, GLRenderableObject {

	private Tile[][] tiles;
	private Size tileSize = new Size(10, 10);

	// Temp
	private Rectangle tileRenderRect = new Rectangle(0, 0, 0, 0);

	public TileMap(int rows, int columns)
	{
		tiles = new Tile[columns][rows];
		this.clearTileMap(new Tile());
	}

	public void clearTileMap(Tile clearTile)
	{

		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				this.tiles[x][y] = clearTile;
			}
		}

	}

	public TileMap(JSONObject o)
	{
		this.deserialize(o);
	}

	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		System.out.println("Rendering Tilemap!");
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				tileRenderRect.setX(x * tileSize.getWidth());
				tileRenderRect.setY(y * tileSize.getHeight());
				tileRenderRect.setWidth(tileSize.getWidth());
				tileRenderRect.setHeight(tileSize.getHeight());
				
				System.out.println("Rendering Tile at: "+x+","+y);
				System.out.println(tileRenderRect);
				this.tiles[x][y].render(tileRenderRect, target, canvas);
			}
		}
	}

	@Override
	public void update(double dt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getBounds()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject serialize()
	{
		try {
			JSONObject retObj = new JSONObject();

			retObj.put("test1", "Derp");
			retObj.put("test2", "Herp");

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
			System.out.println("test1: " + o.getString("test1"));
			System.out.println("test2: " + o.getString("test2"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
