package highscores;
import java.awt.*;
import java.io.*;
import java.util.StringTokenizer;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import window.*;
/**
 * A screen in the game that allows the viewing and printing of high scores in the game. This panel is currently not finished and is still for testing.
 * Time spent: George - 2 hours, Joshua - 1 hour
 * @author Joshua Yuan
 * @author George Lim
 * @version 1.0 May 16, 2014
 * @version 2.0 May 23, 2014.
 * @version 3.0 May 30, 2014.
 * @version 4.0 June 6, 2014 - High scores screen design has been implemented. This includes the print button.
 * @version 5.0 June 12, 2014 - Ctrl + P now works for printing.
 */ 
public class HighScores extends JPanel implements ActionListener { 
    /** Stores all the records for the game players. **/
    public static ArrayList<GamePlayer> records = new ArrayList<GamePlayer> ();
    /** The name fo the player currently playing Compounded. */
    public static String player;
    /** The font currently being used to display text to the user. */
    Font font;
    
    /** 
     * Constructor that sets the visibility of this panel to false and creates a button to allow the user to go back 
     * to the main menu.  The if statement prevents the print button from showing up if there are no records to print.
     * The try catch catches a FontFormatException and an IOException. The second block of code sets properties
     * for the print button.
     * <p> Anonymous KeyListener inner class documentation: allows this panel to "listen" for a ctrl + p press.
     * keyTyped (): Invoked when a key has been typed.
     * keyPressed (): Invoked when a key has been pressed. The if statement checks if ctrl + p has been pressed.
     * keyReleased (): Invoked when a key has been released.
     * @param e: KeyEvent reference: Contains information about the key event.
     * @param e: FontFormatException and IOException reference: Catches and contains info about an exception, if thrown.
     * @param printButton JButton reference: The button which allows the user to print.
     * @param layout SpringLayout reference: The layout of this panel.
     * @throws FontFormatException if there is a problem creating the font.
     * @throws IOException if there is a problem reading the file.
     */
    public HighScores (){
        setVisible (false);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("./data/fonts/goodtime.ttf"));
        } catch (FontFormatException e){
        } catch (IOException e) {
        }
        
        JButton printButton = new JButton ();
        printButton.setActionCommand ("print");
        printButton.setIcon (new ImageIcon ("./data/images/print.png"));
        printButton.setRolloverIcon (new ImageIcon ("./data/images/printActive.png"));
        printButton.setBorder (BorderFactory.createEmptyBorder ());
        printButton.setContentAreaFilled (false);
        printButton.addActionListener (this);
        if (records.size() > 0) {
            add (printButton);
        }
        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_P) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) && records.size() > 0) {
                        print();
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        });
        MainMenu.addReturnButton (this);
        SpringLayout layout = (SpringLayout) getLayout ();
        layout.putConstraint (SpringLayout.WEST, printButton, 850, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.NORTH, printButton, 35, SpringLayout.NORTH, this);
    }
    
    /**
     * Initializes <code>records</code> in <code>HighScores</code> with <code>GamePlayer</code> references as stored in 
     * Scores.pok. The try-catch is used to catch a NumberFormatException or IOException. The for loop iterates through
     * all of the records in Scores.pok.
     * @param records int: The number of records contained in the file.
     * @param in BufferedReader reference: Used to read into the file.
     * @param x int: A loop variable used to iterate through all of the records that are stored in the file.
     * @param st StringTokenizer reference: Used to identify the different words in one string.
     * @param e NumberFormatException and IOException reference: Contains infomation about the exception, if thrown.
     * @throws NumberFormatException if there is a problem with corrupt files.
     * @throws IOException if there is a problem reading the file.
     */ 
    public static void loadHighScores() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("./highscores/scores.pok"));
            if (in.readLine().equals ("This file was created in the Pokilangitis program Compounded.")) {
                int records = Integer.parseInt (in.readLine());
                for (int x = 0; x < records; x++) {
                    StringTokenizer st = new StringTokenizer (in.readLine(), ",");
                    highscores.HighScores.records.add(new highscores.GamePlayer (st.nextToken (), st.nextToken (), Integer.parseInt (st.nextToken ())));
                }
            }
        } catch (NumberFormatException e) {
        } catch (IOException e) {
        }
    }
    
    /** 
     * Overrides paintComponent in JPanel to draw a background image. The if structure checks if the records array
     * is empty. The for loop is used to iterate through all of the records in the records array.
     * @param x int: Used in the for loop to iterate through all of the records in the records array.
     * @param g Graphics reference: Allows the method to draw an image to the panel.
     * @param background Image reference: The background image of this panel.
     * @param player GamePlayer reference: The game player whose information is to be displayed.
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Image background = new ImageIcon("./data/images/highScores.jpg").getImage();
        g.drawImage (background, 0, 0, getWidth (), getHeight (), this);
        g.setColor (new Color (230,230,230));
        if (records.size() == 0) {
            g.setFont(font = font.deriveFont(35.0f));
            g.drawString ("No High Scores Available!", 512 - g.getFontMetrics().stringWidth("No High Scores Available!") / 2, 300);
        } else {
            g.setFont(font = font.deriveFont(25.0f));
            for (int x = 0; x < records.size() * 35; x+= 35) {
                GamePlayer player = records.get(x/35);
                g.drawString (x / 35 + 1 + ".", 150, 175 + x);
                g.drawString (player.getName() + " (" + player.getLevel() + ")", 200, 175 + x);
                g.drawString ("" + player.getScore(), 800 - g.getFontMetrics().stringWidth("" + player.getScore()), 175 + x);
            }
        }
    }
    
    /**
     * Switches the panel back to the main menu after pressing the return button.
     * The if structure determines whether the action event was fired by the return button of the print button.
     * @param ae ActionEvent reference that stores the action that the user executed to trigger this method.
     */
    public void actionPerformed (ActionEvent ae){
        if (ae.getActionCommand ().equals ("print")) {
            print();
        }  else {
            ((CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (CompoundedFrame.MENU_PANEL);
        }
    }
    
    /**
     * The for loop in the inner class iterates through all of the GamePlayer objects in the records array.
     * The if statement is used to check if there is a document to be printed. The try-catch is used to catch 
     * Exception PrintException. The print method in the inner class returns PAGE_EXISTS.
     * print inner method: Allows for printing on page.
     * @param printJob PrinterJob reference: the printer job used to print the high scores.
     * @param book Book reference: needed to format the high scores printing file.
     * @param g Graphics in inner class: The graphics objects used for the print method.
     * @param pageFormat PageFormat reference in inner class: The page formatting objects that determines how to format the file.
     * @param page int in inner class: The specified page to be printed.
     * @param g2d Graphics2D reference: Provides methods that are needed to set up the page for printing.
     * @throws PrintException if there is any error during printing.
     */ 
    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(new Printable() {
            public int print(Graphics g, PageFormat pageFormat, int page) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setPaint(Color.black);
                g2d.draw(new Rectangle2D.Double(0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight()));
                g2d.setFont(font = font.deriveFont(45.0f));
                g2d.drawString("Compounded", (int)((pageFormat.getImageableWidth() / 2) - (g2d.getFontMetrics().stringWidth("Compounded") / 2)), 72);
                g2d.setFont(font = font.deriveFont(25.0f));
                g2d.drawString("High Scores", (int)((pageFormat.getImageableWidth() / 2) - (g2d.getFontMetrics().stringWidth("High Scores") / 2)), 96);
                g2d.setFont(font = font.deriveFont(14.0f));
                for (int x = 0; x < records.size() * 35; x+= 35) {
                    GamePlayer player = records.get(x/35);
                    g.drawString (x / 35 + 1 + ".", 25, 175 + x);
                    g.drawString (player.getName() + " (" + player.getLevel() + ")", 75, 175 + x);
                    g.drawString ("" + player.getScore(), 425 - g2d.getFontMetrics().stringWidth("" + player.getScore()), 175 + x);
                }
                return (PAGE_EXISTS);
            }
        }, printJob.defaultPage());
        printJob.setPageable(book);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception PrintException) {
                PrintException.printStackTrace();
            }
        }
    }
}