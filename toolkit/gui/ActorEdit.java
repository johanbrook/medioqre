package gui;

import graphics.Actor;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.border.LineBorder;
import javax.swing.JButton;

public class ActorEdit extends JPanel {

	private Actor[] actors;
	private int selectedActor;
	private ActionListener cmbAListener;
	private ActionListener parentListener;

	// Getters 
	public Actor getSelectedActor()
	{
		if (this.actors != null && this.actors.length > this.selectedActor)
			return this.actors[this.selectedActor];
		
		return null;
	}
	
	public void addActor(Actor a)
	{
		Actor[] newActors;
		if (this.actors != null) {
			newActors = new Actor[this.actors.length + 1];

			for (int i = 0; i < this.actors.length; i++) {
				newActors[i] = this.actors[i];
			}
		} else
			newActors = new Actor[1];

		this.actors = newActors;

		this.selectedActor = this.actors.length - 1;
		this.actors[this.selectedActor] = a;
		this.updateGUI();
	}
	private void addNewActor()
	{
		this.addActor(new Actor("New Actor", 0, 0, 0, 0));
	}

	private void removeActor()
	{
		if (this.actors != null && this.actors.length > 0) {
			int rmIndex = -1;
			for (int i = 0; i < this.actors.length; i++) {
				if (this.actors[i] == this.actors[selectedActor])
					rmIndex = i;
			}
			if (rmIndex == -1)
				return;

			System.out.println("RM: " + rmIndex);
			Actor[] newActors = new Actor[this.actors.length - 1];
			for (int i = 0; i < newActors.length; i++) {
				System.out.println("I: " + i);
				if (i < rmIndex) {
					newActors[i] = this.actors[i];
				} else if (i > rmIndex) {
					newActors[i] = this.actors[i + 1];
				}
			}

			this.actors = newActors;
			if (this.actors.length > 0)
				this.actors[selectedActor] = this.actors[rmIndex - 1];

		}
		this.updateGUI();
	}

	public void updateGUI()
	{
		this.cmbA.removeActionListener(this.cmbAListener);
		this.cmbA.removeActionListener(this.parentListener);
		this.cmbA.removeAllItems();
		if (this.actors != null) {
			for (int i = 0; i < actors.length; i++) {
				this.cmbA.addItem(actors[i]);
			}
			this.cmbA.setSelectedIndex(this.selectedActor);
		}
		this.cmbA.validate();
		this.cmbA.addActionListener(this.cmbAListener);
		this.cmbA.addActionListener(this.parentListener);

		if (this.actors != null && this.actors[this.selectedActor] != null) {
			this.tfName.setText(this.actors[this.selectedActor].getName());
			this.tfX.setText("" + this.actors[this.selectedActor].getX());
			this.tfY.setText("" + this.actors[this.selectedActor].getY());
			this.tfW.setText("" + this.actors[this.selectedActor].getWidth());
			this.tfH.setText("" + this.actors[this.selectedActor].getHeight());

		}
	}

	public void saveState()
	{
		if (this.actors[this.selectedActor] != null) {
			this.actors[this.selectedActor].setName(this.tfName.getText());
			this.actors[this.selectedActor].setX(Integer.valueOf(this.tfX.getText()));
			this.actors[this.selectedActor].setY(Integer.valueOf(this.tfY.getText()));
			this.actors[this.selectedActor].setWidth(Integer.valueOf(this.tfW.getText()));
			this.actors[this.selectedActor].setHeight(Integer.valueOf(this.tfH.getText()));
		}
	}

	// GUI
	private JComboBox cmbA;
	private JTextField tfName;
	private JTextField tfX;
	private JTextField tfY;
	private JTextField tfW;
	private JTextField tfH;

	public ActorEdit(ActionListener l, Actor[] actors)
	{
		this();

		this.actors = actors;
		this.parentListener = l;
		
		this.updateGUI();
	}

	public ActorEdit()
	{
		setBorder(new LineBorder(new Color(128, 128, 128)));
		setBackground(new Color(192, 192, 192));
		setLayout(null);

		cmbA = new JComboBox();
		cmbA.setBounds(129, 8, 271, 27);
		add(cmbA);
		cmbA.setActionCommand("actor");
		cmbAListener = (new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				selectedActor = ((JComboBox) arg0.getSource())
						.getSelectedIndex();
				updateGUI();
			}
		});
		cmbA.addActionListener(cmbAListener);
		cmbA.validate();

		JLabel lblNewLabel = new JLabel("Name:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(6, 46, 111, 16);
		add(lblNewLabel);

		tfName = new JTextField();
		tfName.setBounds(129, 40, 271, 28);
		add(tfName);
		tfName.setColumns(10);
		tfName.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0)
			{
				saveState();
			}
			@Override
			public void focusGained(FocusEvent arg0){}
		});

		JLabel lblActor = new JLabel("Actor:");
		lblActor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActor.setBounds(6, 12, 111, 16);
		add(lblActor);

		tfX = new JTextField();
		tfX.setColumns(10);
		tfX.setBounds(129, 91, 68, 28);
		add(tfX);
		tfX.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0)
			{
				saveState();
			}
			@Override
			public void focusGained(FocusEvent arg0){}
		});

		JLabel lblY = new JLabel("X:");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY.setBounds(6, 97, 111, 16);
		add(lblY);

		tfY = new JTextField();
		tfY.setColumns(10);
		tfY.setBounds(129, 125, 68, 28);
		add(tfY);
		tfY.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0)
			{
				saveState();
			}
			@Override
			public void focusGained(FocusEvent arg0){}
		});

		JLabel lblWidth = new JLabel("Y:");
		lblWidth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWidth.setBounds(6, 131, 111, 16);
		add(lblWidth);

		tfW = new JTextField();
		tfW.setColumns(10);
		tfW.setBounds(332, 91, 68, 28);
		add(tfW);
		tfW.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0)
			{
				saveState();
			}
			@Override
			public void focusGained(FocusEvent arg0){}
		});

		JLabel lblHeight = new JLabel("Width:");
		lblHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHeight.setBounds(209, 97, 111, 16);
		add(lblHeight);

		JLabel label = new JLabel("Height:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(209, 131, 111, 16);
		add(label);

		tfH = new JTextField();
		tfH.setColumns(10);
		tfH.setBounds(332, 125, 68, 28);
		add(tfH);
		tfH.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0)
			{
				saveState();
			}
			@Override
			public void focusGained(FocusEvent arg0){}
		});

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(128, 128, 128));
		separator.setBounds(6, 74, 438, 16);
		add(separator);

		JButton btnAdd = new JButton("+");
		btnAdd.setBounds(399, 11, 20, 20);
		add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addNewActor();
			}
		});

		JButton btnRemove = new JButton("-");
		btnRemove.setBounds(418, 11, 20, 20);
		add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeActor();
			}
		});
	}
}
