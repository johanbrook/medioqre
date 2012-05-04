package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;


import launcher.Launcher;
import controller.AppController;

/**
 * Main class. Application entry point.
 * 
 * @author Johan
 *
 */
public class Main {
	
	public static final String VERSION = "0.2";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if(os.indexOf("mac") != -1) OSXOptions();
		
		
		new Launcher();
	}
	
	
	private static void OSXOptions() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Frank The Tank");
		
		com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
		URL dockIconURL = Main.class.getResource("/images/launcher/appicon.png");
		Image icon = Toolkit.getDefaultToolkit().createImage(dockIconURL);
		app.setDockIconImage(icon);
	}
	
	private static void windowsOptions() {
		
	}
	
	private static void linuxOptions() {
		
	}
}
