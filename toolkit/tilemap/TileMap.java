package tilemap;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;

public class TileMap implements JSONSerializable, GLRenderableObject{

	private Tile[][] tiles;
	private Rectangle viewPort;
	
	// Setters
	public void setViewPort(Rectangle r)
	{
		this.viewPort = r;
	}
	public void moveViewPortTo(int x, int y)
	{
		this.viewPort.moveTo(x, y);
	}
	public void moveViewPortBy(int x, int y)
	{
		this.viewPort.moveBy(x, y);
	}
	
	// Getters
	public Rectangle getViewPort()
	{
		return this.viewPort;
	}
	public int getRows()
	{
		if (this.tiles != null) return this.tiles.length;
		return 0;
	}
	public int getColumns()
	{
		if (this.tiles != null && this.tiles[0] != null) return this.tiles[0].length;
		return 0;
	}
	
	
	// Constructors
	public TileMap(int rows, int columns, Rectangle viewPort)
	{
		this.tiles = new Tile[columns][rows];
		this.viewPort = viewPort;
	}
	
	
	public TileMap(JSONObject o) {
		this.deserialize(o);
	}
	
	// JSONSerializable
	@Override
	public JSONObject serialize()
	{
		JSONObject retObj = new JSONObject();
		
		try {
			JSONObject jsViewPort = new JSONObject();
			jsViewPort.put("x", this.viewPort.getX());
			jsViewPort.put("y", this.viewPort.getY());
			jsViewPort.put("width", this.viewPort.getWidth());
			jsViewPort.put("height", this.viewPort.getHeight());
			
			retObj.put("viewport", jsViewPort);
			
			JSONArray jsTiles = new JSONArray();
			for (int i = 0; i < this.tiles.length; i++) {
				JSONArray jsTmp = new JSONArray();
				for (int j = 0; j < this.tiles[i].length; j++) {
					jsTmp.put(this.tiles[i][j].serialize());
				}
				jsTiles.put(jsTmp);
			}
			
			retObj.put("tiles", jsTiles);
			
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
			JSONObject jsViewPort = o.getJSONObject("viewport");
			int x = jsViewPort.getInt("x");
			int y = jsViewPort.getInt("y");
			int width = jsViewPort.getInt("width");
			int height = jsViewPort.getInt("height");
			
			this.viewPort = new Rectangle(x, y, width, height);
			
			this.tiles = null;
			JSONArray jsTiles = o.getJSONArray("tiles");
			for (int i = 0; i < jsTiles.length(); i++) {
				JSONArray jsTmp = jsTiles.getJSONArray(i);
				if (this.tiles == null) {
					this.tiles = new Tile[jsTiles.length()][jsTmp.length()];
				}
				for (int j = 0; j < jsTmp.length(); j++) {
					this.tiles[i][j] = new Tile((jsTmp.getJSONObject(j)));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Failed to deserialize TileMap!");
	}
	
	// Rendering 
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
//		Rendering magic goes here
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				
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
}
