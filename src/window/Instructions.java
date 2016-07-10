package window;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * This class contains the panel that is displayed when the user wishes to view the game instructions.
 * Time spent: 10 hours.
 * @author Joshua Yuan.
 * @author George Lim.
 * @version 1.0 May 16, 2014.
 * @version 2.0 May 23, 2014 - No changes made.
 * @version 3.0 May 30, 2014 - No changes made.
 * @version 4.0 June 6, 2014 - Instruction images added. The user can now cycle through the images. Not all of the
 * images have been added.
 * @version 5.0 June 12, 2014 - New instruction images added. Arrows addded.
 */ 
public class Instructions extends JPanel implements KeyListener, ActionListener
{
    /** The background image of this JPanel. */
    private Image background = new ImageIcon("./data/images/Goodbye.jpg").getImage();
    /** The help bar used in this JPanel. */
    private Image helpBar = new ImageIcon("./data/images/debugBubble.png").getImage();
    /** Contains all the images used to show instructions to the user. */
    private static Image [] page = new Image [9];
    /** The index in <code>page</code> which contains the <code>Image</code> that is currently being displayed */
    private int pageNumber = 0;
    /** The font used to display test. */
    private Font font;
    
    /**
     * This method loads the page array. It is made public and static so that it can be loaded during splashscreen as a
     * Thread. Otherwise, it may take a while for instruction images to load after the user has clicked instructions
     * on the main menu. The for loop iterates through all of the instruction images in the folder.
     * @param i int: Used as a for loop to load all of the instruction images.
     */ 
    public static void loadImages (){
        for (int i = 0; i < page.length; i++){
            page [i] = new ImageIcon ("./data/images/page" + i + ".png").getImage();
        }
    }
    
    /** 
     * Constructor that sets the visibility of this panel to false and adds keyboard user input detection. The
     * The try-catch is used to catch a FontFormatException or an IOException.
     * @param e FontFormatException and IOException reference: Contains information about the exception, if thrown.
     * @throws FontFormatException if there is a problem creating the font.
     * @throws IOException if there is a problem reading the file.
     */
    public Instructions (){
        setVisible (false);
        addKeyListener(this);
        MainMenu.addReturnButton (this);
        MainMenu.setButtons (new String [] {"leftArrow", "rightArrow", "chemistryHelp"},
                             new int [] {338, 569, 820},
                             new int [] {30, 30, 15},
                             this);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("./data/fonts/goodtime.ttf"));
        } catch (FontFormatException e){
        } catch (IOException e) {
        }
        requestFocusInWindow ();
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image. 
     * The first if structure checks if <code>pageNumber</code> is smaller or equal to -1. The if structure nested within the
     * first if structure check if pageNumber is larger or equal to page.length. The last if structure determines
     * whether a filled or empty circle should be drawn, depending on the page number currently being displayed.
     * The first block of code prevents pageNumber from going out of bounds. This block of code must be located in 
     * paintComponent since repaint is sometimes called by JPanel itself. The second block of code paints this panel.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param i int: Used as a for loop variable to draw the page dots on the instructions panel.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        if (pageNumber <= -1){
            pageNumber = page.length - 1;
        } else {
            if (pageNumber >= page.length){
                pageNumber = 0;
            }
        }
        
        g.drawImage (page [pageNumber], 0, 0, this);
        g.drawImage (helpBar, 784, 0, this);
        g.setFont (font.deriveFont(14.0f));
        g.setColor (Color.BLACK);
        g.drawString ("Page " + (pageNumber + 1) + " of 9", 850, 70);
        for (int i = 815; i <= 975; i += 20){
            if ((i - 815)/20 <= pageNumber){
                g.fillOval (i, 80, 10, 10);
            } else {
                g.drawOval (i, 80, 10, 10);
            }
        }
    }
    
    /**
     * Invoked when a key has been pressed. The if structure checks if the left or right arrow key has been pressed.
     * The inner if structure checks if the enter key has been pressed.
     * @param e KeyEvent reference: Stores info about the key press.
     * @param keyCode int: A number representing the key code.
     */ 
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT){
            pageNumber--;
        } else {
            if (keyCode == KeyEvent.VK_RIGHT) {
                pageNumber++;
            }
        }
        repaint();
    }
    
    /** 
     * Invoked when a key has been released. 
     * @param e KeyEvent reference: Stores info about the key released.
     */ 
    public void keyReleased(KeyEvent e) {
    }
    
    /** 
     * Invoked when a key has been typed. 
     * @param e KeyEvent reference: Stores info about the key press.
     */
    public void keyTyped(KeyEvent e) {
    }
    
    /** 
     * Invoked when the return button is pressed. The if structure is used to determine the action command
     * of the specified action event. The try-catch is used to catch an IOException that may be thrown when reading
     * the chm help file.
     * @param ae ActionEvent reference: Contains information about the action event that has been fired.
     * @throws IOException if there is any error while exeuting the CHM help file.
     */
    public void actionPerformed (ActionEvent ae){
        if (ae.getActionCommand ().equals ("leftArrow")){
            keyPressed (new KeyEvent (this, 0, 0, 0, KeyEvent.VK_LEFT, ' '));
        } else if (ae.getActionCommand ().equals ("rightArrow")){
            keyPressed (new KeyEvent (this, 0, 0, 0, KeyEvent.VK_RIGHT, ' '));
        } else if (ae.getActionCommand ().equals ("chemistryHelp")){
            try {
                Runtime.getRuntime().exec("HH.EXE ./help/Compounded.chm::/chemicalequations.htm");
            } catch (IOException e) {
            }
        } else {
            ((CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (CompoundedFrame.MENU_PANEL);
        }
        requestFocusInWindow ();
    }
}