package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedBackgoundPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369702432499487888L;

	private Timer timer;
	private int delay = 300;
	private Rectangle[][] rect;
	private double[][] rnd;
	private int rows;
	private int cols;
	private float delta = 0.1f;

	public AnimatedBackgoundPanel() {
		// this(8, 8);
	}

	public AnimatedBackgoundPanel(int rows, int cols, int width, int height) {

		setBounds(0, 0, width, height);

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

		timer = new Timer(delay, this);
		timer.start();

	}

	public void paint(Graphics g) {

		super.paintComponents(g);

		Graphics2D g2d = (Graphics2D) g;

		for (int r = 0; r < rows; r++) {

			for (int c = 0; c < cols; c++) {

				int x = rect[r][c].x;
				int y = rect[r][c].y;
				int dw = rect[r][c].width;
				int dh = rect[r][c].height;

				g2d.setColor(new Color(10, 20, 10));

				double p = Math.random();

				if (p > 0.85)
					g2d.setColor(new Color(50, 100, 50));

				if (p > 0.99)
					g2d.setColor(new Color(100, 200, 100));

				g2d.fillRect(x, y, dw, dh);

			}

		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (delta < 0.9f) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					rnd[r][c] = Math.random();
				}

			}
		}

		delta = (delta + 0.1f) % 100;
		repaint();

	}

}
