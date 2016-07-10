package window;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * This class contains the panel that is displayed when the user wishes to exit. 
 * Time spent: George - 30 minutes, Joshua - 45 minutes
 * @author Joshua Yuan
 * @author George Lim
 * @version 2.0 May 23, 2014.
 * @version 3.0 May 30, 2014 - No changes made.
 * <p>Changes (3.0): Added a working timer to close the program successfully after 3 seconds.
 * @version 4.0 June 6, 2014 - No changes made.
 * @version 5.0 June 12, 2014 - No changes made.
 */ 
public class Goodbye extends JPanel implements ActionListener
{
    /** Constructor sets visibility to false and sets up a new timer to close the program after 3 seconds. */
    public Goodbye (){
        setVisible (false);
        new Timer(3000, this).start();
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image. 
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param background Image reference: The background image of this panel.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Image background = new ImageIcon("./data/images/Goodbye.jpg").getImage();
        g.drawImage(background, 0, 0, getWidth (), getHeight (), this);
    }
    
    /**
     * An action event is fired to this class after 3 seconds.
     * @param ae ActionEvent reference that stores the action that the user executed to trigger this method.
     */
    public void actionPerformed(ActionEvent ae) {
        System.exit(0);
    }
}