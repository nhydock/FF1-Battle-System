package editor.SpellEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.ini4j.Ini;

import spell.Spell;

import commands.SpellCommand;

import editor.ToolKit;

/**
 * SpellEditorGUI
 * @author nhydock
 *
 *	Simple GUI for creating and editing spells
 *	Special feature is the equation builder and tester for spell
 */
public class SpellEditorGUI extends JPanel implements ActionListener, MouseListener{
	
	public static Vector<String> AVAILABLESPELLS = new Vector(){
		{
			for (String s : ToolKit.spells)
				add(s);
		}
	};
	
	public static Vector<String> AVAILABLEANIMATIONS = new Vector(){
		{
			for (String s : ToolKit.animations)
				add(s);
		}
	};
	
	
	/*
	 * Buttons
	 */
	JButton newButton;
	JButton saveButton;
	JButton restoreButton;
	JButton testButton;
	
	/*
	 * Spell switcher
	 */
	JList spellList;
	JScrollPane spellListPane;
	
	/*
	 * Fields
	 */
	JTextField nameField;		//holds the name
	JComboBox animation;		//list of animations that can be used for the spell
	JTextField value;			//Holds either an equation or a constant value for determining the effects of the spell
	
	/*
	 * Type Switcher
	 */
	ButtonGroup types;
	JRadioButton constantType;
	JRadioButton variableType;
	
	/*
	 * Enemy Group Switcher
	 */
	final static String[] TARGETS = {"Enemy", "Ally"};
	final static String[] RANGES = {"Single", "Group", "All"};
	ButtonGroup target;
	ButtonGroup targetRange;
	JRadioButton[] targetButtons = new JRadioButton[TARGETS.length];
	JRadioButton[] targetRangeButtons = new JRadioButton[RANGES.length];
	
	/*
	 * EQUATION EDITOR
	 */
	final static String[] STATS = {"STR", "DEF", "VIT", "INT", "SPD", "LUCK", "EVADE", "RESIST", "Hit %"};
	final static String[] MATH = {"+", "-", "*", "/", "%", "^", "(", ")"};
	JSpinner mpSpinner;
	JSpinner lvlSpinner;
	JButton[] statButtons;
	JButton[] mathButtons;
	
	/*
	 * Enemy elemental properties
	 */
	final static String[] ELEM = {"FIRE", "FREZ", "ELEC", "DEMI", "LGHT"};	
	JCheckBox[] elemEnablers;
	
	Font font = new Font("Arial", 1, 32);
	
	public SpellEditorGUI()
	{
		setLayout(null);
		
		JLayeredPane jlp;
		JLabel l;
		
		/*
		 * Initialize fields 
		 */
		l = new JLabel("Spells: ");
		l.setSize(200,16);
		l.setLocation(10,10);
		spellList = new JList(AVAILABLESPELLS);
		spellList.addMouseListener(this);
		spellListPane = new JScrollPane(spellList);
		spellListPane.setSize(200, 343);
		spellListPane.setLocation(10, 32);
		
		add(l);
		add(spellListPane);
		refreshList();
		
		l = new JLabel("Name: ");
		l.setSize(l.getPreferredSize());
		l.setLocation(220, 10);
		nameField = new JTextField("");
		nameField.setSize(200, 24);
		nameField.setLocation(220, 32);
		
		add(l);
		add(nameField);
		
		l = new JLabel("LVL: ");
		l.setSize(l.getPreferredSize());
		l.setLocation(440, 34);
		lvlSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9, 1));
		lvlSpinner.setSize(44, 24);
		lvlSpinner.setLocation(480, 32);
		add(l);
		add(lvlSpinner);
				
		l = new JLabel("Battle Animation: ");
		l.setSize(l.getPreferredSize());
		l.setLocation(650, 10);
		
		animation = new JComboBox(AVAILABLEANIMATIONS);
		animation.setSize(230, 24);
		animation.setLocation(650, 32);
		add(l);
		add(animation);
		
		/*
		 * Value Type button group
		 */
		jlp = new JLayeredPane();
		jlp.setSize(new Dimension(120, 80));
		jlp.setLocation(220, 64);
		jlp.setBorder(BorderFactory.createTitledBorder("Damage Type: "));
		types = new ButtonGroup();
		constantType = new JRadioButton("Constant");
		constantType.setLocation(10, 20);
		constantType.setSize(constantType.getPreferredSize());
		constantType.setSelected(true);
		types.add(constantType);
		variableType = new JRadioButton("Variable");
		variableType.setLocation(10, 48);
		variableType.setSize(variableType.getPreferredSize());
		types.add(variableType);
		jlp.add(constantType);
		jlp.add(variableType);
		add(jlp);
		
		/*
		 * Target Type button groups
		 */
		jlp = new JLayeredPane();
		jlp.setSize(new Dimension(300, 40));
		jlp.setLocation(340, 64);
		jlp.setBorder(BorderFactory.createTitledBorder("Target: "));
		target = new ButtonGroup();
		
		for (int i = 0; i < TARGETS.length; i++)
		{
			JRadioButton b = new JRadioButton(TARGETS[i]);
			b.setLocation(80 + i*80, 18);
			b.setSize(78, 16);
			jlp.add(b);
			target.add(b);
			targetButtons[i] = b;
		}
		add(jlp);
		
		jlp = new JLayeredPane();
		jlp.setSize(new Dimension(300, 40));
		jlp.setLocation(340, 104);
		jlp.setBorder(BorderFactory.createTitledBorder("Target Range: "));
		targetRange = new ButtonGroup();
		
		
		for (int i = 0; i < RANGES.length; i++)
		{
			JRadioButton b = new JRadioButton(RANGES[i]);
			b.setLocation(40 + i*80, 18);
			b.setSize(78, 16);
			jlp.add(b);
			targetRange.add(b);
			targetRangeButtons[i] = b;
		}
		add(jlp);
		
		
		/*
		 * Elemental checkbox initialization
		 */
		jlp = new JLayeredPane();
		jlp.setSize(new Dimension(230, 80));
		jlp.setLocation(650, 120);
		jlp.setBorder(BorderFactory.createTitledBorder("Elemental Alignment: "));
		
		elemEnablers = new JCheckBox[ELEM.length];
		
		for (int i = 0; i < ELEM.length; i++)
		{
			JCheckBox s = new JCheckBox(ELEM[i]);
			s.setSelected(false);
			s.setSize(70, 24);
			s.setLocation(10 + 72*(i%3), 20 + 30*(i/3));
			elemEnablers[i] = s;
			jlp.add(s);
		}
		add(jlp);
		
		
		/*
		 * Equation Editor
		 */
		l = new JLabel("Value: ");
		l.setLocation(220, 148);
		l.setSize(l.getPreferredSize());
		add(l);
		value = new JTextField("0");
		value.setLocation(220, 168);
		value.setSize(420, 24);
		add(value);
		
		jlp = new JLayeredPane();
		jlp.setSize(new Dimension(420, 176));
		jlp.setLocation(220, 200);
		jlp.setBorder(BorderFactory.createTitledBorder("EQUATION EDITOR:"));
		
		statButtons = new JButton[STATS.length];
		
		for (int i = 0; i < STATS.length; i++)
		{
			JButton s = new JButton(STATS[i]);
			s.setSize(90, 24);
			s.setLocation(15 + (i%4)*100, 20 + 30*(i/4));
			statButtons[i] = s;
			jlp.add(s);
		}
		
		mathButtons = new JButton[MATH.length];
		
		for (int i = 0; i < MATH.length; i++)
		{
			JButton s = new JButton(MATH[i]);
			s.setSize(48, 24);
			s.setLocation(125+(80*(int)(i/4)) + (i%4)*54, 115+(30*(int)(i/4)));
			mathButtons[i] = s;
			jlp.add(s);
		}
		
		for (Component j : jlp.getComponents())
			j.setEnabled(false);
		
		add(jlp);
		
		/*
		 * Initialize Buttons
		 */
		testButton = new JButton("Test");
		testButton.setSize(230, 32);
		testButton.setLocation(650, 215);
		
		int[] buttonSize = {230, 24};
		newButton = new JButton("New");
		newButton.setSize(buttonSize[0], buttonSize[1]);
		newButton.setLocation(650, 290);
		saveButton = new JButton("Save");
		saveButton.setSize(buttonSize[0], buttonSize[1]);
		saveButton.setLocation(650, 320);
		restoreButton = new JButton("Restore");
		restoreButton.setSize(buttonSize[0], buttonSize[1]);
		restoreButton.setLocation(650, 350);
		
		newButton.addActionListener(this);
		saveButton.addActionListener(this);
		restoreButton.addActionListener(this);
		
		add(testButton);
		add(newButton);
		add(saveButton);
		add(restoreButton);

	}

	
	/**
	 * Handles most input
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newButton)
		{
			reset();
		}
		else if (event.getSource() == saveButton)
		{
			saveSpell();
		}
	}

	/**
	 * Loads an enemies values into the editor's fields and makes it the active enemy
	 * @param e
	 */
	public void loadSpell(Spell s)
	{
		//load name
		nameField.setText(s.getName());
		lvlSpinner.setValue(s.getLevel());
		
		//load elemental resistance
		for (int i = 0; i < ELEM.length; i++)
			elemEnablers[i].setSelected(s.getElementalAlignment(i));
	}
	
	/**
	 * Sets all fields to their default values
	 */
	public void reset()
	{
		nameField.setText("");
		lvlSpinner.setValue(1);
		
		for (int i = 0; i < ELEM.length; i++)
			elemEnablers[0].setSelected(false);
				
	}
	
	/**
	 * Saves the editor's values to the enemy ini
	 */
	public void saveSpell()
	{
		String location = "data/actors/enemies/";
		
		//save to file
		try {
			File f = new File(location+nameField.getText()+"/enemy.ini");
			f.delete();					//deletes the old file
			f.createNewFile();			//saves data to new file
			Ini ini = new Ini(f);
			
			//saving main
			ini.add("spell", "lvl", lvlSpinner);
			
			//saving element
			for (int i = 0; i < ELEM.length; i++)
				ini.add("spell", ELEM[i], elemEnablers[0].isSelected());
			
			ini.store(f);
			
			//refresh the list of names after saving
			loadSpell(Spell.getSpell(nameField.getText()));
			refreshList();	
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public void refreshList()
	{
		AVAILABLESPELLS = new Vector<String>()
		{
			{
				String[] s = new File("data/spells").list(new FilenameFilter() {
		            @Override
					public boolean accept(File f, String s) {
		            	return (new File("data/spells/"+s+"/spell.ini").exists());
		              }
		            });
				for (String str : s)
					add(str);
			}
		};
		
		spellList.setListData(AVAILABLESPELLS);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource() == spellList)
			loadSpell(Spell.getSpell((String)spellList.getSelectedValue()));
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {}


	@Override
	public void mouseExited(MouseEvent arg0) {}


	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
