package gui.tilemapeditor.subviews;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import tilemap.Tile;
import tilemap.TileMap;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TileInspector extends JPanel {

	private Tile	currentTile;
	JLabel			lblType, lblCollidable;

	public TileInspector()
	{
		this.initGui();
		this.updateGui();
	}

	public void setTile(Tile tile)
	{
		this.currentTile = tile;
		this.updateGui();
	}

	private void updateGui()
	{
		if (this.currentTile != null) {
			this.lblType.setText(""+this.currentTile.getType());
			this.lblCollidable.setText(this.currentTile.isCollidable() ? "YES" : "NO");
		} else {
			this.lblType.setText(""+0);
			this.lblCollidable.setText("N/A");
		}
	}

	private void initGui()
	{
		setLayout(null);

		JLabel lblNewLabel = new JLabel("Tile");
		lblNewLabel.setBounds(6, 6, 61, 16);
		add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(6, 34, 198, 71);
		add(panel);
		panel.setLayout(null);

		JLabel herp = new JLabel("Type: ");
		herp.setHorizontalAlignment(SwingConstants.RIGHT);
		herp.setToolTipText("This is the integer value representing a pixel when saving the tilemap to disk.");
		herp.setBounds(6, 12, 78, 16);
		panel.add(herp);

		lblType = new JLabel("0");
		lblType.setBounds(96, 12, 77, 16);
		panel.add(lblType);

		lblCollidable = new JLabel("0");
		lblCollidable.setBounds(96, 40, 77, 16);
		panel.add(lblCollidable);

		JLabel derp1 = new JLabel("Collidable: ");
		derp1.setToolTipText("This is the integer value representing a pixel when saving the tilemap to disk.");
		derp1.setHorizontalAlignment(SwingConstants.RIGHT);
		derp1.setBounds(6, 40, 78, 16);
		panel.add(derp1);
	}
}
