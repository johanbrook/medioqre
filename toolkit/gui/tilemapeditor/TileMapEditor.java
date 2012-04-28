package gui.tilemapeditor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JSplitPane;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TileMapEditor extends JFrame {

	public static void main(String[] args)
	{
		new TileMapEditor();
	}

	public TileMapEditor()
	{
		this.initGui();
	}

	private void initGui()
	{
		// Create Main split pane
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerSize(3);

		// Creating left split pane
		// Containing Canvas + tile selector
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setDividerSize(3);

		// Creating Tile Canvas and adding it to
		// split pane
		TileCanvas tileCanvas = new TileCanvas();
		splitPane_1.setLeftComponent(tileCanvas);

		// Creating tile selector and adding it to
		// split pane
		TileSelector tileSelector = new TileSelector();
		splitPane_1.setRightComponent(tileSelector);

		// Creating tile inspector
		TileInspector tileInspector = new TileInspector();
		tileInspector.setMaximumSize(new Dimension(100, 32767));

		// Adding tile inspector and inne split pane
		// to the main split pane
		splitPane.setRightComponent(tileInspector);
		splitPane.setLeftComponent(splitPane_1);

		splitPane.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e)
			{
			}

			@Override
			public void componentResized(ComponentEvent e)
			{
				((JSplitPane) e.getComponent()).setDividerLocation(e
						.getComponent().getWidth() - 200);
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{
			}

			@Override
			public void componentHidden(ComponentEvent e)
			{
			}
		});

		splitPane_1.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e)
			{
			}

			@Override
			public void componentResized(ComponentEvent e)
			{
				((JSplitPane) e.getComponent()).setDividerLocation(e
						.getComponent().getHeight() - 200);
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{
			}

			@Override
			public void componentHidden(ComponentEvent e)
			{
			}
		});

		// Window Listener
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e)
			{
			}

			@Override
			public void windowIconified(WindowEvent e)
			{
			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
			}

			@Override
			public void windowDeactivated(WindowEvent e)
			{
			}

			@Override
			public void windowClosing(WindowEvent e)
			{

				// "You have usaved changes, do you want to save theese before closing down?"
				int i = JOptionPane
						.showConfirmDialog(null,
								"You have usaved changes, do you want to save theese before closing down?");
				if (i == JOptionPane.NO_OPTION) {
					System.out.println("Not saving and sutting down!");
				} else if (i == JOptionPane.YES_OPTION) {
					System.out.println("Saving and sutting down!");
				} else {
					return;
				}
				System.exit(NORMAL);
			}

			@Override
			public void windowClosed(WindowEvent e)
			{}

			@Override
			public void windowActivated(WindowEvent e)
			{
			}
		});

		this.setMinimumSize(new Dimension(700, 500));
		this.setPreferredSize(new Dimension(1000, 700));
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}

}
