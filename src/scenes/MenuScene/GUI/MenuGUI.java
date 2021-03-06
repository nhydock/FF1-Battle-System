package scenes.MenuScene.GUI;

import graphics.NES;
import graphics.Sprite;

import java.awt.Graphics;

import scenes.GameState;
import scenes.HUD;
import scenes.MenuScene.System.*;

/**
 * MenuGUI
 * @author nhydock
 *
 *	Displays the interface for the main system menu gui
 */
public class MenuGUI extends HUD
{

	Sprite arrow;		//arrow pointer
	
	GameState state;	//the current system state
	
	MainGUI mg;			//main gui
	InventoryGUI ig;	//inventory gui
	MagicGUI mag;		//magic gui
	StatusGUI sg;		//status gui
	EquipmentGUI wg;	//weapon gui
	EquipmentGUI ag;	//armor gui
	OrderGUI og;		//party order gui
	
	HUD currentGUI;		//the gui that is currently supposed to be visible
	int index;
	
	/**
	 * Construct the gui
	 */
	public MenuGUI(MenuSystem parent)
	{
		this.parent = parent;
		arrow = NES.ARROW;
		mg = new MainGUI(this);
		ig = new InventoryGUI(this);
		mag = new MagicGUI(this);
		sg = new StatusGUI(this);
		wg = new EquipmentGUI(this);
		ag = new EquipmentGUI(this);
		og = new OrderGUI(this);
		
		currentGUI = mg;
	}
	
	/**
	 * Pulls the cursor's index
	 */
	@Override
	public void update()
	{
		state = parent.getState();
		if (state instanceof MenuState)
			currentGUI = mg;
		else if (state instanceof InventoryState)
			currentGUI = ig;
		else if (state instanceof MagicState)
			currentGUI = mag;
		else if (state instanceof StatusState)
			currentGUI = sg;
		else if (state instanceof WeaponState)
			currentGUI = wg;
		else if (state instanceof ArmorState)
			currentGUI = ag;
		else if (state instanceof OrderState)
			currentGUI = og;
		index = state.getIndex();
		currentGUI.update();
	}

	/**
	 * paints the interface
	 */
	@Override
	public void paint(Graphics g)
	{
		
		currentGUI.paint(g);
		
		arrowPosition = currentGUI.updateArrowPosition(index);
		arrow.setX(arrowPosition[0]);
		arrow.setY(arrowPosition[1]);
		arrow.paint(g);
		
		/*
		 * Draw another arrow when a member is selected for swapping in the order
		 * scene and the party member is not outside the view range of party
		 * members.
		 */
		if (currentGUI == og)
		{
			OrderState s = (OrderState)parent.getState();
			if (s.getSelectedIndex() != -1 && !og.showSelectedWin)
			{
				arrowPosition = currentGUI.updateArrowPosition(s.getSelectedIndex());
				arrow.setX(arrowPosition[0]);
				arrow.setY(arrowPosition[1]);
				arrow.paint(g);
			}
		}
	}
}
