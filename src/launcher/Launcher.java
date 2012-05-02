package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;

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

		setVisible(true);

		setBackground(new Color(100, 200, 100));
		getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(Launcher.class.getResource("/images/launcher/logo.png")));
		btnNewButton.setFocusable(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBounds(275, 50, 250, 100);
		getContentPane().add(btnNewButton);

	}
}
