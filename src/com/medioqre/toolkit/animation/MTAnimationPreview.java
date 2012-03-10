package com.medioqre.toolkit.animation;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MTAnimationPreview extends JFrame implements Runnable {

	// Frame rate
	private final int FPS = 60;
	
	// Timing
	private double timeLast;
	private double timeThisFrame;
	
	// FPS-meter
	private int framesThisSecond;
	private double timeThisSecond;
	private int currentFPS;
	
	private MTSettings settings;
	
	private MTAnimationPanel animationPanel;
	
	private final MTAnimationPreview hackAround = this;
	
	public MTAnimationPreview()
	{
		// Logic 
		
		this.settings = new MTSettings();
		this.settings.imageURL = "";
		this.settings.spriteSize = new Dimension(16,16);
		this.settings.animationDuration = 500;
		this.settings.shouldLoop = true;
		
		
		// GUI
		this.animationPanel = new MTAnimationPanel();
		
		getContentPane().add(this.animationPanel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmRefreshImage = new JMenuItem("Refresh image");
		mntmRefreshImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.META_DOWN_MASK));
		mnTools.add(mntmRefreshImage);
		mntmRefreshImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateSettings();
			}
		});
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences...");
		mnTools.add(mntmPreferences);
		mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,KeyEvent.META_DOWN_MASK));
		mntmPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MTSettingPanel(settings, hackAround);
			}
		});
		
		JMenu mnSpeed = new JMenu("Animation Duration");
		menuBar.add(mnSpeed);
		
		JMenuItem mntmLower = new JMenuItem("Lower");
		mnSpeed.add(mntmLower);
		mntmLower.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,KeyEvent.META_DOWN_MASK));
		mntmLower.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.animationDuration = settings.animationDuration > 50 ? settings.animationDuration - 50 : 0;
				animationPanel.setAnimation(settings.imageURL, settings.spriteSize, settings.animationDuration, settings.shouldLoop);
			}
		});
		
		JMenuItem mntmRaise = new JMenuItem("Raise");
		mnSpeed.add(mntmRaise);
		mntmRaise.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,KeyEvent.META_DOWN_MASK));
		mntmRaise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.animationDuration += 50;
				animationPanel.setAnimation(settings.imageURL, settings.spriteSize, settings.animationDuration, settings.shouldLoop);
			}
		});
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Thread t = new Thread(this);
		t.start();		
	}
	public void updateSettings()
	{
		this.animationPanel.setAnimation(this.settings.imageURL, this.settings.spriteSize, settings.animationDuration, this.settings.shouldLoop);
	}
	
	private void runLoop(double dt)
	{
		this.animationPanel.render(dt);
	}

	@Override
	public void run() {
		this.timeLast = System.nanoTime();
		while(!Thread.interrupted()) {
			try {
				Thread.sleep(1000/FPS);
				this.timeThisFrame = System.nanoTime();
				double dt = (this.timeThisFrame - this.timeLast)/1000000.0;
				this.timeLast = this.timeThisFrame;
				
				this.runLoop(dt);
			
				if (this.timeThisSecond < 1000.0) {
					this.timeThisSecond += dt;
					this.framesThisSecond ++;
				} else {
					this.currentFPS = this.framesThisSecond;
					this.timeThisSecond = 0;
					this.framesThisSecond = 0;
					System.out.println("FPS: "+this.currentFPS);
				}
				
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}	
}
