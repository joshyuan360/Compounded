package window; //Must fix huge bug with playlist volume adjusting, and removal of music.
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import highscores.*;
import javax.swing.border.EmptyBorder;
import utilities.JTextFieldLimit;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import utilities.MusicPlayer;
/**
 * This class contains the panel that is displayed when the user wishes to view the game options. It also contains the about developers screen so the viewer can get to know the developers.
 * Time spent: 15 hours
 * @author George Lim
 * @version 2.0 May 16, 2014
 * @version 3.0 May 30, 2014 - No changes made.
 * @version 4.0 June 6, 2014 - No changes made.
 * @version 5.0 June 12, 2014 - No changes made.
 */ 
public class Options extends JPanel implements ActionListener
{
    /** True if Joshua's bio is being displayed.*/
    private boolean displayJoshua;
    /** True if George's bio is being displayed. */
    private boolean displayGeorge;
    /** True if the aboutDevelopers menu is being displayed. */
    private boolean aboutMenu;
    /** Contains the font that is used in this panel. */
    private Font font;
    /** The text field where the user enters their new username, should they want to change it.*/
    private JTextField userName;
    /** The button the user clicks to save the new user name that they have entered. */
    private JButton signed;
    /** Displays "Enter New Username" if the text field is empty. */
    private JLabel placeHolder;
    
    
    /** 
     * Constructor that sets the visibility of this panel to false and creates a button to allow the user to go back to the main menu. 
     * The try-catch is used to catch a FontFormatException and IOException.
     * @param e FontFormatException and IOException reference: Contains information about the exception, if thrown.
     * @throws FontFormatException if there is a problem creating the font.
     * @throws IOException if there is a problem reading the file.
     */
    public Options () {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("./data/fonts/arabella.ttf"));
        } catch (FontFormatException e){
        } catch (IOException e) {
        }
        setVisible (false);
        optionsScreen();
    }
    
    /**
     * This method sets the buttons loads the options screen.
     * The first block of code sets the buttons. The second block of code sets the place holder. The third block of
     * code sets the properties for the signed button. The fourth block of code sets properties for the user name text
     * field. The fifth block of code sets properties for the music volume slider. The sixth block of code sets
     * properties for the sound effects volume slider. The rest of the method sets contraints for the layout manager.
     * <p> Anonymous DocumentListener inner class documentation:
     * changedUpdate (): Invoked when an update has been made.
     * removeUpdate (): Invoked when an update has been made to remove text.
     * insertUpdate (): Invoked when an update has been made such that text has been inserted.
     * verfyName (): Sets the visibility of the arrow depending on the length of the text currently in the field.
     * The outer if statement determines the length of the text that has been typed. The inner if statement determines
     * whether the placeholder is currently showing.
     * <p> Anonymous KeyListener inner class documentation:
     * keyTyped (): Invoked when a key has been typed.
     * keyPressed (): Invoked when a key has been pressed. When the user presses the enter key and has typed a new username, the username is automatically saved.
     * keyReleased (): Invoked when a key has been released.
     * <p> First anonymous ChangeListener inner class documentation:
     * stateChanged (): Invoked whenever the top music volume slider has been moved. It adjusts the volume of the
     * currently playing background music.
     * <p> Second anonymous ChangeListener inner class documentation:
     * stateChange (): Invoked whenever the bottom sound effect volume slider has been moved. It adjusts the volume of
     * the sound effects in the game.
     * @param MUSIC final JSlider reference: Allows the user to adjust the volume of the background music by sliding.
     * @param SOUNDEFFECT final JSlider reference: Allows the use to adjust the volume of the sound effects by sliding.
     * @param layout SpringLayout reference: The layout manager of this panel.
     * @param e KeyEvent reference in inner class: Contains information about the key event.
     * @param ce ChangeEvent reference in inner class: Contains information about the change event.
     */ 
    public void optionsScreen() {
        MainMenu.addReturnButton (this);        
        MainMenu.setButtons (new String[] {"aboutDevelopers", "resetScores", "changeUserName", "help"}, 
                             new int[] {670, 190, 160, 740}, 
                             new int[] {230, 515, 608, 515}, 
                             this);
        placeHolder = new JLabel();
        placeHolder.setFont(font = font.deriveFont(24.0f));
        placeHolder.setOpaque(false);
        placeHolder.setForeground (Color.WHITE);
        placeHolder.setText("Enter New Username");
        placeHolder.setPreferredSize(new Dimension(350,35));
        placeHolder.setVisible(false);
        add(placeHolder);
        
        signed = new JButton();
        signed.setActionCommand ("signed");
        signed.setIcon(new ImageIcon ("./data/images/proceed.png"));
        signed.setRolloverIcon(new ImageIcon ("./data/images/proceedActive.png"));
        signed.setBorder (BorderFactory.createEmptyBorder ());
        signed.setContentAreaFilled (false);
        signed.setVisible(false);
        signed.addActionListener (this);
        signed.setVisible(false);
        add(signed);
        
        userName = new JTextField ();
        userName.setOpaque(false);
        userName.setForeground (Color.WHITE);
        userName.setFont(font = font.deriveFont(24.0f));
        userName.setPreferredSize(new Dimension(150,35));
        userName.setBorder (new EmptyBorder(-8, 0, 0, 0));
        userName.setDocument (new JTextFieldLimit (10));
        userName.setVisible(false);
        userName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                verifyName();
            }
            public void removeUpdate(DocumentEvent e) {
                verifyName();
            }
            public void insertUpdate(DocumentEvent e) {
                verifyName();
            }
            public void verifyName() {
                if (userName.getText().length() > 0) {
                    signed.setVisible(true);
                    if (placeHolder.isShowing()) {
                        placeHolder.setVisible(false);
                    }
                } else {
                    signed.setVisible(false);
                    placeHolder.setVisible(true);
                }
            }
        });
        userName.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && signed.isShowing() && userName.getText().length() > 0) {
                    Options.this.changeUserName();
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        });
        add(userName);
        
        final JSlider MUSIC = new JSlider (-40, 6, MusicPlayer.musicDecibels);
        MUSIC.setMajorTickSpacing(7);
        MUSIC.setOpaque(false);
        MUSIC.setPaintTicks(true);
        MUSIC.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                if (!MUSIC.getValueIsAdjusting()) {
                    CompoundedFrame.music.musicDecibels = MUSIC.getValue();
                    CompoundedFrame.music.sync();
                }
            }
        });
        add(MUSIC);
        
        final JSlider SOUNDEFFECT = new JSlider (-40, 6, MusicPlayer.soundDecibels);
        SOUNDEFFECT.setMajorTickSpacing(7);
        SOUNDEFFECT.setOpaque(false);
        SOUNDEFFECT.setPaintTicks(true);
        SOUNDEFFECT.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                MusicPlayer.soundDecibels = SOUNDEFFECT.getValue();
            }
        });
        add(SOUNDEFFECT);
        
        SpringLayout layout = (SpringLayout) getLayout ();
        
        layout.putConstraint (SpringLayout.WEST, placeHolder, 190, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, placeHolder, 591, SpringLayout.NORTH, this);
        
        layout.putConstraint (SpringLayout.WEST, userName, 190, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, userName, 595, SpringLayout.NORTH, this);
        
        layout.putConstraint (SpringLayout.WEST, signed, 450, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, signed, 600, SpringLayout.NORTH, this);
        
        layout.putConstraint (SpringLayout.WEST, MUSIC, 180, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, MUSIC, 220, SpringLayout.NORTH, this);
        
        layout.putConstraint (SpringLayout.WEST, SOUNDEFFECT, 180, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, SOUNDEFFECT, 320, SpringLayout.NORTH, this);
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image. The if statement determines whether a bio,
     * about menu, or options background image should be painted onto the panel.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param background Image reference: The background image of the options screen, which shows up initially when the Options panel is created.
     * @param aboutScreenIntro Image reference: The background image for the about screen.
     * @param logo Image reference: The logo displayed in the about screen.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor (Color.WHITE);
        if (displayJoshua){
            g.drawImage (new ImageIcon ("./data/images/joshuaPage.png").getImage (), 0, 0, this);
            g.setFont(font = font.deriveFont(32.0f));
            g.drawString ("Joshua Yuan is a computer programmer who specializes", 380, 200);
            g.drawString ("in the creation of multi-platform computer games with", 380, 230);
            g.drawString ("an educational focus.", 380, 260);
            g.drawString ("He is the winner of the programming award in William", 380, 320);
            g.drawString ("Lyon Mackenzie C.I., as well as the Junior Canadian", 380, 350);
            g.drawString ("Computing Competition held by the University of", 380, 380);
            g.drawString ("Waterloo.", 380, 410);
        } else if (displayGeorge){
            g.drawImage (new ImageIcon ("./data/images/georgePage.png").getImage (), 0, 0, this);
            g.setFont(font = font.deriveFont(32.0f));
            g.drawString ("George Lim is a computer programmer who specializes", 380, 200);
            g.drawString ("in logic and algorithm creation. He programs with", 380, 230);
            g.drawString ("passion at heart with his other half best friend.", 380, 260);
            g.drawString ("Without her, he would be nothing, for she is his", 380, 320);
            g.drawString ("everything and his universe. His talents stem from", 380, 350);
            g.drawString ("her, who he would gladly do anything for.", 380, 380);
            g.drawString ("Her name is Lior, and she is every beautiful thing", 380, 410);
            g.drawString ("in this universe wrapped into a beautiful being.", 380, 440);
            g.drawString ("\"I love you Lior <3 \"", 380, 470);
        } else if (aboutMenu) {
            Image aboutScreenIntro = new ImageIcon("./data/images/aboutScreenIntro.jpg").getImage();
            g.drawImage (aboutScreenIntro, 0, 0, this);
            Image logo = new ImageIcon("./data/images/icon.png").getImage();
            g.drawImage (logo, 825, 500, this);
        } else {
            Image background = new ImageIcon("./data/images/options.jpg").getImage();
            g.drawImage (background, 0, 0, this);
            g.drawString ("Music:", 80, 235);
            g.drawString ("Sound Effects:", 80, 335);
        }
    }
    
    /**
     * Used to change the user name.
     * The for loop iterates through all of the components in the panel. The if statement checks if the component is a button with the action command of "changeUserName".
     * @param c Component reference: Used in the enhanced for-loop to iterate through all the components.
     */
    public void changeUserName() {
        highscores.HighScores.player = userName.getText();
        userName.setVisible (false);
        userName.setText("");
        for (Component c : getComponents ()){
            if (c instanceof JButton && ((JButton)c).getActionCommand ().equals ("changeUserName")){
                c.setVisible (true);
                break;
            }
        }
        placeHolder.setVisible(false);
    }
    
    /**
     * Switches the panel back to the main menu after pressing the return button. The outer if structure is used to
     * determine the action command of the action event that has been fired. The first inner if structure is used to 
     * remove the buttons with action commands "george" and "joshua". The second inner if structure determines whether
     * the user truly wants to delete all high scores. The next if structure determines if the component in
     * consideration has an action command of "changeUserName". Both the first and second for loop iterates through all of the
     * components in the panel. Both try-catch blocks are used to catch an IOException if an error occurs while
     * reading to the file.
     * @param ae ActionEvent reference that stores the action that the user executed to trigger this method.
     * @param c Component reference: Used in the enhanced for-loop to iterate through all the components.
     * @param out PrintWriter reference: Used to print to the high scores file.
     * @param e IOException reference: Contains info about the IOException, if thrown.
     * @throws IOException if there is an error that occurs while printing the high scores or running the CHM help file.
     */
    public void actionPerformed (ActionEvent ae) {
        if (ae.getActionCommand().equals("aboutDevelopers")) {
            removeAll ();
            MainMenu.setButtons (new String[] {"joshua", "george", "options"}, 
                                 new int[] {570, 140, 460}, 
                                 new int[] {130, 130, 570}, 
                                 this);
            displayJoshua = false;
            displayGeorge = false;
            aboutMenu = true;
        } else if (ae.getActionCommand().equals ("options")) {
            aboutMenu = false;
            for (Component c : getComponents()) {
                if (c instanceof JButton && ((JButton)c).getActionCommand ().equals ("george") || ((JButton)c).getActionCommand ().equals ("joshua")){
                    remove (c);
                }
                remove ((JButton)ae.getSource());
            }
            optionsScreen();
        } else if (ae.getActionCommand ().equals ("joshua")){
            displayJoshua = true;
            removeAll ();
            MainMenu.setButtons (new String [] {"aboutDevelopers"}, new int [] {400}, new int [] {600}, this);
        } else if (ae.getActionCommand ().equals ("george")){
            displayGeorge = true;
            removeAll ();
            MainMenu.setButtons (new String [] {"aboutDevelopers"}, new int [] {400}, new int [] {600}, this);
        } else if (ae.getActionCommand ().equals ("resetScores")){
            if (JOptionPane.showConfirmDialog (this, "Are you sure you want to reset all high scores?\nOld records cannot be recovered.", "Reset High Scores?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon ("./data/images/icon.png")) == JOptionPane.YES_OPTION){
                HighScores.records.clear();
                try {
                    PrintWriter out = new PrintWriter(new FileWriter ("./highscores/scores.pok"));
                    out.println ("This file was created in the Pokilangitis program Compounded.");
                    out.println (0);
                    out.close();
                } catch (IOException e) {
                }
            }
        } else if (ae.getActionCommand ().equals ("changeUserName")) {
            ((JButton)ae.getSource()).setVisible(false);
            userName.setVisible(true);
            placeHolder.setVisible(true);
            userName.requestFocus();
        } else if (ae.getActionCommand() .equals ("signed")) {
            changeUserName();
        } else if (ae.getActionCommand ().equals ("help")){
            try {
                Runtime.getRuntime().exec("HH.EXE ./help/Compounded.chm::/aboutcompoundedbypokilangitis.htm");
            } catch (IOException e) {
            }
        } else {
            ((CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (CompoundedFrame.MENU_PANEL);
        }
        repaint();
        revalidate();
    }
}