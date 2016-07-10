package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import highscores.GamePlayer;

/**
 * This class displays the main menu of the Compounded game.
 * Time spent: 5 hours
 * @author Joshua Yuan.
 * @version 2.0 May 23, 2014.
 * @version 3.0 May 30, 2014 - No changes made.
 * @version 4.0 June 6, 2014 - No changes made.
 * @version 5.0 June 12, 2014 - Internal documentation completed.
 */ 
public class MainMenu extends JPanel implements ActionListener
{
    /** True if level selection is displayed. */
    private boolean levelSelectDisplayed;
    /** True if the control key is currently depressed. */
    private boolean controlPressed;
    
    /** Displays the main menu. The constructor sets the buttons for the user to click. */
    public MainMenu () {
        setButtons (new String[] {"LevelSelect", "Instructions", "HighScores", "Options", "Credits", "Exit"}, 
                    new int[] {110, 105, 110, 135, 135, 160}, 
                    new int[] {180, 230, 280, 330, 380, 430}, 
                    this);
        setVisible (false);
    }    
    
    /**
     * Adds an image to the background of this panel.
     * @param g Graphics reference: Allows the method to draw on the panel.
     * @param menu Stores the image of the menu screen background used for menu graphics.
     */ 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image menu = new ImageIcon("./data/images/menuScreen" + (highscores.HighScores.player.equals("Lior") ? "Secret" : "") + ".jpg").getImage();
        g.drawImage (menu, 0, 0, getWidth (), getHeight (), this);
    }
    
    /**
     * Displays the buttons on the menu. It is also used in Game to display options when the game is paused. The if statement checks if
     * the panel is already managed by a SpringLayout layout manager. The second if statement checks that the 
     * specified panel is not an instance of Instructions. The nested if structure checks if the action command
     * of the menuButton is resetScores.
     * The for loop iterates through all of the buttons in 
     * menuButtons, setting the same properties for all.
     * @param layout SpringLayout reference: Contains the layout used by this panel.
     * @param menuButtons JButton array: Contains the buttons used in this menu.
     * @param i int: Used to iterate through all of the buttons in the for loop.
     */ 
    public static void setButtons (String[] actionCommands, int[] leftMargin, int[] topMargin, JPanel panel){
        SpringLayout layout = new SpringLayout ();
        if (panel.getLayout () instanceof SpringLayout){
            layout = (SpringLayout)panel.getLayout ();
        }
        
        JButton [] menuButtons = new JButton [actionCommands.length];   
        
        for (int i = 0; i < menuButtons.length; i++){
            menuButtons [i] = new JButton ();
            menuButtons [i].setActionCommand (actionCommands [i]);
            menuButtons [i].setIcon (new ImageIcon("./data/images/" +  menuButtons [i] .getActionCommand () + ".png"));
            menuButtons [i].setRolloverIcon (new ImageIcon("./data/images/" +  menuButtons [i] .getActionCommand () + "Active.png"));
            menuButtons [i].setBorder (BorderFactory.createEmptyBorder ());
            menuButtons [i].setContentAreaFilled (false);
            menuButtons [i].addActionListener ((ActionListener)panel);
            if (!(panel instanceof Instructions)){
                if (menuButtons [i].getActionCommand ().equals ("resetScores")){
                    menuButtons [i].setMnemonic (KeyEvent.VK_S);
                } else {
                    menuButtons [i].setMnemonic (KeyEvent.getExtendedKeyCodeForChar (actionCommands [i].charAt (0)));
                }
            }
            layout.putConstraint (SpringLayout.WEST,  menuButtons [i] , leftMargin [i], SpringLayout.WEST, panel);
            layout.putConstraint (SpringLayout.NORTH,  menuButtons [i], topMargin[i], SpringLayout.NORTH, panel); 
            
            panel.add (menuButtons [i]);
        }
        panel.setLayout (layout);
        panel.revalidate ();
    }
    
    /**
     * This method is used in other classes to add a button that allows the user to return to the main menu.
     * The first if statement checks if the panel is already managed by a SpringLayout layout manager.
     * The second if statement checks which specific panel the panel is being added onto. The constraints that are used
     * by the layout manager differ depending on which panel the return button is added to. The first block of code
     * set properties for the returnButton. The second block of code initializes the layout variables. The third block
     * of code positions the return button on the specified panel.
     * @param panel JPanel reference: The button will be added to the specified panel.
     * @param returnButton JButton reference: The button to be added to the specified panel.
     * @param layout SpringLayout reference: The layout to be set to the specified panel.
     */ 
    public static void addReturnButton (JPanel panel){
        JButton returnButton = new JButton (new ImageIcon("./data/images/Return.png"));
        returnButton.setActionCommand ("return");
        returnButton.setContentAreaFilled (false);
        returnButton.setBorder (BorderFactory.createEmptyBorder ());
        returnButton.setBorderPainted (false);
        returnButton.addActionListener ((ActionListener) panel);
        returnButton.setRolloverIcon (new ImageIcon ("./data/images/ReturnActive2.png"));
        returnButton.setMnemonic (KeyEvent.VK_R);
        
        SpringLayout layout = new SpringLayout ();
        if (panel.getLayout () instanceof SpringLayout){
            layout = (SpringLayout)panel.getLayout ();
        }
        
        if (panel instanceof Instructions){
            layout.putConstraint (SpringLayout.EAST, returnButton, -32, SpringLayout.EAST, panel);
            layout.putConstraint (SpringLayout.NORTH, returnButton, 30, SpringLayout.NORTH, panel);
        } else if (panel instanceof Options) {
            layout.putConstraint (SpringLayout.HORIZONTAL_CENTER, returnButton, 0, SpringLayout.HORIZONTAL_CENTER, panel);
            layout.putConstraint (SpringLayout.SOUTH, returnButton, -10, SpringLayout.SOUTH, panel);
        } else if (panel instanceof MainMenu) {
            layout.putConstraint (SpringLayout.WEST, returnButton, 120, SpringLayout.WEST, panel);
            layout.putConstraint (SpringLayout.NORTH, returnButton, 450, SpringLayout.NORTH, panel);
        } else {
            layout.putConstraint (SpringLayout.HORIZONTAL_CENTER, returnButton, 0, SpringLayout.HORIZONTAL_CENTER, panel);
            layout.putConstraint (SpringLayout.SOUTH, returnButton, -50, SpringLayout.SOUTH, panel);
        }
        
        panel.setLayout (layout);
        panel.add (returnButton); 
        panel.validate ();
        
    }
    
    /** 
     * Invoked when the user presses Level Select on the main menu. This method sets new buttons that allow the user
     * to select a level and begin playing.
     */ 
    private void levelSelect (){
        levelSelectDisplayed = true;
        removeAll ();
        addReturnButton (this);
        setButtons (new String[] {"EasyLevel", "MediumLevel", "HardLevel"}, 
                    new int[] {150, 140, 150}, 
                    new int[] {220, 290, 360}, 
                    this);
        revalidate ();
        repaint ();
    }
    
    /**
     * Invoked when one of the buttons in this menu is pressed.
     * @param ae ActionEvent reference: Contains info after the action event.
     * @param myFrame CompoundedFrame reference: References the CompoundedFrame which contains this panel.
     * The if structure is used to determine the action command. The nested if structure checks if the action 
     * command equals "return".
     */ 
    public void actionPerformed (ActionEvent ae){
        CompoundedFrame myFrame = (CompoundedFrame) SwingUtilities.getWindowAncestor (this);
        
        if (ae.getActionCommand().equals("LevelSelect")){
            levelSelect ();
        } else if (ae.getActionCommand ().equals ("Instructions")){
            myFrame.switchPanel (CompoundedFrame.INSTRUCTIONS_PANEL);
        } else if (ae.getActionCommand ().equals ("HighScores")){
            myFrame.switchPanel (CompoundedFrame.HIGHSCORES_PANEL);
        } else if (ae.getActionCommand ().equals ("Options")){
            myFrame.switchPanel (CompoundedFrame.OPTIONS_PANEL);
        } else if (ae.getActionCommand ().equals ("Credits")){
            myFrame.switchPanel (CompoundedFrame.CREDITS_PANEL);
        } else if (ae.getActionCommand ().equals ("Exit")){ //REMOVE HANGING ELSE IF AFTER GAME IS DONE
            myFrame.switchPanel (CompoundedFrame.EXIT_PANEL);
        } else if (ae.getActionCommand ().equals ("EasyLevel")){
            myFrame.switchPanel (CompoundedFrame.EASY_PANEL);
        } else if (ae.getActionCommand ().equals ("MediumLevel")){
            myFrame.switchPanel (CompoundedFrame.MEDIUM_PANEL);
        } else if (ae.getActionCommand ().equals ("HardLevel")){
            myFrame.switchPanel (CompoundedFrame.HARD_PANEL);
        } else {
            if (ae.getActionCommand ().equals ("return")){
                levelSelectDisplayed = false;
                removeAll ();
                setButtons (new String[] {"LevelSelect", "Instructions", "HighScores", "Options", "Credits", "Exit"}, 
                            new int[] {110, 105, 110, 135, 135, 160}, 
                            new int[] {180, 230, 280, 330, 380, 430}, 
                            this);
                repaint ();
                requestFocusInWindow ();
            }
        }
    }
   
}

