package editor.MapEditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * NewMapDialog
 * @author nhydock
 *
 *	Little window for creating a new map
 */
public class NewMapDialog extends JDialog implements ActionListener {

	JTextField width;			//width of the map
	JTextField height;			//height of the map
	JButton okButton;			//end dialog
	
	MapEditorGUI parent;		//parent gui
	
	private static final int MAXWIDTH = 500;
	private static final int MAXHEIGHT = 500;
	
	
	/**
	 * Constructs the dialog window
	 * @param p		Parent GUI
	 */
	public NewMapDialog(MapEditorGUI p)
	{
		parent = p;
		setLayout(null);
		
		setBounds(10, 10, 180, 76);
		
		JLabel l = new JLabel("Enter the map's dimensions");
		l.setLocation(10, 16);
		l.setSize(200, 24);
		JLabel l2 = new JLabel("x");
		l2.setLocation(120, 48);
		l2.setSize(20,20);
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		width = new JTextField("1");
		width.setSize(110, 24);
		width.setLocation(5, 48);
		
		height = new JTextField("1");
		height.setSize(110, 24);
		height.setLocation(132, 48);
		
		okButton = new JButton("OK");
		okButton.setSize(100, 32);
		okButton.setLocation(75, 74);
		
		okButton.addActionListener(this);
		
		add(l);
		add(l2);
		add(width);
		add(height);
		add(okButton);
		
		setSize(255,148);
		setVisible(true);
		setModal(true);
		setResizable(false);
	}

	/**
	 * Accepts button clicking
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == okButton)
		{
			
			try
			{
				int w, h;
				w = Integer.parseInt(width.getText());
				h = Integer.parseInt(height.getText());
				
				if (w > MAXWIDTH || h > MAXHEIGHT || w < 1 || h < 1)
					throw new Exception();
				parent.newMap(w, h);
				dispose();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, "Woah there, dimensions need to be within 1x1 to " + MAXWIDTH + "x" + MAXHEIGHT);
			}
		}
	}
	
}
