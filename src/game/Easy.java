package game;

import java.awt.Graphics;
/**
 * This class contains the panel that is displayed when the user wishes to play the easy level. Total time spent: George - 15 minutes, Joshua - 30 minutes
 * @author George Lim
 * @author Joshua Yuan
 * @version 1.0 May 16, 2014
 * @version 2.0 - May 23, 2014 - Background of the panel is now painted.
 * @version 3.0 - May 30, 2014 - No changes made.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - June 12, 2013 - No changes made.
 */ 
public class Easy extends Game
{   
    /** Constructor sets the boundaries of the road for the game and sets the visibility of this panel to false. */
    public Easy (){
        super (389, 628, 1);
        setVisible (false);
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image. 
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     */
    public void paintComponent (Graphics g){
        super.paintBackground(g);
        g.drawImage (road, 384, yPos1, this);
        g.drawImage (road, 384, yPos2, this);
        super.paintComponent(g);
    }
}