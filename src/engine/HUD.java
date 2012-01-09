package engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 * HUD
 * @author nhydock
 *
 *  Heads up display, mainly used for facading sprites for a scene to render
 */
public abstract class HUD extends Sprite
{

    protected GameSystem parent;	//parent scene
    protected Color clearColor;		//color the background of the scene clears to
    
    /**
     * Constructs a hud
     */
    public HUD()
    {
        super("");
    }
    
    /**
     * Sets the parent system of the hud so it knows what information to draw
     * @param p
     */
    public void setParent(GameSystem p)
    {
        parent = p;
    }
    
    /**
     * Updates the display
     */
    abstract public void update();
    
    /**
     * Paints the hud
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
    }
}
