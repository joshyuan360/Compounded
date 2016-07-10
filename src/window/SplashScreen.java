package window;

import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.FontFormatException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.util.StringTokenizer;
import utilities.JTextFieldLimit;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class contains the splashscreen panel which fades in an image. Time spent: 20 minutes
 * @author StackOverflow (http://stackoverflow.com/questions/20346661/java-fade-in-and-out-of-images)
 * @author Joshua Yuan
 * @author George Lim
 * @version 2.0 May 18, 2014.
 * @version 3.0 May 30, 2014. - No changes made.
 * @version 4.0 June 6, 2014. <p> Changes: In the splashscren, the user is presented with a letter congratulating them for becoming
 * a police officer.
 * @version 5.0 June 12, 2013 - Enter key works.
 * The user must now enter a game name that is less than or equal to 10 characters in length
 * before proceeding to the main menu.
 */ 
public class SplashScreen extends JPanel implements ActionListener {
    
    /** Contains the  backgrond image that this splashscreen will display. */
    private Image background;
    /** Contains the user agreement image that this splashscreen will display. */
    private Image userAgreement;
    /** Contains the timer used to fade the image. */
    private Timer timer;
    /** Stores the current alpha of the image. */
    private float alpha = 0f;
    /** The current value of the Y coordinate of the top left corner of the user agreement image. */
    private int yPos = 349; //349
    /** Initializes 'image' and starts the timer. */
    private JTextField userName;
    
    /**
     * Constructor initializes the instance variables, starts the timer, and loads the high scores file.
     * <p> Anonymous inner Thread class documentation: run() loads the high scores and instruction images while
     * the splashscreen is being animated.
     */ 
    public SplashScreen() {
        background = new ImageIcon("./data/images/SplashScreen.jpg").getImage();
        userAgreement = new ImageIcon ("./data/images/userAgreement.png").getImage();
        timer = new Timer(20, this);
        timer.start();
        new Thread (){
            public void run (){
                highscores.HighScores.loadHighScores();
                Instructions.loadImages ();
            }
        }.start ();
    }
    
    /**
     * Asks the user to enter their game name before they can proceed to the main menu. The try-catch catches a
     * potential FontFormatException and IOException. The first block of code sets the font and the layout. The
     * second block of code sets properties of the JButton. The third block of code sets properties for the userName
     * JTextField.
     * <p>About the inner class: Provides a document listener to the SIGNED JButton.
     * changedUpdate () determines the action to perform if a change has been made.
     * removeUpdate () determines the action to perform if text has been removed.
     * verifyName () determines whether or not the SIGNED button should be visible, depending on the length of the
     * current text. The if structure checks if text has been entered.
     * @param font Font reference: The font used for this method - arabella.ttf.
     * @param layout SpringLayout reference: The layout of this panel.
     * @param SIGNED final JButton reference: When clicked, the program proceeds to the main menu.
     * @param e FontFormatExcetion, IOException references: References the 2 exception classes for error handling. DocumentEvent reference in inner class: Contains information about the document event.
     * @throws FontFormatException if there is an error that occurs while formating the newly created font.
     * @throws IOException if there is an error that occurs while parsing the font file.
     */ 
    private void getUserName() {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("./data/fonts/Arabella.ttf"));
        } catch (FontFormatException e){
        } catch  (IOException e) {
        }
        SpringLayout layout = new SpringLayout ();
        setLayout (layout);
        
        final JButton SIGNED = new JButton ();
        SIGNED.setActionCommand ("signed");
        SIGNED.setIcon(new ImageIcon ("./data/images/proceed.png"));
        SIGNED.setRolloverIcon(new ImageIcon ("./data/images/proceedActive.png"));
        SIGNED.setBorder (BorderFactory.createEmptyBorder ());
        SIGNED.setContentAreaFilled (false);
        SIGNED.setVisible(false);
        SIGNED.setMnemonic(KeyEvent.VK_ENTER);
        getRootPane().setDefaultButton(SIGNED);
        
        userName = new JTextField ();
        userName.setOpaque(false);
        userName.setForeground (Color.BLACK);
        userName.setFont(font = font.deriveFont(24.0f));
        userName.setPreferredSize(new Dimension(150,35));
        userName.setBorder (new EmptyBorder(-8, 0, 0, 0));
        userName.setDocument (new JTextFieldLimit (10));
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
                    SIGNED.setVisible(true);
                } else {
                    SIGNED.setVisible(false);
                }
            }
        });
        
        layout.putConstraint (SpringLayout.EAST, userName, -200, SpringLayout.EAST, this);
        layout.putConstraint (SpringLayout.NORTH, userName, 310, SpringLayout.NORTH, this);
        
        layout.putConstraint (SpringLayout.EAST, SIGNED, -160, SpringLayout.EAST, this);
        layout.putConstraint (SpringLayout.NORTH, SIGNED, 310, SpringLayout.NORTH, this);
        
        add (userName);
        add (SIGNED);
        
        SIGNED.addActionListener (this);
        revalidate ();
        userName.requestFocus();
    }
    /**
     * Overrides paintComponent in JPanel to draw a fading splash screen image.
     * The if structure checks if the value of alpha is less than 1.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param g2d Graphics2D reference: Allows the use of the setComposite method which allows for alpha transparency.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (alpha < 1){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(background, 0, 0, null);
        } else {
            g.drawImage(background, 0, 0, null);
            g.drawImage (userAgreement, 0, yPos, this);
        }
    }
    
    /**
     * Darkens the image by increasing the value of 'alpha'.
     * The outer if structure determines whether the action event has been fired by the Timer or the arrow button.
     * The first inner if structure checks if the userName is equal to pokilang, in which case cheat mode will be 
     * activated. The second inner if structure checks if the value of alpha is greater than 1. The if structure
     * nested within the second inner if structure checks if yPos is equal to 0. The most nested if structure checks if
     * yPos is equal to 349.
     * @param ae ActionEvent reference that stores the action passed in to trigger this method.
     * @param al CompoundedFrame al: Points to the frame which contains this panel.
     */ 
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand()!= null && ae.getActionCommand().equals("signed") && userName.getText().length() > 0) {
            highscores.HighScores.player = userName.getText();
            if (userName.getText ().equals ("debug")){
                game.Game.cheatMode = true;
            }
            CompoundedFrame al = (CompoundedFrame) SwingUtilities.getWindowAncestor (this);
            al.switchToMenu ();
        } else {
            alpha += 0.01f;
            if (alpha >1) {
                alpha = 1;
                if (yPos == 0) {
                    timer.stop();
                    getUserName ();
                } else {
                    if (yPos == 349) {
                        timer.setDelay(8);
                    }
                    yPos --;
                }
            }
            repaint();
        }
    }
}