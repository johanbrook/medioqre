package graphics.opengl.tilemap;

import graphics.json.JSONSerializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A class used to map a tiletype to a tile that can be rendered.
 * 
 * @author John Barbero Unenge
 * 
 */
public class TileSheet implements JSONSerializable {

	private Map<Integer, Tile> tileTypes;
	private String name;

	/**
	 * Creates a tilesheet from the given JSONObject.
	 * 
	 * @param o
	 *            The JSONObject
	 */
	public TileSheet(JSONObject o) {
		this.deserialize(o);
	}

	/**
	 * Creates a tilesheet with no tiles.
	 */
	public TileSheet() {
		this.resetTiles();
	}

	// Other

	/**
	 * Delete all tiles.
	 */
	public void resetTiles() {
		this.tileTypes = new HashMap<Integer, Tile>();
	}

	/**
	 * Add a tile to the tilesheet.
	 * 
	 * @param type
	 *            The tiletype
	 * @param tile
	 *            The tile
	 */
	public void addTile(Integer type, Tile tile) {
		if (this.tileTypes == null)
			this.resetTiles();
		this.tileTypes.put(type, tile);
	}

	// Setters

	/**
	 * Set the name of the tilesheet.
	 * 
	 * @param name
	 *            The name of the tilesheet
	 */
	public void setName(String name) {
		this.name = name;
	}

	// Getters

	/**
	 * Get the name of this tilesheet.
	 * 
	 * @return The name of the tilesheet
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the tile of given type.
	 * 
	 * @param tileType
	 *            The type of tile to get
	 * @return The tile
	 */
	public Tile getTile(int tileType) {
		return this.tileTypes.get(Integer.valueOf(tileType));
	}

	/**
	 * Get a collection of all tiles in the tilesheet.
	 * 
	 * @return All the tiles in the tilesheet
	 */
	public Collection<Tile> getTiles() {
		return this.tileTypes.values();
	}

	// Interface
	@Override
	public JSONObject serialize() {
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
	public void deserialize(JSONObject o) {

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
