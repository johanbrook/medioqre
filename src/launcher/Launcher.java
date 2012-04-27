package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Launcher extends JFrame {

	private static final long serialVersionUID = -3189420915172593199L;

	public Launcher() {
		
		String os = System.getProperty("os.name").toLowerCase();
		boolean runningOnOSX = os.indexOf("mac") != -1; 
		
		if (runningOnOSX){
		
			
			
		}
		
		setTitle("Frank the Tank");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension launcherSize = new Dimension(800, 500);
		setBounds((screenSize.width / 2) - (launcherSize.width / 2),
				(screenSize.height / 2) - (launcherSize.height / 2),
				launcherSize.width, launcherSize.height);

		setVisible(true);

		setBackground(new Color(100, 200, 100));

	}

}
