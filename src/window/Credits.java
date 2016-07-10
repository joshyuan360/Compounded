package window;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;


/**
 * This class contains the panel that displays the game credits. Total time spent: George - 30 minutes, Joshua - 1 hour.
 * @author George Lim.
 * @author Joshua Yuan.
 * @version 1.0 May 16, 2014
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - No changes made.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - June 12, 2014 - Credits image has been updated.
 */ 
public class Credits extends JPanel implements ActionListener
{
    /** The current y position of the top of the credits list. */
    private int yPos = 720;
    /** Continuously fires action events to this ActionListener. */
    private Timer timer = new Timer (10, this);
    /** Constructor sets the visibility of this panel to false. */
    public Credits () {
        setVisible (false);
        timer.start ();
        timer.setActionCommand ("animate");
    }
    /** 
     * Overrides paintComponent in JPanel to draw a background image. 
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param background Image reference: The background image of this panel.
     * @param credits Image reference: The credits image that animates on this panel.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Image background = new ImageIcon("./data/images/CreditsBackground.jpg").getImage();
        Image credits = new ImageIcon ("./data/images/CreditsList.png").getImage ();
        g.drawImage (background, 0, 0, getWidth (), getHeight (), this);
        g.drawImage (credits, getWidth () / 2 - credits.getWidth (null) / 2, yPos, this);
    }
    
    /**
     * Continuously invoked by the timer to animate the credits list image.
     * The outer if structure determines if the action event comes from the timer or the user pressing the return button.
     * The inner if structure determines what to do based on the location of the credits image. The very inner if
     * statement checks if yPos + 843 equals 480.
     * @param ae ActionEvent reference: Contains information about the action event.
     */ 
    public void actionPerformed (ActionEvent ae){
        if (ae.getActionCommand ().equals ("animate")) {
            yPos--;
            if (yPos == -843){
                timer.stop ();
            } else {
                if (yPos + 843 == 480) {
                    MainMenu.addReturnButton (this);
                }
            }
            repaint ();
        } else {
            ((CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (CompoundedFrame.MENU_PANEL);
        }
    }
}