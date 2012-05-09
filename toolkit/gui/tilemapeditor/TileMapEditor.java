package gui.tilemapeditor;

import graphics.tools.PixelCastingTool;
import gui.tilemapeditor.subviews.TileCanvas;
import gui.tilemapeditor.subviews.TileInspector;
import gui.tilemapeditor.subviews.TileSelector;
import gui.tilemapeditor.tilesheeteditor.TileSheetEditor;
import gui.tilemapeditor.util.TileSelectorListener;

import javax.imageio.ImageIO;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JSeparator;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import datamanagement.ResourceLoader;

import tilemap.Tile;
import tilemap.TileMap;
import tilemap.TileSheet;

public class TileMapEditor extends JFrame implements TileSelectorListener {

	private File currentFile;

	private TileMap currentTileMap;
	private TileSheet currentTileSheet;

	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;

	private TileCanvas tileCanvas;
	private TileSelector	tileSelector;
	private TileInspector tileInspector;

	public static void main(String[] args)
	{
		new TileMapEditor();
	}

	public TileMapEditor()
	{
		this.initGui();
		this.reloadGui();
	}

	private void saveFile(File file)
	{
		System.out.println("Method for saving file");
		if (file == null) {
			System.out.println("Filename is null");
			JFileChooser chooser = new JFileChooser();
			int input = chooser.showSaveDialog(getParent());
			if (input == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				saveFile(currentFile);
			}
		} else {
			BufferedImage img = new BufferedImage(this.currentTileMap.getTileMapSize().getWidth(), this.currentTileMap.getTileMapSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
						
			this.currentTileMap.fillPixelArrayWithTiles(pixels);
					
			try {
				ImageIO.write(img, "png", file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Saving file: " + file);
		}
	}

	private void newTileMap()
	{
		System.out.println("Creating new tilemap.");
		int rows = Integer.valueOf(JOptionPane
				.showInputDialog("Number of rows: "));
		int columns = Integer.valueOf(JOptionPane
				.showInputDialog("Number of columns: "));
			
		TileSheet t = this.currentTileSheet == null ? new TileSheet() : this.currentTileSheet;
		
		this.currentTileMap = new TileMap(rows, columns, t, null);
		this.currentTileMap.randomizeTileMap();

		this.tileCanvas.setTileMap(this.currentTileMap);

		this.reloadGui();
	}

	private void loadTileMap(File file)
	{
		this.currentFile = file;
				
		this.currentTileMap = ResourceLoader.loadTileMapFromAbsolutePath(this.currentFile.getAbsolutePath());
		this.currentTileMap.setTileSheet(this.currentTileSheet);

		this.tileCanvas.setTileMap(this.currentTileMap);
		
		System.out.println("Loading tilemap: " + file);

	}

	private void createNewTileSheet()
	{
		new TileSheetEditor();
		System.out.println("Creating new tilesheet.");
	}

	private void loadTileSheet(File file)
	{
		
		try {
			this.currentTileSheet = new TileSheet(new JSONObject(
					IOUtils.toString(new FileInputStream(file))));
			System.out.println("load TileSheet: " + file.getAbsolutePath());
			
			this.reloadGui();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Could not load: " + file.getAbsolutePath());
	}

	private void clearTileMap()
	{
		if (this.tileSelector == null) return;
		if (this.tileSelector.getSelectedTile() == null) return;
		if (this.currentTileMap == null) return;
		
		this.currentTileMap.clearTileMap(this.tileSelector.getSelectedTile().getType());
		System.out.println("Clearing tilemap.");
	}

	private void setTileMapSize(int rows, int columns)
	{
		System.out
				.println("Setting tilemap size to: " + rows + " x " + columns);
	}

	private void reloadGui()
	{	
		if (this.currentTileMap == null) {
			// Update GUI
			this.mntmSave.setEnabled(false);
			this.mntmSaveAs.setEnabled(false);
		} else {
			// Update GUI
			this.mntmSave.setEnabled(true);
			this.mntmSaveAs.setEnabled(true);
			
			// Update Logic
			this.currentTileMap.setTileSheet(this.currentTileSheet);
		}
		if (this.currentTileSheet != null && this.tileSelector != null) {
			this.tileSelector.setTileSheet(this.currentTileSheet);
		}
	}

	private void initGui()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("TileMap");
		menuBar.add(mnFile);

		JMenuItem mntmNewTilemap = new JMenuItem("New Tilemap...");
		mntmNewTilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.META_MASK));
		mnFile.add(mntmNewTilemap);
		mntmNewTilemap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newTileMap();
			}
		});

		JMenuItem mntmLoadTilemap = new JMenuItem("Open Tilemap...");
		mntmLoadTilemap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.META_MASK));
		mnFile.add(mntmLoadTilemap);
		mntmLoadTilemap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (currentTileSheet == null) {
					JOptionPane.showMessageDialog(getParent(), "Y U NO LOAD TILESHEET FIRST?  ლ(ಠ益ಠლ)");
					return;
				}
				
				JFileChooser chooser = new JFileChooser();
				int input = chooser.showOpenDialog(getParent());
				if (input == JFileChooser.APPROVE_OPTION)
					loadTileMap(chooser.getSelectedFile());
			}
		});

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.META_MASK));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveFile(currentFile);
			}
		});

		mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.SHIFT_MASK | InputEvent.META_MASK));
		mnFile.add(mntmSaveAs);

		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);

		JMenuItem mntmSize = new JMenuItem("Size...");
		mnFile.add(mntmSize);

		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);

		JMenuItem mntmClear = new JMenuItem("Clear With Active Tile");
		mnFile.add(mntmClear);
		mntmClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int input = JOptionPane.showConfirmDialog(null,
						"Do you really want to clear the tilemap?",
						"Clear tilemap", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION)
					clearTileMap();
			}
		});
		mntmSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int rows = Integer.valueOf(JOptionPane
						.showInputDialog("Number of rows: "));
				int columns = Integer.valueOf(JOptionPane
						.showInputDialog("Number of columns: "));
				setTileMapSize(rows, columns);
			}
		});
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveFile(null);
			}
		});

		JMenu mnTilesheet = new JMenu("TileSheet");
		menuBar.add(mnTilesheet);

		JMenuItem mntmCreateNewTilesheet = new JMenuItem(
				"Create New TileSheet...");
		mnTilesheet.add(mntmCreateNewTilesheet);
		mntmCreateNewTilesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createNewTileSheet();
			}
		});

		JSeparator separator = new JSeparator();
		mnTilesheet.add(separator);

		JMenuItem mntmLoadTileSheet = new JMenuItem("Load TileSheet...");
		mnTilesheet.add(mntmLoadTileSheet);
		mntmLoadTileSheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				JFileChooser chooser = new JFileChooser();
				int input = chooser.showOpenDialog(getParent());
				if (input == JFileChooser.APPROVE_OPTION) {
					loadTileSheet(chooser.getSelectedFile());
				}
			}
		});

		// Create Main split pane
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerSize(3);

		// Creating left split pane
		// Containing Canvas + tile selector
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setEnabled(false);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setDividerSize(3);

		// Creating Tile Canvas and adding it to
		// split pane
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		tileCanvas = new TileCanvas(glC);
		splitPane_1.setLeftComponent(tileCanvas);

		// Creating tile selector and adding it to
		// split pane
		
		this.tileSelector = new TileSelector(glC);
		this.tileSelector.addTileSelectorListener(this);
		splitPane_1.setRightComponent(this.tileSelector);

		// Creating tile inspector
		tileInspector = new TileInspector();
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
						.getComponent().getWidth() - 220);
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
					saveFile(currentFile);
				} else {
					return;
				}
				System.exit(NORMAL);
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
			}

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

	@Override
	public void didSelectTile(Tile selectedTile)
	{
		this.tileCanvas.didSelectTile(selectedTile);
		this.tileInspector.setTile(selectedTile);
	}

}
