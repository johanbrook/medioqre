package main;

import controller.AppController;

/**
 * Main class. Application entry point.
 * 
 * @author Johan
 *
 */
public class Main {
	
	public static final String VERSION = "0.0.3";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length > 0 && args[0].equalsIgnoreCase("--logging"))
			tools.Logger.setLogginEnabled(true);
		
		new AppController();
	}
}
