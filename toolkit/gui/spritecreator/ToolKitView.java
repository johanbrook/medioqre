package gui.spritecreator;

import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JSplitPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.JButton;

public class ToolKitView extends JFrame implements ActionListener {

	private Tile activeTile;
	private Tile[][] tiles;

	public ToolKitView() {
		this.activeTile = new Tile(0, 0, 0, null);
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_1.setLeftComponent(splitPane_2);

		JPanel panel_1 = new TileMapEditor();
		panel_1.setPreferredSize(new Dimension(600, 600));
		panel_1.setMinimumSize(new Dimension(40, 40));
		splitPane_2.setLeftComponent(panel_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setPreferredSize(new Dimension(100, 10));
		splitPane_2.setRightComponent(panel_2);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(10, 100));
		splitPane_1.setRightComponent(panel);

		JButton btnTile_1 = new JButton("Tile1");
		btnTile_1.setActionCommand("tile1");
		panel.add(btnTile_1);
		btnTile_1.addActionListener(this);

		JButton btnTile_2 = new JButton("Tile2");
		btnTile_2.setActionCommand("tile2");
		panel.add(btnTile_2);
		btnTile_2.addActionListener(this);

		JButton btnTile_3 = new JButton("Tile3");
		btnTile_3.setActionCommand("tile3");
		panel.add(btnTile_3);
		btnTile_3.addActionListener(this);

		JButton btnTile_4 = new JButton("Tile4");
		btnTile_4.setActionCommand("tile4");
		panel.add(btnTile_4);
		btnTile_4.addActionListener(this);

		// Make a tree list with all the nodes, and make it a JTree

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("files");
		String blaj;
		try {
			blaj = (String) ToolKitView.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath();
			blaj += "/files";
			File rootFile = new File(blaj);
			System.out.println(blaj);
			if (rootFile.list() != null) {

				for (int i = 0; i < rootFile.list().length; i++) {
					root.add(new DefaultMutableTreeNode(rootFile.list()[i]));
				}
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JTree tree = new JTree(root);
		tree.setRootVisible(true);

		// Add a listener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				System.out.println("You selected " + node);
			}
		});

		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setPreferredSize(new Dimension(200, 10));
		scrollpane.setViewportView(tree);
		splitPane.setLeftComponent(scrollpane);

		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("tile1")) {
			try {
				String s = (String) ToolKitView.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath();
				s += "/files/stone_1.png";
				activeTile = new Tile(0, 0, 1, ImageIO.read(new File(s)));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("tile2")) {
			try {
				String s = (String) ToolKitView.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath();
				s += "/files/grass.png";
				activeTile.setImage(ImageIO.read(new File(s)));
				activeTile = new Tile(0, 0, 2, ImageIO.read(new File(s)));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("tile3")) {
			try {
				String s = (String) ToolKitView.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath();
				s += "/files/metal.png";
				activeTile = new Tile(0, 0, 3, ImageIO.read(new File(s)));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("tile4")) {
			try {
				String s = (String) ToolKitView.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI().getPath();
				s += "/files/water.png";

				activeTile = new Tile(0, 0, 4, ImageIO.read(new File(s)));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class Tile {
		private int x, y, tag;
		private Image img;

		public Tile(int x, int y, int tag, Image img) {
			this.x = x;
			this.y = y;
			this.tag = tag;
			this.img = img;
		}

		public Tile(Tile tile, int x, int y) {
			this(x, y, tile.tag, tile.img);
		}
		public void setImage(Image img) {
			this.img = img;
		}
		public Image getImage() {
			return this.img;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		public void setX(int x) {
			this.x = x;
		}
		public void setY(int y) {
			this.y = y;
		}
	}

	public class TileMapEditor extends JPanel
			implements
				MouseListener,
				MouseMotionListener {
		private final int tileSize = 64;

		public TileMapEditor() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void paint(Graphics g) {
			g.clearRect(0, 0, this.getWidth(), this.getHeight());

			for (int xi = 0; xi < tiles.length; xi++) {
				for (int yi = 0; yi < tiles[xi].length; yi++) {
					g.drawImage(tiles[xi][yi].getImage(), tiles[xi][yi].getX(),
							tiles[xi][yi].getY(), tileSize, tileSize, null);
				}
			}

			g.drawImage(activeTile.getImage(), activeTile.getX(),
					activeTile.getY(), tileSize, tileSize, null);
		}

		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);

			int w = (this.getWidth() / tileSize) + 1;
			int h = (this.getHeight() / tileSize) + 1;
			if (tiles == null || tiles.length != w || tiles[0].length != h) {
				Tile[][] old = tiles;
				tiles = new Tile[w][h];

				// if (old != null) {
				// for (int xi = 0; xi < w; xi++) {
				// for (int yi = 0; yi < h; yi++) {
				// Tile tmp = null;
				// if (old.length < xi && old[xi].length < yi) tmp =
				// old[xi][yi];
				// if (tmp != null) tiles[xi][yi] = tmp;
				// else {
				// tiles[xi][yi] = new Tile(xi * tileSize, yi * tileSize, 0);
				// tiles[xi][yi].setImage(activeTile.getImage());
				// }
				// }
				// }
				// } else {
				for (int xi = 0; xi < tiles.length; xi++) {
					for (int yi = 0; yi < tiles[xi].length; yi++) {
						tiles[xi][yi] = new Tile(xi * tileSize, yi * tileSize,
								0, null);
						tiles[xi][yi].setImage(activeTile.getImage());
					}
				}
				// }

			}
			System.out.println("b4");
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			int xTile = arg0.getX() / tileSize;
			int yTile = arg0.getY() / tileSize;

			tiles[xTile][yTile] = new Tile(activeTile, xTile * tileSize, yTile
					* tileSize);

			System.out.println("Click at: (" + xTile + "," + yTile + ")");
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		public void mouseMoved(MouseEvent arg0) {
			int xTile = arg0.getX() / tileSize;
			int yTile = arg0.getY() / tileSize;
			System.out.println("Moved to: (" + xTile + "," + yTile + ")");
			activeTile.setX(xTile * tileSize);
			activeTile.setY(yTile * tileSize);
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}
}
