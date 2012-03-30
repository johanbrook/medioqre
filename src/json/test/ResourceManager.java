package json.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

public class ResourceManager {

	public static void main(String[] args)
	{
		new ResourceManager();
	}

	public ResourceManager()
	{
		InputStream inputStream = this.getClass().getResourceAsStream("/gamedata/player.txt");
		String inputString = null;
		JSONObject jFather = null;
		try {
			inputString = IOUtils.toString(inputStream);
			
			try {
				jFather = new JSONObject(inputString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			System.out.println(jFather);
			System.out.println("InputStream: "+jFather.getJSONObject("player").getInt("mvspeed"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
