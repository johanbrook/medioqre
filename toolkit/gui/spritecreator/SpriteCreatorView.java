package gui.spritecreator;

import graphics.opengl.Actor;

import java.awt.Dimension;
import java.awt.Frame;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.Animator;

import core.Rectangle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpriteCreatorView extends JFrame implements GLEventListener,
		ActionListener {

	private Actor player;
	private Rectangle rect = new Rectangle(2 * 64, 1 * 64, 64, 64);
	private Rectangle target = new Rectangle(0, 0, 5 * 64, 3 * 64);

	public SpriteCreatorView()
	{
		setPreferredSize(new Dimension(464, 645));
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		GLCanvas canvas = new GLCanvas(glC);
		canvas.setBackground(Color.LIGHT_GRAY);

		canvas.addGLEventListener(this);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser chooser = new JFileChooser();

				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						InputStream inputStream = new FileInputStream(new File(
								chooser.getSelectedFile().getAbsolutePath()));
						String inputString = IOUtils.toString(inputStream);

						JSONObject jSon = new JSONObject(inputString);
						player = new Actor(jSon);
						actorEdit.addActor(player);
					} catch (IOException e) {
						System.out.println("Couldn't load file!");
						e.printStackTrace();
					} catch (JSONException e) {
						System.out.println("Couldn't load file!");
						e.printStackTrace();
					}
				}
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.META_MASK));
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.META_MASK));
		mnFile.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save as\u2026");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.SHIFT_MASK | InputEvent.META_MASK));
		mnFile.add(mntmSaveAs);

		Frame animationView = new Frame("SpriteView");
		animationView.setPreferredSize(new Dimension(400, 400));
		animationView.add(canvas);
		animationView.pack();
		animationView.setVisible(true);

		this.initEditViewGUI();

		Animator anim = new Animator(canvas);
		anim.start();

	}

	private void reloadSprite(Actor newPlayer)
	{
		// synchronized (this.player) {
		// System.out.println("Selected animation: "+this.animationEdit.getSelectedAnimation());
		// newPlayer.setAnimation(this.animationEdit.getSelectedAnimation());
		// this.player = newPlayer;
		// }
	}

	private void saveState()
	{
		System.out.println("Actor: "
				+ this.actorEdit.getSelectedActor().serialize().toString());
	}

	public static void main(String[] args)
	{
		new SpriteCreatorView();
	}

	long lastTime = System.nanoTime();

	@Override
	public void display(GLAutoDrawable arg0)
	{
		long now = System.nanoTime();
		long dt = (now - lastTime) / 1000000;
		this.lastTime = now;

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		if (this.player != null && this.player.getCurrentAnimation() != null) {
			this.player.getCurrentAnimation().update(dt);
			this.player.render(this.rect, this.target, arg0);
		}
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		this.player = new Actor("player", 128, 64, 64, 64);
		this.rect = new Rectangle(this.player.getX(), this.player.getY(),
				this.player.getWidth(), this.player.getHeight());

		String jSon = "{\"animations\":[{\"duration\":500,\"frames\":[{\"height\":16,\"texture\":\"pokemon\",\"width\":13,\"y\":8,\"x\":95},{\"height\":17,\"texture\":\"pokemon\",\"width\":13,\"y\":7,\"x\":81}],\"name\":\"moveLeft\"},{\"duration\":500,\"frames\":[{\"height\":16,\"texture\":\"pokemon\",\"width\":15,\"y\":8,\"x\":142},{\"height\":16,\"texture\":\"pokemon\",\"width\":15,\"y\":8,\"x\":125}],\"name\":\"moveUp\"},{\"duration\":500,\"frames\":[{\"height\":16,\"texture\":\"pokemon\",\"width\":13,\"y\":8,\"x\":53},{\"height\":17,\"texture\":\"pokemon\",\"width\":13,\"y\":7,\"x\":67}],\"name\":\"moveRight\"}],\"height\":128,\"width\":128,\"name\":\"New Actor\",\"y\":0,\"x\":0}";

		// String jSon2 =
		// "{\"animations\":[{\"duration\":500,\"frames\":[{\"height\":64,\"texture\":\"pokemon\",\"width\":64,\"y\":8,\"x\":50}],\"name\":\"moveLeft\"}],\"height\":64,\"width\":64,\"name\":\"player\",\"y\":0,\"x\":0}";

		try {
			synchronized (this.player) {
				this.player = new Actor(new JSONObject(jSon));
				this.player.setCurrentAnimation("moveUp");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("actor")) {
			this.animationEdit.setActor(this.actorEdit.getSelectedActor());
			this.spriteEdit.setAnimation(this.animationEdit
					.getSelectedAnimation());
		} else if (e.getActionCommand().equals("animation")) {
			this.spriteEdit.setAnimation(this.animationEdit
					.getSelectedAnimation());
		} else if (e.getActionCommand().equals("sprite")) {

		} else if (e.getActionCommand().equals("reload")) {
			this.spriteEdit.saveState();
			this.animationEdit.saveState();
			this.actorEdit.saveState();

			this.player = new Actor(this.actorEdit.getSelectedActor()
					.serialize());
			this.player.setCurrentAnimation(this.animationEdit
					.getSelectedAnimation().getName());
			this.rect = new Rectangle(this.player.getX(), this.player.getY(),
					this.player.getWidth(), this.player.getHeight());
		}
	}

	private ActorEdit actorEdit;
	private AnimationEdit animationEdit;
	private SpriteEdit spriteEdit;

	private void initEditViewGUI()
	{
		this.setTitle("EditView");
		this.pack();
		getContentPane().setLayout(null);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		JButton btnReload = new JButton("Reload");
		btnReload.setBounds(324, 564, 134, 29);
		getContentPane().add(btnReload);
		btnReload.setActionCommand("reload");
		btnReload.addActionListener(this);

		JLabel lblSprite = new JLabel("Sprite");
		lblSprite.setBounds(6, 353, 61, 16);
		getContentPane().add(lblSprite);

		JLabel lblActor = new JLabel("Actor");
		lblActor.setBounds(6, 6, 61, 16);
		getContentPane().add(lblActor);

		JLabel lblAnimation = new JLabel("Animation");
		lblAnimation.setBounds(6, 205, 88, 16);
		getContentPane().add(lblAnimation);

		JButton btnSaveState = new JButton("Save state");
		btnSaveState.setBounds(178, 564, 134, 29);
		getContentPane().add(btnSaveState);

		actorEdit = new ActorEdit(this, null);
		actorEdit.setBounds(6, 34, 452, 159);
		getContentPane().add(actorEdit);

		animationEdit = new AnimationEdit(this, null);
		animationEdit.setBounds(6, 233, 452, 108);
		getContentPane().add(animationEdit);

		spriteEdit = new SpriteEdit(this, null);
		spriteEdit.setBounds(6, 381, 452, 171);
		getContentPane().add(spriteEdit);
		btnSaveState.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				saveState();
			}
		});
	}
}
