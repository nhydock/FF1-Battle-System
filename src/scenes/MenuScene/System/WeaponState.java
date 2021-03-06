package scenes.MenuScene.System;

import scenes.GameSystem;
import engine.Input;

public class WeaponState extends EquipmentState
{

	public WeaponState(GameSystem c)
	{
		super(c);
	}

	/**
	 * Handles input/navigating the list of items
	 */
	@Override
	public void handleKeyInput(int key)
	{
		if (mode != 0)
		{
			if (key == Input.KEY_A)
			{
				if (player.getWeapon() == player.getWeapons()[row+col])
					player.setWeapon(null);
				else if (player.getWeapons()[row+col] != null)
					player.setWeapon(player.getWeapons()[row+col]);
			}
		}
		
		super.handleKeyInput(key);
	}
}
