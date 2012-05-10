package gui.spritecreator;

import graphics.opengl.Actor;
import graphics.opengl.Animation;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.swing.border.LineBorder;
import javax.swing.JButton;

public class AnimationEdit extends JPanel {

	private Actor actor;
	private Animation selectedAnimation;
	private ActionListener cmbAListener;
	private ActionListener parentListener;

	public void setActor(Actor actor) {
		this.actor = actor;
		if (this.actor.getAnimations() != null) {
			Collection<Animation> as = this.actor.getAnimations().values();
			for (Animation a : as) {
				this.selectedAnimation = a;
				break;
			}
		} else {
			this.selectedAnimation = null;
		}
		this.updateGUI();
	}

	public Animation getSelectedAnimation() {
		return this.selectedAnimation;
	}

	public Actor getActor() {
		return this.actor;
	}

	private void addAnimation() {
		this.actor.addAnimation(new Animation(("New Animation " + this.actor
				.getAnimations().size()), null, 1000.0));
		this.updateGUI();
	}

	private void removeAnimation() {
		if (this.actor.getAnimations() != null) {
			this.actor.getAnimations().remove(this.selectedAnimation);
		}
		this.updateGUI();
	}

	public void updateGUI() {
		System.out.println("UpdateGUI animEd");
		if (this.actor != null) {
			System.out.println("Num anim: "
					+ this.actor.getAnimations().values().size());
			this.cmbA.removeActionListener(this.cmbAListener);
			this.cmbA.removeActionListener(this.parentListener);
			this.cmbA.removeAllItems();
			if (this.actor.getAnimations() != null) {
				for (Animation a : this.actor.getAnimations().values()) {
					this.cmbA.addItem(a);
				}
			}
			this.cmbA.setSelectedItem(this.selectedAnimation);
			this.cmbA.validate();
			this.cmbA.addActionListener(this.cmbAListener);
			this.cmbA.addActionListener(this.parentListener);

			String name = "";
			String duration = "";

			if (this.selectedAnimation != null) {
				name = this.selectedAnimation.getName();
				duration = "" + this.selectedAnimation.getDuration();
			}
			this.tfName.setText(name);
			this.tfD.setText(duration);
		}
	}

	public void saveState() {
		if (this.selectedAnimation != null) {
			this.selectedAnimation.setName(this.tfName.getText());
			this.selectedAnimation.setDuration(Double.valueOf(this.tfD
					.getText()));
		}
	}

	// GUI
	private JComboBox cmbA;
	private JTextField tfName;
	private JTextField tfD;

	public AnimationEdit(ActionListener l, Actor actor) {
		this();

		this.actor = actor;
		this.parentListener = l;

		this.updateGUI();
	}

	public AnimationEdit() {
		setBorder(new LineBorder(new Color(128, 128, 128)));
		setBackground(new Color(192, 192, 192));
		setLayout(null);

		cmbA = new JComboBox();
		cmbA.setBounds(129, 8, 271, 27);
		add(cmbA);
		cmbA.setActionCommand("animation");
		cmbAListener = (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedAnimation = (Animation) ((JComboBox) arg0.getSource())
						.getSelectedItem();
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
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblActor = new JLabel("Animation:");
		lblActor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActor.setBounds(6, 12, 111, 16);
		add(lblActor);

		tfD = new JTextField();
		tfD.setColumns(10);
		tfD.setBounds(129, 74, 68, 28);
		add(tfD);
		tfD.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblY = new JLabel("Duration:");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY.setBounds(6, 80, 111, 16);
		add(lblY);

		JButton btnAdd = new JButton("+");
		btnAdd.setBounds(399, 11, 20, 20);
		add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAnimation();
			}
		});

		JButton btnRemove = new JButton("-");
		btnRemove.setBounds(418, 11, 20, 20);
		add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAnimation();
			}
		});
	}
}
