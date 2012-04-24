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
			inputStream = new FileInputStream(new File(absolutePath));
			String inputString = IOUtils.toString(inputStream);
			return inputString;
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
			
			System.out.println(ResourceLoader.class.getResource("res/spritesheets/frank.actor"));
			String s = IOUtils.toString(ResourceLoader.class.getResourceAsStream("res/spritesheets/frank.actor"));// + source));
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
