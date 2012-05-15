package gui.spritecreator;

import graphics.opengl.animation.Actor;
import graphics.opengl.animation.Animation;
import graphics.opengl.animation.Sprite;

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
import javax.swing.JSeparator;

public class SpriteEdit extends JPanel {

	private Animation animation;
	private Sprite selectedSprite;
	private ActionListener cmbAListener;
	private ActionListener parentListener;

	public void setAnimation(Animation animation) {
		this.animation = animation;
		if (this.animation != null && this.animation.getFrames() != null) {
			if (this.animation.getFrames().length > 0)
				this.selectedSprite = this.animation.getFrames()[0];
		} else
			this.selectedSprite = null;
		this.updateGUI();
	}

	public Animation getAnimation() {
		return this.animation;
	}
	public Sprite getSelectedSprite() {
		return this.selectedSprite;
	}

	private void addSprite() {
		System.out.println("Animation: " + this.animation);

		System.out.println(Thread.currentThread().toString());

		if (this.animation != null) {
			if (this.animation.getFrames() == null) {
				this.animation.setFrames(new Sprite[1]);
			} else {
				Sprite[] newSprites = new Sprite[this.animation.getFrames().length + 1];
				for (int i = 0; i < this.animation.getFrames().length; i++) {
					newSprites[i] = this.animation.getFrames()[i];
				}
				this.animation.setFrames(newSprites);
			}
			this.animation.getFrames()[this.animation.getFrames().length - 1] = new Sprite(
					"texture", 0, 0, 0, 0);
		}
		this.updateGUI();
	}

	private void removeAnimation() {
		// if (this.animation.getAnimations() != null) {
		// this.animation.getAnimations().remove(this.selectedSprite);
		// }
		// this.updateGUI();
	}

	public void updateGUI() {
		this.cmbS.removeActionListener(this.cmbAListener);
		this.cmbS.removeActionListener(this.parentListener);
		this.cmbS.removeAllItems();
		if (this.animation != null && this.animation.getFrames() != null) {
			for (Sprite s : this.animation.getFrames()) {
				this.cmbS.addItem(s);
			}
		}
		this.cmbS.setSelectedItem(this.selectedSprite);
		this.cmbS.validate();
		this.cmbS.addActionListener(this.cmbAListener);
		this.cmbS.addActionListener(this.parentListener);

		String textureName = "";
		String x = "";
		String y = "";
		String width = "";
		String height = "";

		if (this.selectedSprite != null) {
			textureName = this.selectedSprite.getTextureName();
			x = "" + this.selectedSprite.getX();
			y = "" + this.selectedSprite.getY();
			width = "" + this.selectedSprite.getWidth();
			height = "" + this.selectedSprite.getHeight();
		}
		this.tfTexture.setText(textureName);
		this.tfX.setText(x);
		this.tfY.setText(y);
		this.tfW.setText(width);
		this.tfH.setText(height);
	}

	public void saveState() {
		if (this.selectedSprite != null) {
			this.selectedSprite.setTexture(this.tfTexture.getText());
			this.selectedSprite.setX(Float.valueOf(this.tfX.getText()));
			this.selectedSprite.setY(Float.valueOf(this.tfY.getText()));
			this.selectedSprite.setWidth(Float.valueOf(this.tfW.getText()));
			this.selectedSprite.setHeight(Float.valueOf(this.tfH.getText()));
		}
	}

	// GUI
	private JComboBox cmbS;
	private JTextField tfTexture;
	private JTextField tfX;
	private JTextField tfY;
	private JTextField tfH;
	private JTextField tfW;

	public SpriteEdit(ActionListener l, Animation animation) {
		this();

		this.animation = animation;
		this.parentListener = l;

		this.updateGUI();
	}

	public SpriteEdit() {
		setBorder(new LineBorder(new Color(128, 128, 128)));
		setBackground(new Color(192, 192, 192));
		setLayout(null);

		cmbS = new JComboBox();
		cmbS.setBounds(129, 8, 271, 27);
		add(cmbS);
		cmbS.setActionCommand("sprite");
		cmbAListener = (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedSprite = (Sprite) ((JComboBox) arg0.getSource())
						.getSelectedItem();
				System.out.println(selectedSprite);
				updateGUI();
			}
		});
		cmbS.addActionListener(cmbAListener);
		cmbS.validate();

		JLabel lblNewLabel = new JLabel("Texture:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(6, 46, 111, 16);
		add(lblNewLabel);

		tfTexture = new JTextField();
		tfTexture.setBounds(129, 40, 271, 28);
		add(tfTexture);
		tfTexture.setColumns(10);
		tfTexture.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblActor = new JLabel("Sprite number:");
		lblActor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActor.setBounds(6, 12, 111, 16);
		add(lblActor);

		tfX = new JTextField();
		tfX.setColumns(10);
		tfX.setBounds(129, 98, 68, 28);
		add(tfX);
		tfX.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblY = new JLabel("X:");
		lblY.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY.setBounds(6, 104, 111, 16);
		add(lblY);

		JButton btnAdd = new JButton("+");
		btnAdd.setBounds(399, 11, 20, 20);
		add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSprite();
			}
		});

		JButton btnRemove = new JButton("-");
		btnRemove.setBounds(418, 11, 20, 20);
		add(btnRemove);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(128, 128, 128));
		separator.setBounds(16, 74, 422, 12);
		add(separator);

		JLabel lblY_1 = new JLabel("Y:");
		lblY_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblY_1.setBounds(6, 144, 111, 16);
		add(lblY_1);

		tfY = new JTextField();
		tfY.setColumns(10);
		tfY.setBounds(129, 138, 68, 28);
		add(tfY);
		tfY.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHeight.setBounds(209, 144, 111, 16);
		add(lblHeight);

		tfH = new JTextField();
		tfH.setColumns(10);
		tfH.setBounds(332, 138, 68, 28);
		add(tfH);
		tfH.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		tfW = new JTextField();
		tfW.setColumns(10);
		tfW.setBounds(332, 98, 68, 28);
		add(tfW);
		tfW.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				saveState();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWidth.setBounds(209, 104, 111, 16);
		add(lblWidth);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAnimation();
			}
		});
	}
}
