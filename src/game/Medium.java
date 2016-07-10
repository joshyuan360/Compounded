package game;

import java.awt.Graphics;

/**
 * This class contains the panel that is displayed when the user wishes to play the hard level.
 * Time spent: 5 minutes (JY), 30 seconds (GL)
 * @author Joshua Yuan
 * @author George Lim
 * @version 2.0 May 23, 2014.
 * @version 3.0 Changes: Background of panel is now painted.
 * @version 4.0 - No changes made.
 * @version 5.0 - No changes made.
 */ 
public class Medium extends Game
{   
    /** Constructor sets the boundaries of the road for the game and sets the visibility of this panel to false. */
    public Medium (){
        super (266, 751, 2);
        setVisible (false);
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image.
     * For-loop is used to control the number of times the road is drawn sideways. In this case that number is 2.
     * @param xPos Stores the X position that the road needs to be drawn.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     */
    public void paintComponent (Graphics g) {
        super.paintBackground(g);
        for (int xPos = 261; xPos <= 506; xPos += 245){
            g.drawImage (road, xPos, yPos1, this);
            g.drawImage (road, xPos, yPos2, this);
        }
        super.paintComponent(g);
    }
}