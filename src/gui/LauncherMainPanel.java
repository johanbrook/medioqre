package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.AppController;

public class LauncherMainPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369702432499487888L;

	private Timer timer;
	private int delay = 100;
	private Rectangle[][] rect;
	private double[][] rnd;
	private int rows;
	private int cols;

	private Launcher launcher;

	public LauncherMainPanel(int rows, int cols, int width, int height,
			Launcher container) {
		setBounds(new Rectangle(0, 0, 640, 400));
		launcher = container;
		setLayout(null);

		setBounds(0, 0, 790, 509);

		this.rows = rows;
		this.cols = cols;

		rect = new Rectangle[rows][cols];
		rnd = new double[rows][cols];

		int rw = getWidth() / rows;
		int rh = getHeight() / cols;

		for (int r = 0; r < rows; r++) {

			for (int c = 0; c < cols; c++) {

				rect[r][c] = new Rectangle(r * rw, c * rh, rw, rh);
				rnd[r][c] = Math.random();
			}

		}

		JButton frank = new JButton("");
		frank.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/frank.png")));
		frank.setFocusable(false);
		frank.setBorderPainted(false);
		frank.setBounds(4, 117, 224, 294);
		frank.setContentAreaFilled(false);
		add(frank);

		JButton logo = new JButton("");
		logo.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/logo.png")));
		logo.setFocusable(false);
		logo.setBorderPainted(false);
		logo.setBounds(220, 11, 392, 212);
		logo.setContentAreaFilled(false);
		add(logo);

		JButton startButton = new JButton("");
		startButton.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/startButton.png")));
		startButton.setFocusable(false);
		startButton.setBorderPainted(false);
		startButton.setBounds(300, 260, 239, 26);
		startButton.setContentAreaFilled(false);
		add(startButton);

		JButton optionsButton = new JButton("");
		optionsButton.setIcon(new ImageIcon(Launcher.class
				.getResource("/images/launcher/optionsButton.png")));
		optionsButton.setFocusable(false);
		optionsButton.setBorderPainted(false);
		optionsButton.setBounds(300, 320, 239, 26);
		optionsButton.setContentAreaFilled(false);
		add(optionsButton);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				launcher.dispose();
				
				// Create main game
				AppController game = new AppController();
				game.init();
			}
		});

		optionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				launcher.drawOptionPanel();
			}
		});

		setBackground(new Color(0, 0, 0));

		timer = new Timer(delay, this);

		timer.start();

	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		for (int r = 0; r < rows; r++) {

			for (int c = 0; c < cols; c++) {

				int x = rect[r][c].x;
				int y = rect[r][c].y;
				int dw = rect[r][c].width;
				int dh = rect[r][c].height;

				g2d.setColor(new Color(10, 20, 10));

				double p = Math.random();

				if (p > 0.99) {
					g2d.setColor(new Color(30, 60, 30));
				} else if (p > 0.85) {
					g2d.setColor(new Color(20, 40, 20));
				} else if (p > 0.6) {
					g2d.setColor(new Color(15, 30, 15));
				}

				g2d.fillRect(x, y, dw, dh);

			}

		}

		super.paintComponents(g);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();

	}
}
