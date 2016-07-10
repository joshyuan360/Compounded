package game;

import java.awt.Graphics;

/**
 * This class contains the panel that is displayed when the user wishes to play the hard level.
 * Time spent: 30 seconds (JY), 30 seconds (GL)
 * @author Joshua Yuan, George Lim.
 * @version 2.0 May 17, 2014.
 * @version 3.0 Changes: The background of this panel is now painted.
 * @version 4.0 June 6, 2014 - No changes made.
 * @version 5.0 June 12, 2013 - No changes made.
 */ 
public class Hard extends Game
{   
    /** Constructor sets the boundaries of the road for the game and sets the visibility of this panel to false. */
    public Hard (){
        super (144, 873, 3);
        setVisible (false);
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image.
     * For-loop is used to control the number of times the road is drawn sideways. In this case that number is 3.
     * @param xPos int: Stores the X position that the road needs to be drawn.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     */
    public void paintComponent (Graphics g) {
        super.paintBackground(g);
        for (int xPos = 139; xPos <= 629; xPos += 245){
            g.drawImage (road, xPos, yPos1, this);
            g.drawImage (road, xPos, yPos2, this);
        }
        super.paintComponent(g);
    }
}
