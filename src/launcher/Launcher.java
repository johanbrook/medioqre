package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.AppController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Component;

/**
 * 
 * Launcher for Frank the Tank
 * 
 * 
 * @author chrisnordqvist
 * 
 */

public class Launcher extends JFrame {

	private static final long serialVersionUID = -3189420915172593199L;

	JPanel panel;

	public Launcher() {
		setResizable(false);

		setTitle("Frank the Tank");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension launcherSize = new Dimension(640, 422);
		setBounds((screenSize.width / 2) - (launcherSize.width / 2),
				(screenSize.height / 2) - (launcherSize.height / 2),
				launcherSize.width, launcherSize.height);

		panel = new AnimatedBackgoundPanel(16, 10, 640, 400);
		panel.setVisible(true);
		
		panel.setLayout(null);
		
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/frank.png")));
		button.setFocusable(false);
		button.setBorderPainted(false);
		button.setBounds(24, 120, 224, 294);
		panel.add(button);
		
		
		
		
		add(panel);
		setVisible(true);

	}
}
