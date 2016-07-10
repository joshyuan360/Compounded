package game;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import utilities.MusicPlayer;
import highscores.*;

/**
 * This is the Game class that controls the actual game processing for Compounded. Total time spent: George - 150 hours, Joshua - 155 hours.
 * @author George Lim
 * @author Joshua Yuan
 * @version 1.0 May 16, 2014
 * @version 2.0 May 23, 2014
 * <p>Changes: Added the processing of cars, a coin system that grants money to the officer based on successful arrests, random names and descriptions assigned to each driver.
 * <p>Chemical equations now vary based on the level, car collision and detection is now functional, compound confuser algorithm is now implemented to generate false solutions, Police car can now fully chase suspects based on user input.
 * <p>Pause menu now functional and added to allow the game to be paused at any time. Transcript graphics now added to display user information such as arrests, and provide a report for the previous suspect being interrogated.
 * <p>Chemical equations now display correctly with subscripts, charges, and format based on length to fit onto the nav screen. There is now a game timer and death animation when the car explodes or runs out of time.
 * <p>New spawn algorithm incorporated to allow criminals to spawn at set times, and innocent drivers to spawn properly. Added breaking glass graphic as part of damage when car hits below 26% hull.
 * @version 3.0 May 30, 2014
 * @version 4.0 June 6, 2014 - Massive debug session, fixed bugs with display, added cheat mode to testing of successful car arrests.
 * @version 5.0 June 12, 2014 - Massive debug session. Game is now finalized!
 */ 
public class Game extends JPanel implements ActionListener {
    /** True if the last driver caught was the suspect. */
    private boolean lastDriverIsSuspect;
    /** Stores the state of whether or not the cheat mode is activated */
    public static boolean cheatMode;
    /** True if the end screen buttons have been painted onto the panel. */
    private boolean endScreenButtonsPainted;
    /** Stores the Rectangle object representing the location of the police car on the game map. */
    private Rectangle copPos;
    /** Stores the instance of the Police car. */
    private Cop cop;
    /** Stores the custom font used in the game screen. */
    private Font font;
    /** Stores the time in seconds representing the digital time seen during the game screen. */
    private int time = 600;
    /** Stores the equation that is to be solved in the current mission of the current level */
    private ChemicalEquation equation;
    /** The background image of the game. */
    private Image backgroundImage = new ImageIcon ("./data/images/gamebackground.jpg").getImage ();
    /** Stores the image of the road used for game graphics. */
    protected Image road = new ImageIcon ("./data/images/road.png").getImage();
    /** Stores the image of the police car used for game graphics. */
    private Image copCar [] = {
        new ImageIcon ("./data/images/police.png").getImage(),
        new ImageIcon ("./data/images/police2.png").getImage ()
    };
    /** Stores various images that are used in the game. */
    private Image [] images = {
        new ImageIcon ("./data/images/healthBar.png").getImage(),
        new ImageIcon ("./data/images/interior.png").getImage(),
        new ImageIcon ("./data/images/steeringWheel.png").getImage(),
        new ImageIcon ("./data/images/glass.png").getImage(),
        new ImageIcon ("./data/images/explosion.gif").getImage(),
        new ImageIcon ("./data/images/finalReport.png").getImage(),
        new ImageIcon ("./data/images/debugBubble.png").getImage(),
        new ImageIcon ("./data/images/transcript.png").getImage()
    };
    /** Contains the sound effects that are used in this game. */
    private MusicPlayer [] soundEffects = {
        new MusicPlayer ("brake"),
        new MusicPlayer ("start"),
    };
    /** Stores the image of the power button when it is not in the paused state. */
    private ImageIcon powerOn = new ImageIcon ("./data/images/powerOn.png");
    /** Stores the image of the power button in its paused state used for game graphics. */
    private ImageIcon powerPause = new ImageIcon ("./data/images/powerPause.png");
    /** Stores the position of the left wall boundary so that the police car cannot drive off the road. */
    static int roadLeft;
    /** Stores position of the right wall boundary so that the police car cannot drive off the road. */
    static int roadRight;
    /** Stores the yPosition for the first road graphic animating. */
    protected int yPos1 = 0;
    /** Stores the yPosition for the second road graphic animating. */
    protected int yPos2 = -691;
    /** Stores the instance of the Java swing timer used for animating the road. */
    private javax.swing.Timer timer;
    /** Stores the instance of the EquationBook used to load all the equations into the game. */
    private EquationBook book;
    /** Acts as a universal measure for when drivers and criminals will spawn in the map. */
    private int counter = 0;
    /** Stores all the drivers currently spawned in the map. */
    private ArrayList<Driver> driverList = new ArrayList<Driver> ();
    /** Stores the number of milliseconds that go on before the game finally ends after the explosion animation. */
    private int milliseconds = 0;
    /** Grants access to the DriverUtilities generation of names, descriptions for each driver. */
    private utilities.DriverUtilities generator = new utilities.DriverUtilities ();
    /** Stores the number of road lanes in the current map. Changes with difficulty. */
    public static int numLanes;
    /** Stores the amount of money the user has earned through arrests. */
    private int coins;
    /** Stores the state of which the game is paused. */
    private boolean gamePaused;
    /** Stores the increase of money each time the user successfully arrests a criminal. */
    private int increase; //Remove this variable.
    /** Stores the number of interrogations that the police car has attempted during the game. */
    private int numInterrogations;
    /** Stores the number of successful arrests that the user has made. */
    private int numArrests;
    /** Stores the powerButton so that the user can actually pause the game at any time by pressing on this button. */
    private JButton powerButton;
    /** Stores the previous driver suspect that the user has interrogated. */
    private Driver suspect;
    /** Stores the state at which the paused buttons are already shown. Prevents constant creation of new pause buttons every 5 milliseconds in the paintComponent method. */
    private boolean shown;
    /** Stores the times at which the criminal will spawn in the map. */
    private int [] correctTime = new int [3];
    /** Stores the driver that is the actual criminal. */
    private Driver criminal;
    /** Stores the time to add before the next innocent driver spawns in the map */
    private int timeToAdd = (int)(Math.random () * 400) + 600;
    /** Stores the time delay before the game alerts the user that a new mission has been activated. */
    private int displayNewMission;
    /** Stores the state of whether or not the user has won the game. */
    private boolean gameWon;
    /** Stores the number of times the criminal has passed your car and has not been arrested. */
    private int numFails;
    /** Stores whether or not the game has ended. If so, the end screen will be displayed. */
    private boolean gameEnd;
    /** Stores whether or not the high scores has been updated. */
    private boolean updatedHighScores;
    /** Stores the time that is currently being displayed to the player. */
    private String displayTime = "10:00 AM";
    /** Stores the number of minutes before the game will end. */
    private int maxMinutes = 300;
    /** True if game options are displayed. */
    private boolean gameOptions;
    /** True if the player has given up. */
    private boolean giveUp;
    /** Greater than 0 when the number of escapes has recently changed. */
    private int highlightTime;
    
    /**
     * Constructor that sets up the fonts for the game, the sound effects for the game, the boundaries for the road, the lanes, the equations processing, the position of the police car, the criminal drivers, the power button to allow game pause, and the timers required for the animations as well as an in-game timer to stop the user from playing forever.
     * The try-catch catches a FontFormatException or IOException for font importing.
     * There is an anonymous inner class within this method. In this inner class, the run method advanced the time by one minute for every second that passes by in real life.
     * The if statement in the inner class prevents the time from incrementing when the game is state.
     * The inner thread is used to manager the game clock. The run method in the class starts the thread.
     * @param roadLeft int: Stores position of the left wall boundary so that the police car cannot drive off the road.
     * @param roadRight int: Stores position of the right wall boundary so that the police car cannot drive off the road.
     * @param level int: Stores the level that the user is currently playing in.
     * @param layout SpringLayout reference: Stores the layout to be used while adding buttons to the game.
     * @throws FontFormatException if there is an error that occurs while formating the newly created font.
     * @throws IOException if there is an error that occurs while parsing the font file.
     */
    public Game(int roadLeft, int roadRight, int level) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("./data/fonts/pixel.ttf"));
        } catch (FontFormatException e){
        } catch (IOException e) {
        }
        
        Game.roadLeft = roadLeft;
        Game.roadRight = roadRight;
        
        numLanes = level * 2;
        book = new EquationBook(level);
        updateEquation();
        cop = new Cop();
        copPos = new Rectangle(cop.getXCoord(), cop.getYCoord(), 60, 125);
        criminal = new Driver(generator.getRandomName(), generator.getRandomDescription(), (int)(Math.random() * numLanes), equation.getMissingElement(), null);
        initializeCorrectTime ();
        addKeyListener(cop);
        powerButton = new JButton ();
        powerButton.setActionCommand ("pause");
        powerButton.setIcon (powerOn);
        powerButton.setBorder (BorderFactory.createEmptyBorder ());
        powerButton.setContentAreaFilled (false);
        powerButton.addActionListener (this);
        add (powerButton);
        SpringLayout layout = new SpringLayout ();
        layout.putConstraint (SpringLayout.WEST, powerButton, 185, SpringLayout.WEST, this);
        layout.putConstraint (SpringLayout.SOUTH, powerButton, -10, SpringLayout.SOUTH, this);
        setLayout(layout);
        requestFocusInWindow();
        new java.util.Timer().schedule(new TimerTask() {
            public void run() {
                if (!gamePaused && !gameEnd) {
                    time++;
                }
            }
        }, 1000, 1000);
        (timer = new Timer(5, this)).start();
    }
    
    /** 
     * Overrides paintComponent in JPanel to produce the whole game graphics.
     * The first if statement checks if the game has ended. The second if statement checks the new mission text
     * should be displayed. The third if statement checks if the game is currently paused. The first inner if
     * statement checks if the user has won the game. The second inner if statement checks if the option buttons are
     * currently being displayed.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    public void paintComponent (Graphics g) {
        paintCars(g);
        paintCop(g);
        paintHealthBar(g);
        paintCarInterior(g);
        if (!gameEnd) {
            paintTranscript (g);
        } else {
            paintEndScreen(g);
        }
        
        if (displayNewMission > 0){
            g.setFont (font.deriveFont (200));
            g.setColor (Color.WHITE);
            g.drawString ("New Mission!", (1018 - g.getFontMetrics (font).stringWidth ("New Mission!")) / 2, 350);
            displayNewMission--;
            if (gameWon){
                g.drawString ("Congratulations!", (1018 - g.getFontMetrics (font).stringWidth ("Congratulations!")) / 2, 300);
                g.setFont (font = font.deriveFont (50.0f));
                g.drawString ("+20 minutes", (1018 - g.getFontMetrics (font).stringWidth ("+20 minutes")) / 2, 400);
            } else {
                g.drawString ("Mission Failed!", (1018 - g.getFontMetrics (font).stringWidth ("Mission Failed!")) / 2, 300);
                g.setFont (font = font.deriveFont (50.0f));
                g.drawString ("-10 minutes", (1018 - g.getFontMetrics (font).stringWidth ("+20 minutes")) / 2, 400);
            }
        } else {
            gameWon = false;
        }
        if (gamePaused) {
            if (!shown) {
                window.MainMenu.setButtons(new String[] {"1resume", "2gameOptions", "3retire"}, new int [] {340, 340, 340}, new int[] {575, 595, 615}, this);
                shown = true;
            }
            pauseMenu(g);
        }
    }
    
    /** 
     * Draws the background graphic for the game.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    protected void paintBackground(Graphics g) {
        g.drawImage (backgroundImage, 0, 0, this);
    }
    
    /**
     * Draws all the car graphics for the game excluding the police car.
     * First for-loop is used to process all the drivers in the driver list ArrayList.
     * First enhanced for-loop is used to process all the other drivers for each driver when there is collision.
     * First if statement is used to check to see if the driver currently being processed is the one being interrogated by the police car.
     * Second if statement is used to despawn the targetted car if the police car and the suspect are off the screen.
     * Third if statement is used to set the police car collision with the driver if they intersect.
     * The fourth if statement is used to change the position of the driver depending on where the police car has collided with the driver.
     * The fifth if statement is used to check for driver collision with the left level boundary.
     * The sixth if statement is used to check for driver collision with the right level boundary.
     * The seventh if statement is used to check for other driver collision with the current driver being processed.
     * The eighth if statement is used to check for other driver collision with the current driver being processed.
     * The ninth if statement is used to adjust the positions of the other driver that has collided with the current driver being processed.
     * The tenth if statement is used to adjust the height of the other driver to not increase if it is hitting the current driver being processed.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     * @param i Loop integer variable that controls the amount of drivers being processed.
     * @param d Driver class reference: Stores the current driver car being processed.
     * @param other Driver class refernece: Used in the enhanced for-loop to modify the positions of other cars if there is collision.
     */
    private void paintCars(Graphics g) {
        copPos = new Rectangle(cop.getXCoord(), cop.getYCoord(), 60, 125);
        for (int i = 0; i < driverList.size(); i++) {
            Driver d = driverList.get(i);
            if (!cop.isPursuitModeActivated() || cop.isSuspectPulledOver()) {
                if (d.getYCoord() > 500) {
                    if (d.equals (criminal)){
                        numFails++;
                        highlightTime = 300;
                    }
                    driverList.remove(d);
                    i--;
                } else {
                    if (!cop.isPursuitModeActivated() && d.getBounds().intersects(copPos)) {
                        cop.setMoveDirection(0.5);
                        cop.decrementHull(0.05);
                        if (copPos.getCenterX() > d.getXCoord()){
                            d.setXVel(-0.2);
                            d.setYVel(-0.3);
                        } else if (copPos.getCenterX () < d.getXCoord()) {
                            d.setXVel(0.2);
                            d.setYVel(-0.3);
                        } else {
                            if (copPos.getCenterY () > d.getYCoord()) {
                                d.setYVel(0);
                            }
                        }
                    }
                }
                if (!gamePaused && milliseconds == 0)
                    d.move();
                d.setXVel(0);
                d.setYVel(0.6);
            }
            if (d.getXCoord() <= roadLeft)
                d.setXCoord(roadLeft + 10);
            else
                if (d.getXCoord() >= roadRight - d.getCar().getWidth(null))
                d.setXCoord(roadRight - d.getCar().getWidth(null) - 5);
            for (Driver other : driverList) {
                if (d != other && d.getBounds().intersects(other.getBounds())) {                    
                    if (d.getXCoord () > other.getXCoord ()){
                        other.setXVel(-0.2);
                        other.setYVel(-0.3);
                    } else if (d.getXCoord() < other.getXCoord()) {
                        other.setXVel(0.2);
                        other.setYVel(-0.3);
                    } else {
                        if (d.getYCoord() > other.getYCoord()) {
                            other.setYVel(0);
                        }
                    }
                }
            }
            g.drawImage(d.getCar(), d.getXCoord(), d.getYCoord(), this);
        }
    }
    
    /**
     * Draws all the police car graphics.
     * The first if statement is used to draw the car graphic only until moments before the car's explosion.
     * The second if statement is used to draw the explosion animation when the police car's health reaches zero.
     * The inner if statement checks if the timer should stop.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    private void paintCop(Graphics g) {
        if (cop.getHull() > 0 || cop.getHull() <= 0 && milliseconds < 500) {
            g.drawImage(copCar [counter / 100 % 2], copPos.x, copPos.y, copPos.width, copPos.height, this);
        }
        if (cop.getHull() <= 0) {
            g.drawImage(images [4], cop.getXCoord() - 135, cop.getYCoord() - 55, this);
            milliseconds += 5;
            if (milliseconds == 2000) {
                gameEnd = true;
                timer.stop();
            }
        }
    }
    /**
     * Draws all the health bar graphics.
     * The first if statement is used to change the color of the health bar depending on the health of the police car.
     * The second if statement is used to draw the glass shattering graphic when the car hull is below 26%.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     * @param displayTime String reference: Stores the formatted in-game time.
     */
    private void paintHealthBar(Graphics g) {
        g.setFont(font = font.deriveFont(25.0f));
        g.setColor(Color.GRAY);
        g.fillRect(10, 15, 330, 13);
        g.fillRect(10, 28, 165, 10);
        if (cop.getHull() > 50) {
            g.setColor(Color.GREEN);
        } else if (cop.getHull() > 25) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(10, 15, (int)(330 * (cop.getHull() / 100)), 13);
        g.fillRect(10, 28, (int)(165 * (cop.getHull() > 50 ? 1 : (cop.getHull() / 50))), 10);
        g.drawImage (images [0], 0, 0, this);
        g.setColor(Color.WHITE);
        displayTime = (time < 600 + maxMinutes ? (time / 60 < 13 ? time / 60 : time / 60 - 12) + ":" + (time % 60 < 10 ? "0" + (time % 60) : time % 60) + (time / 60 < 12 ? " AM" : " PM") : "Day Over!");
        g.drawString(displayTime, 235 - g.getFontMetrics().stringWidth(displayTime) / 2, 59);
        g.drawString((int)cop.getHull() + "%", 313 - (g.getFontMetrics().stringWidth((int)cop.getHull() + "%") / 2), 59);
        if (cop.getHull() <= 25)
            g.drawImage (images [3], 0, 0, this);
    }
    
    /**
     * Draws the car interior graphics.
     * First for-loop is used to draw the horizonal lines for the grid animation.
     * Second for-loop is used to draw the vertical lines for hte grid animation.
     * First if statement is used to display target information on the dashboard based on if there is a driver in front of the police car.
     * Second if statement is used to display the formatted chemical equation based on the length of the equation. Used to fit the equation on the dashboard screen.
     * The last block of code is used to display the equation to the user.
     * @param trans AffineTransform class reference: Used to rotate the steering wheel as an animation.
     * @param x loop integer variable: Used to draw the grid graphics.
     * @param inFront Driver class reference: Stores the driver in front of the police car.
     * @param displayEquation String class reference: Stores the chemical equation to be displayed to the user.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    private void paintCarInterior(Graphics g){ //OPTIMIZED
        AffineTransform trans = new AffineTransform();
        g.drawImage (images [1], 0, 492, this);
        trans.translate (10, 513);
        trans.rotate (Math.toRadians (cop.getTilt ()), 90, 90);
        ((Graphics2D)g).drawImage (images [2], trans, this);
        
        g.setFont (font.deriveFont (30.0f));
        g.drawString ("Driver Information", 340, 555);
        g.drawString ("Criminal Description", 340, 620);
        
        for (int x = 593; x <= 653; x += 10){
            g.fillRect(x, 535, 1, 120);
        }
        for (int x = 535; x <= 655; x+=10){
            g.fillRect(593, x, 60, 1);
        }
        
        g.setFont(font.deriveFont(23.0f));
        
        Driver inFront = getDriverInFront();
        if (inFront == null) {
            g.drawString ("Name: ---", 340, 575);
            g.drawString ("License Plate: ---", 340, 590);
            g.setFont (font = font.deriveFont(100.0f));
            g.drawString ("?", 605, 615);
        } else {
            g.drawString ("Name: " + inFront.getName (), 340, 575);
            g.drawString ("License Plate: ", 340, 590);
            g.drawString (formatEquation (inFront.getElement()), 455, 590);
            g.setFont (font = font.deriveFont (100.0f));
            g.drawString ("?", 605, 615);
            g.drawImage (inFront.getCar (), 593, 595 - inFront.getCar ().getHeight (null) / 2, this);
        }
        
        String displayEquation = (inFront == null ? equation.getIncompleteEquation() : equation.getIncompleteEquation().replace ("?", inFront.getElement())); //Work in progress: formatting long strings.
        if (g.getFontMetrics (font).stringWidth(displayEquation) < 1350){
            g.drawString (formatEquation(displayEquation), 340, 640);
        } else if (g.getFontMetrics(font).stringWidth(displayEquation.substring(0, displayEquation.indexOf('>') + 1)) < 1350){
            g.drawString (formatEquation(displayEquation.substring(0, displayEquation.indexOf('>') + 1)), 340, 640);
            g.drawString (formatEquation(displayEquation.substring(displayEquation.indexOf('>') + 2)), 340, 660);
        } else {
            g.drawString (formatEquation(displayEquation.substring(0, displayEquation.indexOf('>') - 3)), 340, 640);
            g.drawString (formatEquation(displayEquation.substring(displayEquation.indexOf('>') - 2)), 340, 660);
        }
    }
    /**
     * Draws the transcript graphics.
     * The first if statement is used to display information about the interrogation report after a car has been 
     * interrogated. It is used to error trap for no previous driver being processed. The nested if statement
     * displays the criminal act only if the last driver was the suspect. The second if statement determines if the
     * number of suspect escapes should be highlighted.
     * The last if statement displayed debugging information if cheat mode is on.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    private void paintTranscript (Graphics g) {
        g.drawImage (images[7], 795, 443, this);
        g.setFont (font.deriveFont (25.0f));
        g.setColor (new Color (41, 41, 41));
        g.drawString ("Officer " + HighScores.player, 890 - g.getFontMetrics().stringWidth ("Officer " + HighScores.player) / 2, 465);
        
        g.setFont (font.deriveFont(20.0f));
        g.drawString ("Activity Report", 845, 480);
        g.drawString ("Total Interrogations: " + numInterrogations, 807, 510);
        g.drawString ("Total Arrests: " + numArrests, 807, 525);
        g.drawString ("Cash Earned: $" + coins, 807, 540);
        g.drawString ("Interrogation Report", 807, 575);
        
        int yCoord;
        if (suspect == null) {
            yCoord = 650;
            g.drawString ("Name: ---", 812, 590);
            g.drawString ("License Plate: ---", 812, 605);
            g.drawString ("Status: ---", 812, 620);
            g.drawString ("Suspect escape count: " + numFails, 807, 650);
        } else {
            g.drawString ("Name: " + suspect.getName(), 812, 590);
            g.drawString ("License Plate:", 812, 605);
            g.drawString (formatEquation(suspect.getElement()), 910, 605);
            g.drawString ("Status: " + (lastDriverIsSuspect ? "Guilty" : "Not Guilty"), 812, 620);
            if (lastDriverIsSuspect) {
                g.drawString ("Act: " + suspect.getDescription (), 812, 635);
                yCoord = 670;
            } else {
                yCoord = 650;
            }
            g.drawString ("Suspect escape count: " + numFails, 807, yCoord);
        }
        if (highlightTime > 0 && highlightTime <= 300 && numFails != 0){
            g.setColor (Color.BLUE);
            g.drawRect (802, yCoord - 15, 170, 20);
            highlightTime--;
        }
        if (cheatMode){
            g.drawImage (images[6], 784, 0, this);
            g.setColor (Color.BLACK);
            g.setFont (font.deriveFont (25.0f));
            g.drawString ("Debug Screen", 800, 20);
            g.setFont (font.deriveFont (20.0f));
            g.drawString ("Counter: " + counter, 800, 40);
            g.drawString ("Next Wrong Car: " + timeToAdd, 800, 55);
            g.drawString ("Right Cars: " + correctTime [0] + ", " + correctTime [1] + ", " + correctTime [2], 800, 70);
            g.drawString ("Answer: " + criminal.getElement (), 800, 85);
        }
    }
    /**
     * Draws the pause menu graphics. The two for loops are used to draw the grid. The if statement is used
     * to determine whether or not game options should be displayed.
     * @param x loop integer variable: Used to draw the grid graphic.
     * @param g Graphics reference: Allows the method to access the graphics of the panel.
     */
    private void pauseMenu(Graphics g) {
        g.setColor (Color.BLACK);
        g.fillRect (328, 528, 360, 138);
        g.setFont (font.deriveFont (30.0f));
        g.setColor (Color.WHITE);
        g.drawString ("Compounded (Paused)", 340, 555);
        
        for (int x = 593; x <= 653; x += 10){
            g.fillRect (x, 535, 1, 120);
        }
        for (int x = 535; x <= 655; x += 10){
            g.fillRect (593, x, 60, 1);
        }
        g.drawImage(copCar [counter / 100 % 2], 593, 533, this);
        if (gameOptions) {
            g.setFont (font.deriveFont (20.0f));
            g.drawString ("Music Volume", 340, 595);
            g.drawString ("Sound Effects Volume", 340, 635);
        }
    }
    
    /**
     * The method paints an end screen to the player, informing them of their score. The first if statement
     * checks if the end screen buttons have not yet been painted. The inner if structure checks if the Next Level
     * button should be available to the user.
     * Third if statement checks if at least one hour has elapsed. Second if statement checks if at least one hour has elasped.
     * Fourth if statement checks if 0 or 1 minute has elapsed. Fifth if statement checks if at least 2 minutes have passed by.
     * The last if statement checks if the high score should be updated.
     * @param g Graphics reference: The graphics object that will be painted to the JPanel.
     * @param playerName String reference: The name of the current game player.
     * @param hours String reference: The number of hours that will be displayed on the end screen.
     * @param minutes String reference: The number of minutes that will be displayed on the end screen.
     * @param numHours int: The number of hours that the user has been playing (in game time).
     * @param numMinutes int: The number of minutes that the user has been playing (in game time).
     * @param score int: The player's final score.
     */ 
    private void paintEndScreen(Graphics g) { //Bugs: you can still press pause button.
        powerButton.removeActionListener (this);
        int score = (int)(coins - ((1 - cop.getHull () / 100.0) * 3000));
        if (!endScreenButtonsPainted){
            if (numLanes <= 4 && score > 0){
                window.MainMenu.setButtons (new String [] {"nextLevel", "returnToMenu"},
                                            new int [] {405, 360},
                                            new int [] {450, 500},
                                            this);
            } else {
                window.MainMenu.setButtons (new String [] {"returnToMenu"}, new int [] {360}, new int [] {500}, this);
            }
            endScreenButtonsPainted = true;
        }
        g.drawImage (images[5], 250, 5, this);
        g.setColor (new Color (41, 41, 41));
        g.setFont (font.deriveFont (45.0f));
        String playerName = HighScores.player;
        g.drawString ("Officer " + playerName, 512 - g.getFontMetrics().stringWidth ("Officer " + playerName) / 2, 50);
        g.setFont (font.deriveFont(30.0f));
        g.drawString ("Activity Report", 512 - g.getFontMetrics().stringWidth ("Activity Report") / 2, 75);
        g.drawString ("Total Interrogations: " + numInterrogations, 280, 130);
        g.drawString ("Total Arrests: " + numArrests, 280, 160);
        g.drawString ("Cash Earned: $" + coins, 280, 190);
        g.drawString ("Car Health: " + (int)cop.getHull() + "%", 280, 220);
        String hours = "";
        String minutes = "";
        if ((time - 600) / 60 > 0){
            hours = ((time - 600) / 60) + " hour";
        }
        if ((time - 600) / 60 > 1){
            hours += "s";
        }
        if ((time - 600) % 60 == 0 && hours.equals ("")){
            minutes = "0 minutes";
        } else if ((time - 600) % 60 > 0){
            minutes = (time % 60) + " minute";
        }
        if ((time - 600) % 60 > 1){
            minutes += "s";
        }
        g.drawString ("Time Elapsed: " + hours + " " + minutes, 280, 250);
        g.setFont (font.deriveFont(75.0f));
        g.drawString ("Final Score: " + (giveUp ? "N/A" : score), 290, 600);
        if (!updatedHighScores && !giveUp && score >= 0) {
            updateScores(score);
        }
        requestFocusInWindow ();
    }
    
    /**
     * Updates the highscores file based on the new score that the player has achieved.
     * The if statement checks how many lanes are currently visible in the game. The next if statement determines
     * if a record needs to be deleted.
     * @param level String reference: The level the user is currently playing.
     * @param score int: The to update in high scores.
     */ 
    private void updateScores (int score) {
        updatedHighScores = true;
        String level;
        if (numLanes == 6) {
            level = "Hard";
        } else if (numLanes == 4) {
            level = "Medium";
        } else {
            level = "Easy";
        }
        HighScores.records.add (new GamePlayer (HighScores.player, level, score));
        sortScores();
        if (HighScores.records.size () > 10){
            HighScores.records.remove(HighScores.records.get(HighScores.records.size() - 1));
        }
        printScores();
    }
    /**
     * Sorts the records array in HighScores in descending order using the insertion sort algorithm.
     * The for loop is used to iterate through all of the records.
     * The while loop continuously moves the temp GamePlayer until it is in the right position.
     * @param position: The index for which the temp GamePlayer should be stored.
     * @param temp: The GamePlayer that is currently being sorted.
     * @param i int: For loop varaibles, used to iterate through all of the high scores.
     */ 
    private void sortScores() {
        GamePlayer temp = null;
        int position = 0;
        for (int i = 1; i < HighScores.records.size(); i++) {
            temp = HighScores.records.get(i);
            position = i;
            while (position > 0 && new Integer(temp.getScore()).compareTo(new Integer (HighScores.records.get(position - 1).getScore())) > 0) {
                HighScores.records.set(position, HighScores.records.get(position - 1));
                position--;
            }
            HighScores.records.set(position, temp);
        }
    }
    /**
     * Thie method records the high scores array into the scores.pok file.
     * The try-catch is used to catch an IOException. The for loop iterates through all of the GamePlayer objects in records.
     * @param out PrintWriter reference: Used to write to the scores.pok file.
     * @param e IOException reference: Contains info about the IOException, if thrown.
     * @throws IOException catches any error that could occur from printing to the scores file.
     */ 
    private void printScores ()
    {
        try {
            PrintWriter out = new PrintWriter(new FileWriter ("./highscores/scores.pok"));
            out.println ("This file was created in the Pokilangitis program Compounded.");
            out.println (HighScores.records.size());
            for (GamePlayer g : HighScores.records) {
                out.println (g.getName() + "," + g.getLevel() + "," + g.getScore());
            }
            out.close();
        } catch (IOException e) {
        }
    }
    /**
     * Accessor method that returns the driver in front of the police car.
     * First if statement is used to check if the driver being processed is 20 pixels horizontally ahead of the police car.
     * Enhanced for-loop is used to process all the drivers that are currently spawn in that map.
     * @param closest Driver class reference: Stores the driver that is closest to the police car.
     * @param d Driver class reference: Stores the driver being processed.
     * @return Driver reference: The driver in front of the cop car.
     */
    private Driver getDriverInFront() {
        Driver closest = null;
        for (Driver d : driverList) {
            if (Math.abs (cop.getXCoord () - d.getXCoord ()) < 20 && d.getYCoord() < copPos.y && (closest == null || d.getYCoord() > closest.getYCoord())) {
                closest = d;
            }
        }
        return closest;
    }
    /** Algorithm that formats any chemical equation to be displayed on the game screen.
      * First for-loop is used to detect for charges and super scripts and add them to the ArrayList.
      * Second for-loop is used to pass through the equation to make changes to the subscripts.
      * Enhanced for-loop is used add superscripts to the charges that were recorded earlier in the algorithm
      * First if statement is used to check for charges after the ^ symbol.
      * Second if statement is used to check for subscripts in the the chemical equation.
      * Third if statement is used to leave multi-digit coefficients as normal characters and not subscripts.
      * @param superscripts ArrayList of Integers reference: Stores all the indices where super scripts are found in the chemical equation.
      * @param x loop integer variable: Stores the index the algorithm is currently processing.
      * @param i loop Integer variable: Stores the Integer being processed in the enhanced for-loop.
      * @param tempEquation String reference: The equation to be formatted.
      * @return AttributedCharacterIterator reference: The formatted version of the specified tempEquation.
      */
    private AttributedCharacterIterator formatEquation(String tempEquation) {
        ArrayList<Integer> superscripts = new ArrayList<Integer>();
        for (int x = 0; x < tempEquation.length(); x++) {
            if (tempEquation.charAt(x) == '^') {
                superscripts.add(new Integer(x));
                tempEquation = tempEquation.replaceFirst("\\^", "");
            }
        }
        tempEquation = tempEquation.replace(".", "\u2022");
        AttributedString as = new AttributedString(tempEquation);
        as.addAttribute(TextAttribute.SIZE, 14);
        for (int x = 1; x < tempEquation.length(); x++) {
            if (tempEquation.substring(x, x + 1).matches("[0-9]") && !tempEquation.substring(x - 1, x).matches("\\s|\u2022")) {
                if (x != 1 && !tempEquation.substring(x - 2, x - 1).matches("\\s|\u2022") || !tempEquation.substring(x - 1, x).matches("[0-9]")) {
                    as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, x, x + 1);
                }
            }
        }
        for (Integer i : superscripts) {
            as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, i, i + (tempEquation.substring(i, i + 1).matches("[0-9]") ? 2 : 1));
        }
        return as.getIterator();
    }
    /**
     * Continuously plays the road animation and moves the police car.
     * The first if structure determines if the game or mission should end. The second if structure determines if a button has been pressed,
     * and if not, whether the suspect should be chased or moved away from the screen.
     * @param ae ActionEvent reference that stores the action that the user executed to trigger this method.
     */
    public void actionPerformed(ActionEvent ae) {
        if (displayTime.equals ("Day Over!")){
            gameEnd = true;
            timer.stop ();
        } else if (numFails == 3){
            endMission (false);
        }
        if (ae.getActionCommand() != null) {
            buttonPressed (ae);
        } else if (cop.isSuspectPulledOver()) {
            moveSuspectAway ();
        } else if (cop.isPursuitModeActivated()) {
            chaseSuspect ();
        } else {
            if (!gameEnd && milliseconds == 0){
                addCar();
                moveRoad();
            }
        }
        
        cop.move();
        repaint();
    }
    /**
     * The outer if structure is used to check the action command of the action event. The first inner if structure checks
     * for the number of lanes in the game. The second inner if structure checks if the action command is pause or
     * done2gameOptions. The inner structure within the one previously mentioned removes jsliders. The remaining if 
     * statements are all used to located the correct component in the panel. The three for loops are all used to 
     * iterate through all of the components that are in the panel.
     * @param MUSIC JSlider reference: The JSlider that allows the user to adjust the volume of the background music.
     * @param SOUNDEFFECT JSlider reference: THe JSlider that allows the user to adjust the volume of the sound effects.
     * @param c Component class reference: Used to cycle through the components in the pause menu and remove all the pause menu buttons.
     * @param ae ActionEvent reference that stores the action that the user executed to trigger this method.
     * @param layout SpringLayout reference: The layout manager currently being used by this panel.
     */
    public void buttonPressed (ActionEvent ae){
        if (ae.getActionCommand ().equals ("returnToMenu")){
            ((window.CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (window.CompoundedFrame.MENU_PANEL);
        } else if (ae.getActionCommand ().equals ("nextLevel")){
            if (numLanes == 2){
                ((window.CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (window.CompoundedFrame.MEDIUM_PANEL);
            } else {
                ((window.CompoundedFrame) SwingUtilities.getWindowAncestor (this)).switchPanel (window.CompoundedFrame.HARD_PANEL);
            }
        } else if (!gameEnd){
            if (ae.getActionCommand().equals("pause") && !gamePaused) {
                timer.stop();
                gamePaused = true;
                shown = false;
                powerButton.setIcon (powerPause);
                powerButton.setPressedIcon (powerOn);
                soundEffects [1].stop ();
            } else if (ae.getActionCommand().equals("done2gameOptions")) {
                gameOptions = false;
                for (Component c : getComponents ()){
                    if (c instanceof JSlider || c.equals((JButton)ae.getSource())){
                        remove (c);
                    } else {
                        if (c instanceof JButton && ((JButton)c).getActionCommand ().equals ("pause") == false){
                            c.setVisible (true);
                        }
                    }
                }
            } else if (ae.getActionCommand().equals("pause") && gamePaused || ae.getActionCommand().equals("1resume")) {
                timer.start();
                gamePaused = false;
                gameOptions = false;
                for (Component c : getComponents ()){
                    if (c instanceof JSlider || c instanceof JButton && ((JButton)c).getActionCommand ().equals ("pause") == false){
                        remove (c);
                    }
                }
                powerButton.setIcon (powerOn);
                powerButton.setPressedIcon (powerPause);
                requestFocusInWindow();
                soundEffects [1].start ();
            } else if (ae.getActionCommand().equals("2gameOptions")) {
                for (Component c : getComponents ()){
                    if (c instanceof JButton && ((JButton)c).getActionCommand ().equals ("pause") == false){
                        c.setVisible (false);
                    }
                }
                final JSlider MUSIC = new JSlider (-40, 6, MusicPlayer.musicDecibels);
                MUSIC.setOpaque(false);
                MUSIC.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent ce) {
                        window.CompoundedFrame.music.musicDecibels = MUSIC.getValue();
                        window.CompoundedFrame.music.sync();
                    }
                });
                add(MUSIC);
                final JSlider SOUNDEFFECT = new JSlider (-40, 6, MusicPlayer.soundDecibels);
                SOUNDEFFECT.setOpaque(false);
                SOUNDEFFECT.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent ce) {
                        MusicPlayer.soundDecibels = SOUNDEFFECT.getValue();
                    }
                });
                add(SOUNDEFFECT);
                JButton done2gameOptions = new JButton(new ImageIcon ("./data/images/optionsReturn.png"));
                done2gameOptions.setActionCommand ("done2gameOptions");
                done2gameOptions.setBorder (BorderFactory.createEmptyBorder ());
                done2gameOptions.setContentAreaFilled (false);
                done2gameOptions.addActionListener(this);
                add (done2gameOptions);
                SpringLayout layout = (SpringLayout)getLayout();
                layout.putConstraint (SpringLayout.WEST, MUSIC, 340, SpringLayout.WEST, this);
                layout.putConstraint (SpringLayout.NORTH, MUSIC, 600, SpringLayout.NORTH, this);
                
                layout.putConstraint (SpringLayout.WEST, SOUNDEFFECT, 340, SpringLayout.WEST, this);
                layout.putConstraint (SpringLayout.NORTH, SOUNDEFFECT, 640, SpringLayout.NORTH, this);
                
                layout.putConstraint (SpringLayout.WEST, done2gameOptions, 340, SpringLayout.WEST, this);
                layout.putConstraint (SpringLayout.NORTH, done2gameOptions, 565, SpringLayout.NORTH, this);
                gameOptions = true;
            } else {
                if (ae.getActionCommand().equals("3retire") && JOptionPane.showConfirmDialog (this, "Are you sure you want to quit?\nAll progress will be lost.", "Retire?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon ("./data/images/icon.png")) == JOptionPane.YES_OPTION){
                    gameEnd = true;
                    gamePaused = false;
                    giveUp = true;
                    for (Component c : getComponents ()){
                        if (c instanceof JButton && ((JButton)c).getActionCommand ().equals ("pause") == false){
                            remove (c);
                        }
                    }
                    powerButton.setIcon (powerOn);
                    powerButton.setPressedIcon (powerPause);
                    requestFocusInWindow();
                }
            }
        }
    }
    /** Determines if the player has lost or won the game.
      * The if statement is used to set up the gameWon boolean based on whether or not the user successfully won the game.
      * @param won boolean: Stores the state of whether or not the user has successfully completed the mission.
      */
    private void endMission (boolean won){
        displayNewMission = 600;
        if (won){
            gameWon = true;
        }
        
        updateEquation ();
        counter = milliseconds = numFails = 0;
        driverList.clear ();
        gamePaused = shown = false;
        initializeCorrectTime ();
        criminal = new Driver(generator.getRandomName(), generator.getRandomDescription(), (int)(Math.random() * numLanes), equation.getMissingElement(), null);
    }
    /**
     * Initiates the police car's act of chasing the suspect in its sight.
     * The first if statement is used to reset the arrest mode to false if the driver it was targeting previously has been deloaded.
     * The second if statement is used to stop the break sound effect after the police car has made contact with the suspect driver.
     * @param d Driver class reference: Stores the driver that is currently ahead of the police car to be interrogated.
     * @param yTarget int: The yCoord of the target.
     */
    private void chaseSuspect (){
        if (getDriverInFront() == null) {
            cop.setArrestModeActivated(false);
        } else {
            Driver d = getDriverInFront();
            cop.setReturnToZero(false);
            
            int yTarget = d.getYCoord () + d.getCar ().getHeight (null);
            cop.moveTowards(d.getXCoord(), yTarget);
            soundEffects [0].start ();
            if (cop.getXCoord () == d.getXCoord () && cop.getYCoord () == yTarget){
                soundEffects [0].stop ();
            }
        }
    }
    
    /**
     * Initiates the pulling over of the suspect when the police car has chased the suspect down.
     * The first if statement checks to see if there is a driver in front of the police car.
     * The inner if structure checks if the suspect is the criminal.
     * @param d Driver class reference: Stores the driver that is currently ahead of the police car to be interrogated.
     */
    private void moveSuspectAway (){
        Driver d = getDriverInFront();
        if (d != null && d.getYCoord () == 500) {
            cop.setArrestModeActivated(false);
            cop.setSuspectPulledOver(false);
            cop.setLocation(479, 350);
            suspect = new Driver (d.getName(), d.getDescription() + " $" + d.getBounty (), d.getLane(), d.getElement(), d.getCar());
            if (suspect.equals (criminal)){
                coins += increase;
                counter = 0;
                numArrests++;
                maxMinutes += 20;
                initializeCorrectTime ();
                timeToAdd = (int)(Math.random () * 400) + 600;
                lastDriverIsSuspect = true;
                endMission (true);
            } else {
                coins -= 200;
                maxMinutes -= 10;
                lastDriverIsSuspect = false;
            }
            increase = 0;
            numInterrogations++;
        } else {
            moveRoad();
            cop.setLocation(d.getXCoord(), d.getYCoord () + d.getCar ().getHeight (null));
            increase = d.getBounty ();
        }
    }
    /**
     * Spawns a new driver in the map.
     * For-loop checks to see if the current counter time matches the criminal appearance time.
     * First if statement is used to verify if the current counter time is the right time for the criminal to spawn in the game.
     * Second if statement is used to spawn in a new driver if it matches the time to add a random innocent driver.
     * @param num int: Stores the time at which the criminals are to spawn in the map.
     */
    private void addCar() {
        counter++;
        for (int num : correctTime){
            if (counter == num){
                driverList.add (new Driver (criminal.getName (), criminal.getDescription () + " $" + criminal.getBounty (), (int)(Math.random () * numLanes), criminal.getElement (), criminal.getCar ()));
                return;
            }
        }
        if (counter == timeToAdd){ //counter 200 == 1 second
            driverList.add (new Driver(generator.getRandomName(), generator.getRandomDescription(), (int)(Math.random() * numLanes), utilities.ChemistryUtilities.getConfusingElement(equation.getMissingElement()), null));
            setNextCarTime ();
        }
    }
    /** Sets up the spawn time for the criminal cars. */
    private void initializeCorrectTime (){
        correctTime [0] = (int)(Math.random () * 6400) + 1600;
        correctTime [1] = (int)(Math.random () * 10400) + 1600 + correctTime [0]; //next correct car comes 8 to 60 secodns after first car.
        correctTime [2] = (int)(Math.random () * 10400) + 1600 + correctTime [1]; //next correct car comes 8 to 60 seconds after second car.
    }
    /**
     * For-loop is used to compare the spawn time of the next innocent car with the spawn time of the criminal cars.
     * The if statement is used to prevent the spawning of an innocent car if it is too close to the spawn time of the criminal.
     * Sets up the time at which the next innocent driver should spawn in the map.
     * @param num int: Stores the time at which the criminals are to spawn in the map.
     */
    private void setNextCarTime (){
        timeToAdd += (int)(Math.random () * 400) + 600;
        for (int num : correctTime){
            if (Math.abs (timeToAdd - num) < 600){
                setNextCarTime ();
                break;
            }
        }
    }
    /** Updates the equation on the on board computer to be solved by the user. */
    private void updateEquation() {
        equation = book.getRandomEquation();        
    }
    /**
     * Updates the position of the road graphic for animations.
     * The first if statement is used reset the position of the first road image if that image has reached the bottom of the panel.
     * The second if statement is used reset the position of the second road image if that image has reached the bottom of the panel.
     */
    private void moveRoad() {
        if (yPos1 == 691) {
            yPos1 = -691;
        } else {
            if (yPos2 == 691) {
                yPos2 = -691;
            }
        }
        yPos1++;
        yPos2++;
    }
}