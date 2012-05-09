package launcher;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;


import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import audio.AudioConstants;

public class OptionsPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3031517145290968619L;
	private Launcher launcher;
	
	
	public OptionsPanel(Launcher frame) {
		launcher = frame;
		
		setSize(new Dimension(640,400));
		
		setBackground(new Color(50,50,50));
		setLayout(null);
		
		JCheckBox chckbxHipsterMode = new JCheckBox("Hipster mode");
		chckbxHipsterMode.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		chckbxHipsterMode.setForeground(Color.WHITE);
		chckbxHipsterMode.setBounds(30, 350, 170, 30);
		add(chckbxHipsterMode);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.drawMainPanel();
			}
		});
		btnDone.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnDone.setBounds(510, 340, 100, 40);
		add(btnDone);
		
		JPanel soundPanel = new JPanel();
		soundPanel.setBackground(Color.DARK_GRAY);
		soundPanel.setBounds(230, 30, 380, 220);
		add(soundPanel);
		soundPanel.setLayout(null);
		
		JSlider musicSlider = new JSlider();
		musicSlider.setValue((int)(AudioConstants.getBGMVolume()*10));
		musicSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider temp = (JSlider)e.getSource();
				AudioConstants.setBGMVolume((float)(temp.getValue()/10.0));
			}
		});
		musicSlider.setBounds(20, 93, 340, 38);
		soundPanel.add(musicSlider);
		musicSlider.setPaintLabels(true);
		musicSlider.setMinorTickSpacing(1);
		musicSlider.setMaximum(10);
		musicSlider.setPaintTicks(true);
		
		JSlider fxSlider = new JSlider();
		fxSlider.setValue((int)(AudioConstants.getFXVolume()*10));
		fxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider fxTemp = (JSlider)e.getSource();
				AudioConstants.setFXVolume((float)(fxTemp.getValue()/10.0));

			}
		});
		fxSlider.setBounds(20, 156, 340, 38);
		soundPanel.add(fxSlider);
		fxSlider.setPaintTicks(true);
		fxSlider.setPaintLabels(true);
		fxSlider.setMinorTickSpacing(1);
		fxSlider.setMaximum(10);
		
		JLabel musicVolumeLabel = new JLabel("Music Volume");
		musicVolumeLabel.setBounds(20, 80, 340, 16);
		soundPanel.add(musicVolumeLabel);
		musicVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		musicVolumeLabel.setForeground(Color.WHITE);
		
		JLabel lblFxVolume = new JLabel("FX Volume");
		lblFxVolume.setBounds(20, 143, 340, 16);
		soundPanel.add(lblFxVolume);
		lblFxVolume.setHorizontalAlignment(SwingConstants.CENTER);
		lblFxVolume.setForeground(Color.WHITE);
		
		JLabel lblSound = new JLabel("Sound");
		lblSound.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblSound.setForeground(Color.WHITE);
		lblSound.setBounds(30, 20, 200, 24);
		soundPanel.add(lblSound);
		
		JComboBox difficultyBox = new JComboBox();
		difficultyBox.setModel(new DefaultComboBoxModel(new String[] {"Easy", "Medium", "Hard"}));
		difficultyBox.setBounds(30, 100, 170, 27);
		add(difficultyBox);
		
		JLabel lblDifficulty = new JLabel("Difficulty");
		lblDifficulty.setHorizontalAlignment(SwingConstants.CENTER);
		lblDifficulty.setForeground(Color.WHITE);
		lblDifficulty.setBounds(30, 80, 170, 16);
		add(lblDifficulty);

		
		
		
	}
}
