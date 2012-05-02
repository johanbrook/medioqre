package tilemap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import graphics.opengl.Sprite;

import javax.media.opengl.GLAutoDrawable;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import core.Size;

public class TileMap implements JSONSerializable, GLRenderableObject {

	private TileSheet tileSheet;
	private int[][] tiles;
	private Size tileSize = new Size(1, 1);
	private Size tileMapSize;
	
	// Temp
	private Rectangle tileRenderRect;
	
	public TileMap(int rows, int columns, TileSheet tileSheet)
	{
		tiles = new int[columns][rows];
		this.tileMapSize = new Size(columns, rows);
		
		this.tileSheet = tileSheet;
		
		this.clearTileMap(0xffffffff);
	}

	public void clearTileMap(int clearTile)
	{
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				this.tiles[x][y] = clearTile;
			}
		}

	}
	public Size getTileMapSize()
	{
		return this.tileMapSize;
	}

	public TileMap(JSONObject o)
	{
		this.deserialize(o);
	}

	public void setTileSheet(TileSheet tileSheet)
	{
		this.tileSheet = tileSheet;
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{
		if (this.tileRenderRect == null) this.tileRenderRect = new Rectangle(0, 0, 0, 0);
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				tileRenderRect.setX(x * tileSize.getWidth());
				tileRenderRect.setY(y * tileSize.getHeight());
				tileRenderRect.setWidth(tileSize.getWidth());
				tileRenderRect.setHeight(tileSize.getHeight());
				
				this.tileSheet.getTile(this.tiles[x][y]).render(tileRenderRect, target, canvas);
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

			retObj.put("tilesheet", this.tileSheet.getName());
			
			JSONArray cols = new JSONArray();
			for (int[] i : this.tiles) {
				JSONArray rows = new JSONArray();
				for (int j : i) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("i", ""+j);
					rows.put(jsonObject);
				}
				cols.put(rows);
			}
			retObj.put("tiles", cols);
			
			retObj.put("tilewidth", this.tileSize.getWidth());
			retObj.put("tileheight", this.tileSize.getHeight());
			retObj.put("tilemapwidth", this.tileMapSize.getWidth());
			retObj.put("tilemapheight", this.tileMapSize.getHeight());

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

//			this.tileSheet = new TileSheet(o);
			
			this.tiles = null;
			
			JSONArray cols = o.getJSONArray("tiles");
			for (int i = 0; i < cols.length(); i++) {
				JSONArray rows = cols.getJSONArray(i);
				for (int j = 0; j < rows.length(); j++) {
					if (this.tiles == null) this.tiles = new int[cols.length()][rows.length()];
					JSONObject jsonObject = rows.getJSONObject(j);
					this.tiles[i][j] = jsonObject.getInt("i");
				}
			}
			
			this.tileSize = new Size(o.getInt("tilewidth"), o.getInt("tileheight"));
			this.tileMapSize = new Size(o.getInt("tilemapwidth"), o.getInt("tilemapheight"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
