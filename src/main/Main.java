package main;


import gui.Launcher;

import java.io.IOException;

import javax.imageio.ImageIO;

import tools.Logger;
import tools.factory.ObjectFactory;

import controller.AppController;

import static tools.Logger.*;

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

	public static final String VERSION = "0.7";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length > 0 && "--debug".equals(args[0])) {
			MODE = DEBUG;
		}

		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac") != -1)
			OSXOptions();

		if (MODE == PRODUCTION) {
			new Launcher();
		} else {
			new AppController().init();
		}
		
		// Logging format
		
		String loggingFormat = ObjectFactory.getConfigString("loggingFormat");
		tools.Logger.getInstance().setTimestampFormat(loggingFormat);
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
}
