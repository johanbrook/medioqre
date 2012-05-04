package gui.tilemapeditor.messagedialogs;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class TileMapSizeMessageDialog {
	private JTextField tfRows;
	private JTextField tfCols;

	public final int NO_OPTION = -1;
	public final int DONE_OPTION = 0;
	public final int CANCEL_OPTION = 1;

	int index = NO_OPTION;
	Object lock;

	JFrame frame; 
	
	public TileMapSizeMessageDialog()
	{
		this.initGui();

	}

	public int showTileMapSizeDialog()
	{
		frame.setVisible(true);
		
		while (this.frame.isShowing()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Index: "+index);
		return index;
	}

	private void done()
	{
		index = DONE_OPTION;

		synchronized (this) {
			this.notifyAll();
		}
	}

	private void cancel()
	{
		index = CANCEL_OPTION;

		synchronized (this) {
			this.notifyAll();
		}
	}

	private void initGui()
	{
		frame = new JFrame();
		
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(6, 34, 230, 71);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Rows:");
		lblNewLabel.setBounds(6, 12, 70, 16);
		panel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblColumns = new JLabel("Columns:");
		lblColumns.setBounds(6, 40, 70, 16);
		panel.add(lblColumns);
		lblColumns.setHorizontalAlignment(SwingConstants.RIGHT);

		tfCols = new JTextField();
		tfCols.setBounds(88, 34, 134, 28);
		panel.add(tfCols);
		tfCols.setColumns(10);

		tfRows = new JTextField();
		tfRows.setBounds(88, 6, 134, 28);
		panel.add(tfRows);
		tfRows.setColumns(10);

		JLabel lblSize = new JLabel("Size");
		lblSize.setBounds(6, 6, 61, 16);
		frame.getContentPane().add(lblSize);

		JButton btnDone = new JButton("Done");
		btnDone.setBounds(119, 117, 117, 29);
		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				done();
			}
		});
		frame.getContentPane().add(btnDone);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				cancel();
			}
		});
		btnCancel.setBounds(6, 117, 117, 29);
		frame.getContentPane().add(btnCancel);

		frame.setPreferredSize(new Dimension(243, 180));
		frame.pack();
	}
}
