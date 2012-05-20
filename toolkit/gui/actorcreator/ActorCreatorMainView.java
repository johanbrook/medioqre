package gui.actorcreator;

import graphics.opengl.animation.Actor;
import graphics.opengl.animation.Animation;
import graphics.opengl.animation.Sprite;

import java.awt.Component;
import java.awt.Dimension;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

import javax.swing.CellRendererPane;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SpinnerDateModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JSeparator;

import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import tools.datamanagement.ResourceLoader;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActorCreatorMainView implements GLEventListener {

	public static void main(String[] args) {
		new ActorCreatorMainView();
	}

	private File currentFilePath;
	private File currentFile;
	private Actor currentActor;

	private Texture spriteSheet;
	private File spriteSheetFile;
	private boolean changedSpriteFile = false;

	private Sprite currentSprite;
	
	private JFrame frame;

	// Other functionality
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	public void addAnimation() {
		if (this.currentActor == null) {
			this.showMessage("No actor has been set!");
			return;
		}
		this.currentActor.addAnimation(new Animation());
		this.currentActor.setAnimation(0);
		this.updateGui();
		System.out.println("Adding animation!");
	}
	public void addSprite() {
		if (this.currentActor != null
				&& this.currentActor.getCurrentAnimation() != null) {
			this.currentActor.getCurrentAnimation().addSprite(new Sprite());
			this.updateGui();
			System.out.println("Adding sprite!");
		}
	}

	public void removeAnimation() {
		if (this.currentActor != null && this.currentActor.getCurrentAnimation() != null) {
			this.currentActor.removeAnimation(this.currentActor.getCurrentAnimation());
			this.updateGui();
			System.out.println("Remove animation!");
		}
	}
	public void removeSprite() {
		System.out.println("Remove sprite!");
	}

	public void selectAnimation(Animation a) {
		if (this.currentActor != null && a != null) {
			this.currentActor.setAnimation(a.getAnimationTag());
			this.updateGui();
		}
	}
	public void selectSprite(Sprite s) {
		if (this.currentActor != null && this.currentActor.getCurrentAnimation() != null) {
			for (int i = 0; i < this.currentActor.getCurrentAnimation().getSprites().length; i++) {
				if (this.currentActor.getCurrentAnimation().getSprites()[i] == s) {
					this.currentSprite = s;
					this.updateGui();
				}
			}
		}
	}

	public void editAnimationTag() {
		if (this.currentActor == null
				|| this.currentActor.getCurrentAnimation() == null)
			return;

		this.currentActor.getCurrentAnimation().setAnimationTag(
				tools.Math.intFromHexString(JOptionPane.showInputDialog(
						this.frame.getParent(),
						"Enter the hexadecimal value for the animation tag")
						.toLowerCase()));
		this.updateGui();
	}
	public void editDuration() {
		if (this.currentActor != null
				&& this.currentActor.getCurrentAnimation() != null) {
			this.currentActor.getCurrentAnimation().setAnimationDuration(
					Integer.valueOf(JOptionPane.showInputDialog(this.frame,
							"Enter new animation duration")));
			this.updateGui();
		}
	}

	// GUI functionality
	public void updateGui() {
		this.liAnimations.setModel(new ListModel() {

			@Override
			public void removeListDataListener(ListDataListener arg0) {
			}

			@Override
			public int getSize() {
				if (currentActor == null
						|| currentActor.getAnimations() == null)
					return 0;
				return currentActor.getAnimations().length;
			}

			@Override
			public Object getElementAt(int arg0) {
				if (currentActor == null
						|| currentActor.getAnimations() == null)
					return null;

				return currentActor.getAnimations()[arg0];
			}

			@Override
			public void addListDataListener(ListDataListener arg0) {
			}
		});
		this.liAnimations.validate();
		this.liAnimations.setCellRenderer(new ListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList arg0, Object arg1,
					int arg2, boolean arg3, boolean arg4) {
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				JLabel title = new JLabel("Tag: "+((Animation) arg1).getAnimationTag());
				p.add(title, BorderLayout.WEST);
					
				p.setBackground(arg4 ? Color.BLUE : Color.WHITE);
				return p;
			}
		});

		this.liSprites.setModel(new ListModel() {
			
			@Override
			public void removeListDataListener(ListDataListener arg0) {}
			
			@Override
			public int getSize() {
				if (currentActor == null || currentActor.getCurrentAnimation() == null || currentActor.getCurrentAnimation().getSprites() == null) {
					return 0;
				}
				return currentActor.getCurrentAnimation().getSprites().length;
			}
			
			@Override
			public Object getElementAt(int arg0) {
				if (currentActor == null || currentActor.getCurrentAnimation() == null || currentActor.getCurrentAnimation().getSprites() == null) {
					return null;
				}
				return currentActor.getCurrentAnimation().getSprites()[arg0];
			}
			
			@Override
			public void addListDataListener(ListDataListener arg0) {}
		});
		this.liSprites.setCellRenderer(new ListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(JList arg0, Object arg1,
					int arg2, boolean arg3, boolean arg4) {
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				JLabel title = new JLabel(""+arg2);
				p.add(title, BorderLayout.WEST);
					
				p.setBackground(arg4 ? Color.BLUE : Color.WHITE);
				return p;
			}
		});
		this.liSprites.validate();
		
		if (this.currentActor != null
				&& this.currentActor.getCurrentAnimation() != null) {
			this.lblAnimationtag.setText("AnimationTag: "
					+ Integer.toHexString(this.currentActor
							.getCurrentAnimation().getAnimationTag()));
			this.lblAnimationDuration.setText("Duration: "
					+ this.currentActor.getCurrentAnimation()
							.getAnimationDuration());
		}
		System.out.println("Updating GUI");
	}

	// Menu actions
	public void newActor() {
		this.currentFile = null;
		this.currentActor = new Actor();
		if (this.spriteSheetFile != null)
			this.currentActor.setTextureName(this.spriteSheetFile.getName()
					.substring(0, this.spriteSheetFile.getName().indexOf('.')));
		System.out.println("Creating new actor!");
		this.updateGui();
	}
	public void openActor() {
		JFileChooser chooser = new JFileChooser(this.currentFilePath);
		int input = chooser.showOpenDialog(this.frame.getParent());
		if (input == JFileChooser.APPROVE_OPTION) {
			this.currentFile = chooser.getSelectedFile();
			this.currentFilePath = this.currentFile;
			try {
				this.currentActor = new Actor(new JSONObject(
						ResourceLoader
								.loadJSONStringFromStream(new FileInputStream(
										this.currentFile))));
				System.out.println("Actor: " + this.currentActor);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.updateGui();
	}
	public void save(boolean forceChooserToShow) {
		if (forceChooserToShow || this.currentFile == null) {
			JFileChooser chooser = new JFileChooser(this.currentFile);
			int input = chooser.showSaveDialog(this.frame.getParent());

			if (input != JFileChooser.APPROVE_OPTION)
				return;

			this.currentFile = chooser.getSelectedFile();
			this.currentFilePath = this.currentFile;
		}

		this.writeToFile(this.currentFile);
		System.out.println("Saving!");
		this.updateGui();
	}
	public void loadSpriteSheet() {

		JFileChooser chooser = new JFileChooser(this.spriteSheetFile);
		int input = chooser.showOpenDialog(this.frame.getParent());

		if (input != JFileChooser.APPROVE_OPTION)
			return;

		this.spriteSheetFile = chooser.getSelectedFile();
		this.changedSpriteFile = true;
		if (this.currentActor != null)
			this.currentActor.setTextureName(this.spriteSheetFile.getName()
					.substring(0, this.spriteSheetFile.getName().indexOf('.')));
		System.out.println("Load spritesheet! "
				+ this.spriteSheetFile.getName().substring(0,
						this.spriteSheetFile.getName().indexOf('.')));

		this.lblTexture.setText("Texture: " + this.spriteSheetFile.getName());
	}

	// Core functionality
	private void writeToFile(File f) {
		if (this.currentActor == null) {
			return;
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(f);
			writer.write(this.currentActor.serialize().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					System.out.println("Wrote to disk!");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private JList liAnimations;
	private JList liSprites;
	private JLabel lblTexture;
	private JLabel lblAnimationtag;
	private JLabel lblAnimationDuration;

	public ActorCreatorMainView() {
		this.frame = new JFrame("ActorCreator 0.1 - Made by JBarberU");
		this.frame.setPreferredSize(new Dimension(700, 500));
		this.frame.pack();

		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		GLCanvas canvas = new GLCanvas(glC);

		canvas.addGLEventListener(this);
		new FPSAnimator(canvas, 30).start();

		JSplitPane splitPane = new JSplitPane();
		this.frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);

		splitPane.setRightComponent(splitPane_1);

		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		splitPane_1.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_6.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel lblAnimations = new JLabel("Animations");
		panel_1.add(lblAnimations, BorderLayout.WEST);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.EAST);

		JButton btnAddAnimation = new JButton("+");
		btnAddAnimation.setPreferredSize(new Dimension(20, 20));
		btnAddAnimation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAnimation();
			}
		});
		panel_2.add(btnAddAnimation);

		JButton btnRemoveAnimation = new JButton("-");
		btnRemoveAnimation.setPreferredSize(new Dimension(20, 20));
		btnRemoveAnimation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeAnimation();
			}
		});
		panel_2.add(btnRemoveAnimation);

		this.liAnimations = new JList();
		this.liAnimations.setBackground(Color.MAGENTA);
		this.liAnimations.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				JList l = (JList) arg0.getSource();
				selectAnimation((Animation) l.getSelectedValue());
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		panel_6.add(scrollPane);
		scrollPane.setViewportView(this.liAnimations);

		JPanel panel_7 = new JPanel();
		panel.add(panel_7, BorderLayout.NORTH);
		panel_7.setLayout(new BorderLayout(0, 0));

		this.lblTexture = new JLabel("Texture:");
		panel_7.add(lblTexture, BorderLayout.WEST);

		JButton btnChangeTexture = new JButton("...");
		btnChangeTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadSpriteSheet();
			}
		});
		btnChangeTexture.setPreferredSize(new Dimension(20, 20));
		panel_7.add(btnChangeTexture, BorderLayout.EAST);

		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);

		splitPane_1.setRightComponent(splitPane_2);

		JPanel panel_3 = new JPanel();
		splitPane_2.setLeftComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panel_8 = new JPanel();
		panel_3.add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_8.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblSprites = new JLabel("Sprites");
		panel_4.add(lblSprites, BorderLayout.WEST);

		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.EAST);

		JButton btnAddSprite = new JButton("+");
		btnAddSprite.setPreferredSize(new Dimension(20, 20));
		btnAddSprite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addSprite();
			}
		});
		panel_5.add(btnAddSprite);

		JButton btnRemoveSprite = new JButton("-");
		btnRemoveSprite.setPreferredSize(new Dimension(20, 20));
		btnRemoveSprite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeSprite();
			}
		});
		panel_5.add(btnRemoveSprite);

		this.liSprites = new JList();
		this.liSprites.setBackground(Color.CYAN);
		this.liSprites.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				JList l = (JList) arg0.getSource();
				selectSprite((Sprite) l.getSelectedValue());
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_8.add(scrollPane_1, BorderLayout.CENTER);
		scrollPane_1.setViewportView(this.liSprites);

		JPanel panel_10 = new JPanel();
		panel_10.setLayout(new BorderLayout());
		panel_3.add(panel_10, BorderLayout.NORTH);

		JPanel panel_9 = new JPanel();
		panel_10.add(panel_9, BorderLayout.NORTH);
		panel_9.setLayout(new BorderLayout(0, 0));

		JPanel panel_11 = new JPanel();
		panel_10.add(panel_11, BorderLayout.SOUTH);
		panel_11.setLayout(new BorderLayout(0, 0));

		this.lblAnimationDuration = new JLabel("Duration: ");
		panel_11.add(this.lblAnimationDuration, BorderLayout.WEST);

		JButton btnEditDuration = new JButton("...");
		btnEditDuration.setPreferredSize(new Dimension(20, 20));
		btnEditDuration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editDuration();
			}
		});
		panel_11.add(btnEditDuration, BorderLayout.EAST);

		lblAnimationtag = new JLabel("AnimationTag: ");
		panel_9.add(lblAnimationtag, BorderLayout.WEST);

		JButton btnAnimationTag = new JButton("...");
		btnAnimationTag.setPreferredSize(new Dimension(20, 20));
		btnAnimationTag.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editAnimationTag();
			}
		});
		panel_9.add(btnAnimationTag, BorderLayout.EAST);

		splitPane.setLeftComponent(canvas);

		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewActor = new JMenuItem("New Actor");
		mntmNewActor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.META_MASK));
		mnFile.add(mntmNewActor);
		mntmNewActor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newActor();
			}
		});

		JMenuItem mntmOpenActor = new JMenuItem("Open...");
		mntmOpenActor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.META_MASK));
		mnFile.add(mntmOpenActor);
		mntmOpenActor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openActor();
			}
		});

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmSaveActor = new JMenuItem("Save");
		mntmSaveActor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.META_MASK));
		mnFile.add(mntmSaveActor);
		mntmSaveActor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save(false);
			}
		});

		JMenuItem mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.SHIFT_MASK | InputEvent.META_MASK));
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save(true);
			}
		});

		JMenu mnTexture = new JMenu("SpriteSheet");
		menuBar.add(mnTexture);

		JMenuItem mntmLoadSpritesheet = new JMenuItem("Load SpriteSheet...");
		mntmLoadSpritesheet.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, InputEvent.META_MASK));
		mnTexture.add(mntmLoadSpritesheet);
		mntmLoadSpritesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadSpriteSheet();
			}
		});

		this.frame.validate();
		this.frame.setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1f, 1f, 1f, 1f);
		gl.glEnable(GL.GL_TEXTURE_2D);
	}
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		if (this.changedSpriteFile && this.spriteSheetFile != null) {
			try {
				this.spriteSheet = TextureIO.newTexture(this.spriteSheetFile,
						false);
				this.changedSpriteFile = false;
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.spriteSheet != null) {
			this.spriteSheet.bind(gl);
		}
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0f, 0f);
		gl.glVertex2f(-1f, -0.5f);

		gl.glTexCoord2f(0f, 1f);
		gl.glVertex2f(-1f, 1f);

		gl.glTexCoord2f(1f, 1f);
		gl.glVertex2f(1f, 1f);

		gl.glTexCoord2f(1f, 0f);
		gl.glVertex2f(1f, -0.5f);

		gl.glEnd();

	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}

}
