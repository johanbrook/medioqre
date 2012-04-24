package core;

import org.json.JSONObject;

public interface JSONSerializable {

	/**
	 * Generates a JSON object representing the JSONSerializable.
	 * @return A JSON object representing the JSONSerializable.
	 */
	public JSONObject serialize();
	/**
	 * Restore the state from a JSON object.
	 * @param o The JSON object to restore state from.
	 */
	public void deserialize(JSONObject o);
	
}
