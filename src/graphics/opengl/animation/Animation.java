package graphics.opengl.animation;

import org.json.JSONException;
import org.json.JSONObject;

import graphics.json.JSONSerializable;

public class Animation implements JSONSerializable{

	private int animationTag;
	private Sprite[] sprites;
	
	public Animation() {
		
	}
	
	public Animation(JSONObject o) throws JSONException{
		this.deserialize(o);
	}
	
	// Setters
	public void setAnimationTag(int animationTag) {
		this.animationTag = animationTag;
	}
	
	// Getters
	public int getAnimationTag() {
		return this.animationTag;
	}
	
	@Override
	public JSONObject serialize() throws JSONException {
		JSONObject retObj = new JSONObject();
		
		retObj.put("animationTag", this.animationTag);
		return retObj;
	}

	@Override
	public void deserialize(JSONObject o) throws JSONException{
		this.animationTag = o.getInt("animationTag");
	}

}
