package gui.tilemapeditor.subviews;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class TileSelector extends JPanel{

	public TileSelector()
	{
		this.setBackground(Color.YELLOW);
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0)
			{}
			
			@Override
			public void componentResized(ComponentEvent arg0)
			{
				Component cmp = (Component) arg0.getSource();
				setPreferredSize(new Dimension(cmp.getWidth(), 100));
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0)
			{}
			
			@Override
			public void componentHidden(ComponentEvent arg0)
			{}
		});
	}
	
}
