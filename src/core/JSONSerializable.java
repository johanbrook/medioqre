package core;

import org.json.JSONObject;

/**
 * An interface for serializing and deserialization to and from JSON.
 * 
 * @author John Barbero Unenge
 *
 */
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
