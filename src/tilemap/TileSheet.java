package tilemap;

import graphics.opengl.Sprite;

import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.JSONSerializable;

public class TileSheet implements JSONSerializable {

	private Map<Integer, Tile> tileTypes;
	private String name;

	public TileSheet(JSONObject o)
	{
		this.deserialize(o);
	}

	public TileSheet()
	{
		this.resetTiles();
	}

	public void resetTiles()
	{
		this.tileTypes = new HashMap<Integer, Tile>();
	}
	public void addTile(Integer type, Tile tile)
	{
		if (this.tileTypes == null) this.resetTiles();
		this.tileTypes.put(type, tile);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Tile getTile(int tileType)
	{
		return this.tileTypes.get(Integer.valueOf(tileType));
	}

	public Collection<Tile> getTiles()
	{
		return this.tileTypes.values();
	}
	
	@Override
	public JSONObject serialize()
	{
		try {
			JSONObject retObj = new JSONObject();

			JSONArray jsonArray = new JSONArray();

			for (Integer i : this.tileTypes.keySet()) {
				jsonArray.put(this.tileTypes.get(i).serialize());
			}

			retObj.put("tiletypes", jsonArray);

			return retObj;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void deserialize(JSONObject o)
	{

		JSONArray jsonArray;
		try {

			this.tileTypes = new HashMap<Integer, Tile>();

			jsonArray = o.getJSONArray("tiletypes");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				Tile tile = new Tile(jsonObject);

				this.tileTypes.put(tile.getType(), tile);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
