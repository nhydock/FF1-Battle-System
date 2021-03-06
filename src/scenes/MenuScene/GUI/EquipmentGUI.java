package scenes.MenuScene.GUI;

import java.awt.Graphics;

import scenes.HUD;
import scenes.MenuScene.System.EquipmentState;
import scenes.MenuScene.System.WeaponState;
import actors.Player;
import engine.Engine;
import graphics.NES;
import graphics.SFont;
import graphics.SWindow;
import groups.Party;

/**
 * Equipment GUI
 * @author nhydock
 *
 *	Dialog that shows all the player's equipment and which things are equipped
 */
public class EquipmentGUI extends HUD
{
	Engine e = Engine.getInstance();		//engine
	Party p;								//party
	
	SWindow titleWindow;						//shows menu selection
	ModeWindow modeWindow;					//mode window
	EquipmentWindow[] eWindows;				//equipment windows
	MenuGUI parentGUI; 						//core gui for the menu system
	
	SFont f = SFont.loadFont("default");
	
	/**
	 * Constructs the gui component
	 * @param parent
	 */
	public EquipmentGUI(MenuGUI parent)
	{
		parentGUI = parent;
		this.parent = parentGUI.getParent();
		p = e.getParty();
		titleWindow = new SWindow(7, 1, 58, 32, NES.BLUE);
		modeWindow = new ModeWindow(63, 1);
		eWindows = new EquipmentWindow[p.size()];
		for (int i = 0; i < eWindows.length; i++)
		{
			eWindows[i] = new EquipmentWindow(this, p.get(i), 7, 33+(i*48));
		}
	}
	
	/**
	 * Paints the component
	 */
	@Override
	public void paint(Graphics g)
	{
		titleWindow.paint(g);
		if (parent.getState() instanceof WeaponState)
			f.drawString(g, "Weapon", 0, 14, 1, titleWindow);
		else
			f.drawString(g, "Armor", 0, 14, 1, titleWindow);
			
		modeWindow.paint(g);
		for (int i = 0; i < eWindows.length; i++)
			eWindows[i].paint(g);
	}
	
	/**
	 * Gets the position on screen of where the global arrow should draw
	 * @return
	 */
	@Override
	public int[] updateArrowPosition(int index)
	{
		EquipmentState e = (EquipmentState)parentGUI.state;
		if (e.getMode() == 0)
		{
			arrowPosition[0] = modeWindow.getX() + 8 + 56*index;
			arrowPosition[1] = modeWindow.getY() + 14;
		}
		else
		{
			int i = index / 4;
			int x = index % 2;
			int y = index % 4 / 2;
			arrowPosition[0] = eWindows[i].getX() + 58 + 80*x;
			arrowPosition[1] =  eWindows[i].getY() + 16 + 12*y;
		}
		return arrowPosition;
	}

	/**
	 * Do nothing
	 */
	@Override
	public void update(){
		//itemWindow.updateIndex(parentGUI.state.getIndex());
	}
	

	/**
	 * ModeWindow
	 * 
	 * @author nhydock
	 * 
	 * Window that displays modes of interaction with equipment in the menu
	 */
	class ModeWindow extends SWindow
	{
		public static final int WIDTH = 186;
		public static final int HEIGHT = 32;

		SWindow w;

		SFont f = SFont.loadFont("default");
		
		public ModeWindow(int x, int y) {
			super(x, y, 1, 1);
			w = new SWindow(x, y, WIDTH, HEIGHT, NES.BLUE);
		}

		@Override
		public void paint(Graphics g) {
			w.paint(g);
			f.drawString(g, "EQUIP", 14, 14, w);
			f.drawString(g, "TRADE", 70, 14, w);
			f.drawString(g, "DROP", 126, 14, w);	
		}
	}
	
	/**
	 * EquipmentWindow
	 * 
	 * @author nhydock
	 * 
	 * Window the name of the player and their equipment
	 */
	class EquipmentWindow extends SWindow
	{
		SWindow name;
		SWindow items;

		Player p;
		HUD parent;
		
		SFont f = SFont.loadFont("default");
		
		public EquipmentWindow(HUD parentGUI, Player p, int x, int y) {
			super(x, y, 1, 1);
			parent = parentGUI;
			this.p = p;
			name = new SWindow(x, y, 66, 32, NES.BLUE);
			items = new SWindow(x+56, y, 186, 48, NES.BLUE);
		}

		@Override
		public void paint(Graphics g) {
			items.paint(g);
			name.paint(g);
			f.drawString(g, p.getName(), 0, 14, 1, name);
			
			for (int i = 0; i < 4; i++)
			{
				String t;
				if (parent.getParent().getState() instanceof WeaponState)
					t = ((p.getWeapon() == p.getWeapons()[i] && p.getWeapon() != null)?"E-":"") + ((p.getWeapons()[i] != null)?p.getWeapons()[i].getName():"");
				else
					t = ((p.isWearing(p.getArmor()[i])?"E-":"") + ((p.getArmor()[i] != null)?p.getArmor()[i].getName():""));
				f.drawString(g, t, 20+(items.getWidth()/2*(i%2)), 14+(12*(i/2)), items);
			}
		}
	}
}
