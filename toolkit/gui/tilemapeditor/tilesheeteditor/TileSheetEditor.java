package gui.tilemapeditor.tilesheeteditor;

import graphics.opengl.Sprite;

import java.awt.Dimension;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import core.Rectangle;

import tilemap.Tile;
import tilemap.TileSheet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

public class TileSheetEditor implements GLEventListener {

	private File		currentFile;
	private TileSheet	currentTileSheet;
	private File		currentTextureFile;
	private Texture		currentTexture;
	private Tile		currentTile;

	public static void main(String[] arg)
	{
		new TileSheetEditor();
	}

	public TileSheetEditor()
	{
		this.initGui();
		this.updateGui();
	}

	public void loadTexture(File file)
	{
		System.out.println("Load texture: " + file.getAbsolutePath());
		this.currentTextureFile = file;
		this.currentTexture = null;
	}

	public void newTileSheet()
	{
		this.currentTileSheet = new TileSheet();
		System.out.println("Create new TileSheet");
		this.updateGui();
	}

	public void loadTileSheet(File file)
	{
		try {
			this.currentTileSheet = new TileSheet(new JSONObject(
					IOUtils.toString(new FileInputStream(file))));
			System.out.println("load TileSheet: " + file.getAbsolutePath());
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Couldn't load: " + file.getAbsolutePath());
	}

	public void saveTileSheet(File file)
	{
		this.currentFile = file;
		
		System.out.println(this.currentTileSheet.serialize());
		System.out.println("Save TileSheet: " + file.getAbsolutePath());
	}

	public void updateGui()
	{
		if (this.currentTileSheet == null) {
			mntmSave.setEnabled(false);
			mntmSaveAs.setEnabled(false);

			// Disable all gui
			tfX.setEnabled(false);
			tfY.setEnabled(false);
			tfWidth.setEnabled(false);
			tfHeight.setEnabled(false);
			tfType.setEnabled(false);
			chbxColl.setEnabled(false);
			btnAdd.setEnabled(false);
			btnRemove.setEnabled(false);
			btnSave.setEnabled(false);

		} else {
			mntmSave.setEnabled(true);
			mntmSaveAs.setEnabled(true);

			// Enable all gui
			tfX.setEnabled(true);
			tfY.setEnabled(true);
			tfWidth.setEnabled(true);
			tfHeight.setEnabled(true);
			tfType.setEnabled(true);
			chbxColl.setEnabled(true);
			btnAdd.setEnabled(true);
			btnRemove.setEnabled(true);
			btnSave.setEnabled(true);
		}

		if (this.liTiles.getSelectedIndex() != -1 && this.tiles != null) {
			Tile tile = this.tiles.get(this.liTiles.getSelectedIndex());
			if (tile != null) {
				tfX.setText("" + tile.getSprite().getX());
				tfY.setText("" + tile.getSprite().getY());
				tfWidth.setText("" + tile.getSprite().getWidth());
				tfHeight.setText("" + tile.getSprite().getHeight());
				tfType.setText("" + tile.getType());
				chbxColl.setSelected(tile.isCollidable());
			}
		}

		liTiles.setModel(new AbstractListModel() {
			public int getSize()
			{
				return tiles.size();
			}

			public Object getElementAt(int index)
			{
				return tiles.get(index);
			}
		});
	}

	// JButton actions
	public void addTile()
	{
		System.out.println("Add new tile");
		this.tiles.add(new Tile(new Sprite("tilesheet", 0, 0, 0, 0),
				0xffffffff, false));
		// this.tiles.add("Hello" + this.tiles.size());
		this.updateGui();
	}

	public void removeTile()
	{
		this.tiles.remove(this.currentTile);
		this.updateGui();
	}

	public void selectTile(int i)
	{
		this.currentTile = this.tiles.get(i);
	}

	public void saveTile()
	{
		if (this.currentTile != null) {
			Tile tile = this.currentTile;
			if (tile != null) {
				tile.getSprite().setX(Integer.valueOf(tfX.getText()));
				tile.getSprite().setY(Integer.valueOf(tfY.getText()));
				tile.getSprite().setWidth(Integer.valueOf(tfWidth.getText()));
				tile.getSprite().setHeight(Integer.valueOf(tfHeight.getText()));
				tile.setType(Integer.valueOf(tfType.getText()));
				tile.setCollidable(chbxColl.isSelected());
			}
		}
	}

	// Ivar gui elements
	private JMenuItem	mntmSave;
	private JMenuItem	mntmSaveAs;
	private JList		liTiles;
	ArrayList<Tile>		tiles	= new ArrayList<Tile>();
	private JTextField	tfX;
	private JTextField	tfY;
	private JTextField	tfWidth;
	private JTextField	tfHeight;
	private JTextField	tfType;
	private JCheckBox	chbxColl;
	private JButton		btnAdd;
	private JButton		btnRemove;
	private JButton		btnSave;

	// ArrayList<String> tiles = new ArrayList<String>();

	public void initGui()
	{
		final JFrame frame = new JFrame("TileSheetEditor");
		frame.setPreferredSize(new Dimension(700, 600));
		frame.pack();

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewTileSheet = new JMenuItem("New TileSheet");
		mntmNewTileSheet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.META_MASK));
		mnFile.add(mntmNewTileSheet);
		mntmNewTileSheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newTileSheet();
			}
		});

		JMenuItem mntmOpenTileSheet = new JMenuItem("Open TileSheet...");
		mntmOpenTileSheet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.META_MASK));
		mnFile.add(mntmOpenTileSheet);
		mntmOpenTileSheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				int input = chooser.showOpenDialog(frame.getParent());
				if (input == JFileChooser.APPROVE_OPTION) {
					loadTileSheet(chooser.getSelectedFile());
				}
			}
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.META_MASK));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentFile == null) {
					JFileChooser chooser = new JFileChooser();
					int input = chooser.showSaveDialog(frame.getParent());
					if (input == JFileChooser.APPROVE_OPTION) {
						saveTileSheet(chooser.getSelectedFile());
					}
				} else {
					saveTileSheet(currentFile);
				}
			}
		});

		mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.SHIFT_MASK | InputEvent.META_MASK));
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				int input = chooser.showSaveDialog(frame.getParent());
				if (input == JFileChooser.APPROVE_OPTION) {
					saveTileSheet(chooser.getSelectedFile());
				}
			}
		});

		JMenu mnTexture = new JMenu("Texture");
		menuBar.add(mnTexture);

		JMenuItem mntmLoad = new JMenuItem("Load...");
		mnTexture.add(mntmLoad);
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				int input = chooser.showOpenDialog(frame.getParent());
				if (input == JFileChooser.APPROVE_OPTION) {
					loadTexture(chooser.getSelectedFile());
				}
			}
		});

		// GL
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(5);
		frame.getContentPane().add(splitPane);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(5);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(128, 128, 128)));
		splitPane_1.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel_1.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JLabel lblTiles = new JLabel("Tiles");
		panel_3.add(lblTiles, BorderLayout.WEST);

		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.EAST);

		btnAdd = new JButton("+");
		btnAdd.setPreferredSize(new Dimension(19, 19));
		panel_4.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				addTile();
			}
		});

		btnRemove = new JButton("-");
		btnRemove.setPreferredSize(new Dimension(19, 19));
		panel_4.add(btnRemove);

		liTiles = new JList();
		liTiles.setModel(new AbstractListModel() {
			public int getSize()
			{
				return tiles.size();
			}

			public Object getElementAt(int index)
			{
				return tiles.get(index);
			}
		});
		panel_1.add(liTiles, BorderLayout.CENTER);
		liTiles.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				selectTile(arg0.getLastIndex());
				updateGui();
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				removeTile();
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(128, 128, 128)));
		splitPane_1.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(128, 128, 128), 1, true));
		panel_6.setBackground(new Color(192, 192, 192));
		panel_2.add(panel_6);
		panel_6.setLayout(null);

		JLabel lblX = new JLabel("X:");
		lblX.setHorizontalAlignment(SwingConstants.RIGHT);
		lblX.setBounds(10, 12, 76, 16);
		panel_6.add(lblX);

		JLabel lblY = new JLabel("Y:");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY.setBounds(10, 40, 76, 16);
		panel_6.add(lblY);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHeight.setBounds(10, 96, 76, 16);
		panel_6.add(lblHeight);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWidth.setBounds(10, 68, 76, 16);
		panel_6.add(lblWidth);

		JLabel lblType = new JLabel("Type:");
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setBounds(10, 147, 76, 16);
		panel_6.add(lblType);

		tfX = new JTextField();
		tfX.setBounds(98, 6, 83, 28);
		tfX.setText("1");
		panel_6.add(tfX);
		tfX.setColumns(10);

		tfY = new JTextField();
		tfY.setColumns(10);
		tfY.setBounds(98, 34, 83, 28);
		tfY.setText("2");
		panel_6.add(tfY);

		tfWidth = new JTextField();
		tfWidth.setColumns(10);
		tfWidth.setBounds(98, 62, 83, 28);
		tfWidth.setText("3");
		panel_6.add(tfWidth);

		tfHeight = new JTextField();
		tfHeight.setColumns(10);
		tfHeight.setText("4");
		tfHeight.setBounds(98, 90, 83, 28);
		panel_6.add(tfHeight);

		tfType = new JTextField();
		tfType.setColumns(10);
		tfType.setBounds(98, 141, 83, 28);
		tfType.setText("5");
		panel_6.add(tfType);

		chbxColl = new JCheckBox("");
		chbxColl.setBounds(98, 168, 83, 23);
		panel_6.add(chbxColl);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(128, 128, 128));
		separator_1.setBounds(0, 124, 100000, 12);
		panel_6.add(separator_1);

		JLabel lblCollidable = new JLabel("Collidable:");
		lblCollidable.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCollidable.setBounds(10, 175, 76, 16);
		panel_6.add(lblCollidable);

		btnSave = new JButton("Save");
		btnSave.setBounds(98, 203, 83, 29);
		panel_6.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveTile();
			}
		});

		JPanel panel_7 = new JPanel();
		panel_7.setPreferredSize(new Dimension(10, 30));
		panel_2.add(panel_7, BorderLayout.NORTH);
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel lblTile = new JLabel("Tile");
		panel_7.add(lblTile, BorderLayout.WEST);
		splitPane_1.setDividerLocation(0.5);
		splitPane_1.setDividerLocation(200);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		GLCanvas canvas = new GLCanvas(glC);

		panel.add(canvas);

		canvas.addGLEventListener(this);

		FPSAnimator animator = new FPSAnimator(canvas, 10);
		splitPane.setDividerLocation(500);
		animator.start();
		frame.setVisible(true);
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				GL.GL_NEAREST);

		if (this.currentTextureFile != null && this.currentTexture == null) {
			try {
				this.currentTexture = TextureIO.newTexture(
						this.currentTextureFile, false);
				this.currentTexture.bind(gl);			
			} catch (GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		gl.glBegin(GL2.GL_QUADS);
//		gl.glColor3f(1.0f, 1.0f, 1.0f);
//
//		gl.glTexCoord2f(0.0f, 0.0f);
//		gl.glVertex2f(-1.0f, 1.0f);
//		gl.glTexCoord2f(0.0f, 1.0f);
//		gl.glVertex2f(-1.0f, -1.0f);
//		gl.glTexCoord2f(1.0f, 1.0f);
//		gl.glVertex2f(1.0f, -1.0f);
//		gl.glTexCoord2f(1.0f, 0.0f);
//		gl.glVertex2f(1.0f, 1.0f);
//
//		gl.glEnd();
		
		for (Tile t : this.tiles) {
			t.render(t.getBounds(),	new Rectangle(0, 0, 64, 64), arg0);
		}

	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL2.GL_TEXTURE_2D);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
		// TODO Auto-generated method stub

	}
}
