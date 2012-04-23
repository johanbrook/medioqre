package tilemap;

import org.json.JSONObject;

import core.JSONSerializable;

public class Tile implements JSONSerializable{

	public Tile(JSONObject o) {
		this.deserialize(o);
	}
	
	@Override
	public JSONObject serialize()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserialize(JSONObject o)
	{
		// TODO Auto-generated method stub
		
	}

}
