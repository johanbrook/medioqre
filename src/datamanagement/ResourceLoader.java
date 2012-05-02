package datamanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ResourceLoader {
	
	public static String loadStringFromAbsolutePath(String absolutePath)
	{
		InputStream inputStream;
		try {
			InputStream input = new FileInputStream(new File(absolutePath));
			String load = IOUtils.toString(input);
			return load;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String loadStringFromResourceFolder(String source)
	{
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("spritesheets/json/"+source);
			String load = IOUtils.toString(input);
			return load;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
