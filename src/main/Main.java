package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;


import launcher.Launcher;
import controller.AppController;
import static tools.Logger.*;

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
		
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("--logging")){
				setLogginEnabled(true);
				
				if(args.length > 1) {
					setLogMode(Integer.parseInt(args[1]));
				}
				else {
					setLogMode(LOG_ALL);
				}
			}
		}
		
		String os = System.getProperty("os.name").toLowerCase();
		boolean runningOnOSX = os.indexOf("mac") != -1; 
		
		if (runningOnOSX){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Frank The Tank");
			
			com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
			URL dockIconURL = Main.class.getResource("/images/launcher/appicon.png");
			Image icon = Toolkit.getDefaultToolkit().createImage(dockIconURL);
			app.setDockIconImage(icon);
			
		}
		
		new Launcher();
	}
}
