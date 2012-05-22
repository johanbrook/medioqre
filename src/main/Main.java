package main;


import gui.Launcher;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tools.Logger;

import controller.AppController;

import org.lwjgl.LWJGLUtil;
import org.simplericity.macify.eawt.*;


import static controller.AppController.MODE;
import static controller.AppController.PRODUCTION;
import static controller.AppController.DEBUG;

/**
 * Main class. Application entry point.
 * 
 * @author Johan
 * 
 */
public class Main {

	public static final String VERSION = "0.8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		setAudioNativeDir();
		
		if (args.length > 0 && "--debug".equals(args[0])) {
			MODE = DEBUG;
		}

		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac") != -1)
			OSXOptions();

		if (os.indexOf("windows") != -1)
			windowsOptions();

		if (MODE == PRODUCTION) {
			new Launcher();
		} else {
			JFrame f = new JFrame();
			f.setPreferredSize(new Dimension(20 * 48, 12 * 48));
			f.pack();
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			new AppController(f).init();
		}

	}

	private static void OSXOptions() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"Frank The Tank");

		Application app = new DefaultApplication();
		try {
			app.setApplicationIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream("images/launcher/appicon.png")));
		} catch (IOException e) {
			Logger.log("Couldn't load dock icon!");
			e.printStackTrace();
		}

	}

	private static void windowsOptions() {

	}

	private static void linuxOptions() {

	}

	private static void setAudioNativeDir(){
		System.setProperty( "java.library.path", new File(new File(System.getProperty("user.dir"), "libs/lwjgl/native"), LWJGLUtil.getPlatformName()).getAbsolutePath()) ;

		// Field borrowed from http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
		Field field;
		try {
			field = ClassLoader.class.getDeclaredField( "sys_paths" );
			field.setAccessible( true );
			field.set( null, null );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}