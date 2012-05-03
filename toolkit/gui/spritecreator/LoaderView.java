package gui.spritecreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.JButton;

public class LoaderView extends JPanel{

	public LoaderView(ActionListener l)
	{
		setBorder(new LineBorder(new Color(128, 128, 128)));
		setBackground(new Color(192, 192, 192));
		setLayout(null);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(6, 6, 117, 29);
		add(btnLoad);
		btnLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser chooser = new JFileChooser();
			    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
			    // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
//			    ExampleFileFilter filter = new ExampleFileFilter();
//			    filter.addExtension("jpg");
//			    filter.addExtension("gif");
//			    filter.setDescription("JPG & GIF Images");
//				FileFilter filter = new AcceptAllFileFilter();
//			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getAbsolutePath());
			    }	
			}
		});
		
	}
	
}
