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
		
		
		JButton frank = new JButton("");
		frank.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/frank.png")));
		frank.setFocusable(false);
		frank.setBorderPainted(false);
		frank.setBounds(4, 117, 224, 294);
		panel.add(frank);
		
		JButton logo = new JButton("");
		logo.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/logo.png")));
		logo.setFocusable(false);
		logo.setBorderPainted(false);
		logo.setBounds(220, 11, 392, 212);
		panel.add(logo);
		
		
		
		
		add(panel);
		setVisible(true);

	}
}
