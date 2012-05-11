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

import datamanagement.PreferenceLoader;

import audio.AudioConstants;

public class OptionsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3031517145290968619L;
	private Launcher launcher;

	public OptionsPanel(Launcher frame) {
		launcher = frame;

		setSize(new Dimension(640, 400));

		setBackground(new Color(50, 50, 50));
		setLayout(null);

		JCheckBox chckbxHipsterMode = new JCheckBox("Hipster mode");
		chckbxHipsterMode.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		chckbxHipsterMode.setForeground(Color.WHITE);
		chckbxHipsterMode.setBounds(30, 350, 170, 30);
		chckbxHipsterMode.setOpaque(false);
		add(chckbxHipsterMode);
		chckbxHipsterMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreferenceLoader.putBoolean("HIPSTER_MODE", ((JCheckBox) arg0.getSource()).isSelected());
			}
		});

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
		soundPanel.setBounds(230, 30, 380, 280);
		add(soundPanel);
		soundPanel.setLayout(null);

		JSlider musicSlider = new JSlider();
		musicSlider.setValue((int) (PreferenceLoader.getFloat("BGM_VOLUME",AudioConstants.standardBGMVolume) * 10));
		musicSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider temp = (JSlider) e.getSource();
				PreferenceLoader.putFloat("BGM_VOLUME",((float) (temp.getValue() / 10.0)));
			}
		});
		musicSlider.setBounds(30, 153, 340, 38);
		musicSlider.setPaintLabels(true);
		musicSlider.setMinorTickSpacing(1);
		musicSlider.setMaximum(10);
		musicSlider.setPaintTicks(true);
		musicSlider.setOpaque(false);
		soundPanel.add(musicSlider);

		JSlider fxSlider = new JSlider();
		fxSlider.setValue((int) (PreferenceLoader.getFloat("FX_VOLUME",AudioConstants.standardFXVolume) * 10));
		fxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider fxTemp = (JSlider) e.getSource();
				PreferenceLoader.putFloat("FX_VOLUME",((float) (fxTemp.getValue() / 10.0)));

			}
		});
		fxSlider.setBounds(30, 216, 340, 38);
		fxSlider.setPaintTicks(true);
		fxSlider.setPaintLabels(true);
		fxSlider.setMinorTickSpacing(1);
		fxSlider.setMaximum(10);
		fxSlider.setOpaque(false);
		soundPanel.add(fxSlider);

		JLabel musicVolumeLabel = new JLabel("Music Volume");
		musicVolumeLabel.setBounds(30, 140, 340, 16);
		musicVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		musicVolumeLabel.setForeground(Color.WHITE);
		musicVolumeLabel.setOpaque(false);
		soundPanel.add(musicVolumeLabel);
		
		JLabel lblFxVolume = new JLabel("FX Volume");
		lblFxVolume.setBounds(30, 203, 340, 16);
		lblFxVolume.setHorizontalAlignment(SwingConstants.CENTER);
		lblFxVolume.setForeground(Color.WHITE);
		lblFxVolume.setOpaque(false);
		soundPanel.add(lblFxVolume);

		JLabel lblSound = new JLabel("Sound");
		lblSound.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
		lblSound.setForeground(Color.WHITE);
		lblSound.setBounds(30, 20, 200, 24);
		lblSound.setOpaque(false);
		soundPanel.add(lblSound);
		
		JCheckBox pitchDownOnHarm = new JCheckBox("Pitch music when hurt");
		pitchDownOnHarm.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent i) {
				JCheckBox e = (JCheckBox)i.getSource();
				PreferenceLoader.putBoolean("PITCH_D_BGM_WHEN_HURT",e.isSelected());
			}
		});
		pitchDownOnHarm.setSelected(PreferenceLoader.getBoolean("PITCH_D_BGM_WHEN_HURT", false));
		pitchDownOnHarm.setBounds(30, 80, 325, 30);
		soundPanel.add(pitchDownOnHarm);
		pitchDownOnHarm.setOpaque(false);
		pitchDownOnHarm.setForeground(Color.WHITE);
		pitchDownOnHarm.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JComboBox difficultyBox = new JComboBox();
		difficultyBox.setModel(new DefaultComboBoxModel(new String[]{"Easy",
				"Medium", "Hard"}));
		difficultyBox.setBounds(30, 100, 170, 27);
		difficultyBox.setOpaque(false);
		add(difficultyBox);

		JLabel lblDifficulty = new JLabel("Difficulty");
		lblDifficulty.setHorizontalAlignment(SwingConstants.CENTER);
		lblDifficulty.setForeground(Color.WHITE);
		lblDifficulty.setBounds(30, 80, 170, 16);
		lblDifficulty.setOpaque(false);
		add(lblDifficulty);
		
		JCheckBox chckbxLsdMode = new JCheckBox("LSD mode");
		chckbxLsdMode.setOpaque(false);
		chckbxLsdMode.setForeground(Color.WHITE);
		chckbxLsdMode.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		chckbxLsdMode.setBounds(30, 308, 170, 30);
		add(chckbxLsdMode);
		chckbxLsdMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PreferenceLoader.putBoolean("LSD_MODE", ((JCheckBox) e.getSource()).isSelected());
			}
		});

	}
}
