package gui;

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
import controller.AudioController;

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

	JPanel mainPanel;
	JPanel optionsPanel;

	public Launcher() {
		setResizable(false);

		setTitle("Frank the Tank");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension launcherSize = new Dimension(640, 422);
		setBounds((screenSize.width / 2) - (launcherSize.width / 2),
				(screenSize.height / 2) - (launcherSize.height / 2),
				launcherSize.width, launcherSize.height);

		mainPanel = new LauncherMainPanel(16, 10, this);
		mainPanel.setVisible(true);

		optionsPanel = new OptionsPanel(this);
		optionsPanel.setVisible(false);

		getContentPane().add(mainPanel);
		getContentPane().add(optionsPanel);
		setVisible(true);

		AudioController.getInstance().playStartUpSound();

	}

	public void drawOptionPanel() {
		mainPanel.setVisible(false);
		optionsPanel.setVisible(true);

	}

	public void drawMainPanel() {
		mainPanel.setVisible(true);
		optionsPanel.setVisible(false);

	}

}
