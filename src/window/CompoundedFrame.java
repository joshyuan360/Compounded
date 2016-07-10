package window;
import highscores.HighScores;
import game.*;
import java.awt.event.KeyEvent;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * This class is the frame which is used throughout the entire Compounded game. Time spent: 10 hours
 * @author Joshua Yuan
 * @version 1.0 May 16, 2014.
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - No changes made.
 * @version 4.0 - June 6, 2014 - Added new method switchToMenu() which is used in the splashScreen() method to switch to the menu screen.
 * @version 5.0 - No changes made.
 */ 
public class CompoundedFrame extends JFrame
{
    /** Stores the panel currently contained by this frame. */
    private JPanel panel;
    /** Stores the number used to identify the instructions panel. */
    static final int INSTRUCTIONS_PANEL = KeyEvent.VK_I;
    /** Stores the number used to identify the high scores panel. */
    public static final int HIGHSCORES_PANEL = KeyEvent.VK_H;
    /** Stores the number used to identify the options panel. */
    static final int OPTIONS_PANEL = KeyEvent.VK_O;
    /** Stores the number used to identify the credits panel. */
    static final int CREDITS_PANEL = KeyEvent.VK_C;
    /** Stores the number used to identify the exit panel. */
    static final int EXIT_PANEL = KeyEvent.VK_E;
    /** Stores the number used to identify the menu panel. */
    public static final int MENU_PANEL = -1;
    /** Stores the number used to identify the game panel of easy difficulty. */
    public static final int EASY_PANEL = KeyEvent.VK_E * 100;
    /** Stores the number used to identify the game panel of medium difficulty. */
    public static final int MEDIUM_PANEL = KeyEvent.VK_M * 100;
    /** Stores the number used to identify the game panel of hard difficulty. */
    public static final int HARD_PANEL = KeyEvent.VK_H * 100;
    /** Stores the music for the game and allows for volume control */
    public static utilities.MusicPlayer music;
    
    /**
     * Sets the icon of the window, starts the music, and then switches to the menu panel.
     * @param icon Image reference: Sets the taskbar icon of this frame.
     */ 
    public CompoundedFrame () {
        super ();
        
        Image icon = new ImageIcon ("./data/images/icon.png").getImage();
        
        setSize (620, 349);
        setDefaultCloseOperation (EXIT_ON_CLOSE);
        setUndecorated (true);
        setIconImage (icon);
        setLocationRelativeTo (null);
        setVisible (true);
        
        panel = new SplashScreen ();
        add (panel);
        panel.setVisible (true);
        validate ();
        
        music = new utilities.MusicPlayer();
    }
    
    /**
     * Removes the panel currently contained by this JFrame, and adds a new one.
     * The if structure determines which panel should be added.
     * @param newPanel int: Identifies the panel to be added.
     */ 
    public void switchPanel (int newPanel){
        remove (panel);
        repaint ();
        if (newPanel == MENU_PANEL){
            panel = new MainMenu ();
            setTitle ("Compounded by Pokilangitis");
        } else if (newPanel == INSTRUCTIONS_PANEL){
            panel = new Instructions ();
            setTitle ("Compounded - Instructions");
        } else if (newPanel == HIGHSCORES_PANEL){
            panel = new HighScores ();
            setTitle ("Compounded - High Scores");
        } else if (newPanel == OPTIONS_PANEL){
            panel = new Options ();
            setTitle ("Compounded - Options");
        } else if (newPanel == CREDITS_PANEL){
            panel = new Credits ();
            setTitle ("Compounded - Credits");
        } else if (newPanel == EXIT_PANEL){
            panel = new Goodbye ();
            setTitle ("Compounded by Pokilangitis");
        } else if (newPanel == EASY_PANEL){
            panel = new Easy ();
            setTitle ("Compounded - Easy");
        } else if (newPanel == MEDIUM_PANEL){
            panel = new Medium ();
            setTitle ("Compounded - Medium");
        } else {
            if (newPanel == HARD_PANEL) {
                panel = new Hard ();
                setTitle ("Compounded - Hard");
            }
        }
        add (panel);
        panel.setVisible (true);
        panel.requestFocusInWindow ();
    }
    
    /** Switches the current screen to the menu screen. */
    public void switchToMenu (){
        remove (panel);
        dispose ();
        setUndecorated (false);
        setVisible (true);
        setIconImage (new ImageIcon ("./data/images/icon.png").getImage());
        setSize (1024, 720);
        setResizable (false);
        setDefaultCloseOperation (EXIT_ON_CLOSE);
        setLocationRelativeTo (null);
        switchPanel (MENU_PANEL);
    }
}
