package tilemap;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

public class TileMap implements JSONSerializable, GLRenderableObject {

	Tile[] tiles;

	public TileMap(int rows, int cols, int tileSize)
	{
		this.tiles = new Tile[rows * cols];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				this.tiles[y * cols + x] = new Tile(x*tileSize, y*tileSize, tileSize, tileSize);
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
		if (this.tiles != null) {
			for (int i = 0; i < this.tiles.length; i++) {
				this.tiles[i].render(this.tiles[i].getBounds(), target, canvas);
			}
		}
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public Rectangle getBounds()
	{
		throw new NotImplementedException();
	}

	@Override
	public JSONObject serialize()
	{
		try {
			JSONObject retObj = new JSONObject();
			JSONArray jSonArray = new JSONArray();
			for (int i = 0; i < this.tiles.length; i++) {
				jSonArray.put(this.tiles[i].serialize());
			}

			retObj.put("tiles", jSonArray);

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
			JSONArray jSonArray = o.getJSONArray("tiles");
			this.tiles = new Tile[jSonArray.length()];
			for (int i = 0; i < jSonArray.length(); i++) {
				this.tiles[i] = new Tile(jSonArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
