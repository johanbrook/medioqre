package tilemap;

import graphics.opengl.Sprite;

import java.util.HashMap;
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
		this.tileTypes = new HashMap<Integer, Tile>();
		int firstType = 0xffffffff;
		this.tileTypes.put(firstType, new Tile(new Sprite("tilesheet", 0, 0,
				16, 16), firstType, false));
		this.name = "test";
	}

	public String getName()
	{
		return this.name;
	}

	public Tile getTile(int tileType)
	{
		return this.tileTypes.get(Integer.valueOf(tileType));
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

			retObj.put("tilesheet", jsonArray);

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
