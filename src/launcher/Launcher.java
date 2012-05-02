package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import controller.AppController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

	public Launcher() {

		setTitle("Frank the Tank");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension launcherSize = new Dimension(800, 500);
		setBounds((screenSize.width / 2) - (launcherSize.width / 2),
				(screenSize.height / 2) - (launcherSize.height / 2),
				launcherSize.width, launcherSize.height);

		getContentPane().setLayout(null);

		// Logo, Button for now, since I'd like an easter egg here
		JButton Logo = new JButton("");
		Logo.setBounds(265, 50, 330, 120);
		Logo.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/logo.png")));
		Logo.setFocusable(false);
		Logo.setBorderPainted(false);
		getContentPane().add(Logo);

		/*
		 * Start Button
		 */
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.setBounds(370, 220, 120, 40);
		getContentPane().add(btnStartGame);

		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				dispose();

				// Create main game
				new AppController();
			}
		});

		/*
		 * Options Button
		 */

		JButton btnOptions = new JButton("Options");
		btnOptions.setBounds(370, 280, 120, 40);
		getContentPane().add(btnOptions);
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(Launcher.class.getResource("/images/launcher/frank.png")));
		button.setFocusable(false);
		button.setBorderPainted(false);
		button.setBounds(20, 184, 224, 294);
		getContentPane().add(button);

		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO Fix options view!
				
				System.out.println("Options pressed");
			}
		});

		setVisible(true);
		setBackground(new Color(100, 200, 100));

	}
}
