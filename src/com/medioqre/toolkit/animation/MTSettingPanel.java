package com.medioqre.toolkit.animation;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class MTSettingPanel extends JFrame{
	
	private JTextField textField;
	private JSpinner spinner;
	private JComboBox comboBox, comboBox_1;
	private JCheckBox checkBox;
	
	public MTSettingPanel(final MTSettings settings, final MTAnimationPreview delegate) {
		getContentPane().setBackground(UIManager.getColor("Panel.background"));
		getContentPane().setLayout(null);
		
		JLabel lblImage = new JLabel("Image");
		lblImage.setBounds(21, 13, 61, 16);
		getContentPane().add(lblImage);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(220,220,220));
		panel.setBorder(new LineBorder(UIManager.getColor("ScrollBar.thumbShadow"), 1, true));
		panel.setBounds(30, 41, 390, 68);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Image URL");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(17, 25, 83, 16);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setToolTipText("Absolute path to image");
		textField.setBounds(112, 19, 272, 28);
		textField.setText(settings.imageURL);
		panel.add(textField);
		textField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(220,220,220));
		panel_1.setBorder(new LineBorder(UIManager.getColor("ScrollBar.thumbShadow"), 1, true));
		panel_1.setBounds(30, 149, 390, 143);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		spinner = new JSpinner();
		spinner.setToolTipText("Time in milliseconds it takes to complete animation");
		spinner.setBounds(112, 19, 75, 28);
		spinner.setModel(new SpinnerNumberModel((int) settings.animationDuration, 0, 10000, 10));
		panel_1.add(spinner);
		
		JLabel lblNewLabel_2 = new JLabel("Time");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(0, 25, 100, 16);
		panel_1.add(lblNewLabel_2);
		
		JLabel lblSpriteSize = new JLabel("Sprite size");
		lblSpriteSize.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSpriteSize.setBounds(17, 63, 83, 16);
		panel_1.add(lblSpriteSize);
		
		comboBox = new JComboBox();
		comboBox.setToolTipText("Width in pixels");
		comboBox.setModel(new DefaultComboBoxModel(new Number[] {8, 16, 32, 64, 128, 256, 512}));
		comboBox.setSelectedItem(settings.spriteSize.width);
		comboBox.setBounds(112, 59, 75, 27);
		panel_1.add(comboBox);
		
		JLabel lblX = new JLabel("x");
		lblX.setBounds(187, 63, 16, 16);
		panel_1.add(lblX);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setToolTipText("Height in pixels");
		comboBox_1.setModel(new DefaultComboBoxModel(new Number[] {8, 16, 32, 64, 128, 256, 512}));
		comboBox_1.setSelectedItem(settings.spriteSize.height);
		comboBox_1.setBounds(197, 59, 75, 27);
		panel_1.add(comboBox_1);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(126, 85, 61, 16);
		panel_1.add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height");
		lblHeight.setBounds(207, 85, 61, 16);
		panel_1.add(lblHeight);
		
		JLabel lblLoop = new JLabel("Loop");
		lblLoop.setToolTipText("Loop the animation");
		lblLoop.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLoop.setBounds(17, 107, 83, 16);
		panel_1.add(lblLoop);
		
		checkBox = new JCheckBox();
		checkBox.setBounds(112, 103, 128, 23);
		checkBox.setSelected(settings.shouldLoop);
		panel_1.add(checkBox);
		
		JLabel lblNewLabel_1 = new JLabel("Animation");
		lblNewLabel_1.setBounds(21, 121, 98, 16);
		getContentPane().add(lblNewLabel_1);
		
		JButton btnApplyChanges = new JButton("Apply");
		btnApplyChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.imageURL = textField.getText();
				settings.animationDuration = ((Number)spinner.getValue()).doubleValue();
				settings.spriteSize = new Dimension(((Number) comboBox.getSelectedItem()).intValue(),((Number) comboBox_1.getSelectedItem()).intValue());
				settings.shouldLoop = checkBox.isSelected();
			
				dispose();
				delegate.updateSettings();
			}
		});
		btnApplyChanges.setBounds(340, 304, 80, 29);
		getContentPane().add(btnApplyChanges);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(30, 304, 80, 29);
		getContentPane().add(btnCancel);
		this.setBounds(250,0,441,361);
		this.setVisible(true);
	}
}
