package game;
import java.awt.*;
import javax.swing.ImageIcon;

/**
 * This class represents a driver on the road in the Compounded game. Total time spent: George - 2 hours, Joshua - 2 hours.
 * @author George Lim.
 * @author Joshua Yuan.
 * @version 1.0 May 16, 2014.
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - Complete Driver class rewrite, it now works as intended.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - June 12, 2013 - Method relocation for better organization.
 */ 
public class Driver
{
    /** The name of this driver. */
    private String name;
    /** The description of this driver. */
    private String description;
    /** The lane of this driver's car. */
    private int lane;
    /** The bounty of this driver. */
    private int bounty;
    /** The element written on the license plate of this driver's car. */
    private String element;
    /** The car of this driver. */
    private Image car;
    /** The rectangle that surrounds this this driver's car. */
    private Rectangle bounds;
    /** The xCoord of the top left corner of this driver's car. */
    private double xCoord;
    /** The yCoord of the top left corner of this driver's car. */
    private double yCoord = -100;
    /** The x-velocity of this driver. */
    private double xVel;
    /** The speed the driver is travelling. */
    private double yVel = 0.6;
    
    /**
     * Constructor that specifies the name, description, lane, element, and car of a driver.
     * If <code>null</code> is passed in as a paramter for Image, a random car will be given to this <code>Driver</code>.
     * The if structure checks if <code>car</code> has a <code>null</code> reference.
     * @param newName String reference: Contains the new name of this <code>Driver</code>.
     * @param newDescription String reference: Contains the new description of this <code>Driver</code>.
     * @param newLane int: Contains the new lane of this <code>Driver</code>.
     * @param newElement String reference: Contains the new element associated with this <code>Driver</code>.
     * @param car Image reference: Contains the new car of this <code>Driver</code>. If the value of <code>car</code> is <code>null</code>, a random car will be assigned to this <code>Driver</code>.
     */ 
    public Driver (String newName, String newDescription, int newLane, String newElement, Image car)
    {
        name = newName;
        description = newDescription.substring (0, newDescription.indexOf (" $"));
        lane = newLane;
        bounty = Integer.parseInt (newDescription.substring (newDescription.indexOf ("$") + 1));
        if (car == null){
            this.car = getRandomCar ();
        } else {
            this.car = car;
        }
        xCoord = Game.roadLeft + lane * 125 + 30;
        bounds = new Rectangle (getXCoord (), getYCoord (), this.car.getWidth (null), this.car.getHeight (null));
        element = newElement;
    }
    
    /**
     * Returns the name of this <code>Driver</code>.
     * @return String reference: the name of this <code>Driver</code>.
     */ 
    public String getName (){
        return name;
    }
    
    /**
     * Returns the description of this <code>Driver</code>.
     * @return String reference: the description of this <code>Driver</code>.
     */ 
    public String getDescription (){
        return description;
    }
    
    /**
     * Returns the lane of this <code>Driver</code>.
     * @return int: the lane of this <code>Driver</code>.
     */ 
    public int getLane (){
        return lane;
    }
    
    /**
     * Returns the bounty of this <code>Driver</code>.
     * @return int: the bounty of this <code>Driver</code>.
     */
    public int getBounty (){
        return bounty;
    }
    
    /**
     * Returns the car of this <code>Driver</code>.
     * @return Image reference: the car of this <code>Driver</code>.
     */ 
    public Image getCar (){
        return car;
    }
    
    /**
     * Returns the X coordinate of the top left corner of this <code>Driver</code>.
     * @return int: the X coordinate of the top left corner of this <code>Driver</code>.
     */
    public int getXCoord (){
        return (int)xCoord;
    }
    
    /**
     * Sets the X coordinate of the top left corner of this <code>Driver</code>.
     * @param newXCoord the new X coordinate of the top left corner of this <code>Driver</code>.
     */
    public void setXCoord (double newXCoord){
        xCoord = newXCoord;
        bounds.x = (int)newXCoord;
    }
    
    /**
     * Sets the Y coordinate of the top left corner of this <code>Driver</code>.
     * @param newXCoord the new Ycoordinate of the top left corner of this <code>Driver</code>.
     */
    public void setYCoord (int newYCoord){
        yCoord = newYCoord;
        bounds.y = newYCoord;
    }
    
    
    /**
     * Returns the Y coordinate of the top left corner of this <code>Driver</code>.
     * @return int: the Y coordinate of the top left corner of this <code>Driver</code>.
     */
    public int getYCoord (){
        return (int) yCoord;
    }
    
    
    /**
     * Returns a rectangle that surrounds this <code>Driver</code>.
     * @return Rectangle reference: a rectangle that surrounds this <code>Driver</code>.
     */
    public Rectangle getBounds (){
        return bounds;
    }
    
    /**
     * Assigns a random car to this <code>Driver</code>.
     * @return Image reference: one of the possible cars that may be assigned to a <code>Driver</code>.
     */ 
    private Image getRandomCar (){
        return new ImageIcon ("./Data/Images/CarType" + ((int) (Math.random () * 5) + 1) + ".png").getImage ();
    }
    
    /**
     * Returns the element on the license plate of this <code>Driver</code>.
     * @return String reference: the element on the license plate of this <code>Driver</code>.
     */ 
    public String getElement (){
        return element;
    }
    
    /**
     * Sets the X velocity of this <code>Driver</code>.
     * @param vel the new X velocity this <code>Driver</code>.
     */
    public void setXVel (double vel){
        xVel = vel;
    }
    
    /**
     * Sets the Y velocity of this <code>Driver</code>.
     * @param vel the new Y velocity this <code>Driver</code>.
     */
    public void setYVel (double vel){
        yVel = vel;
    }
    
    /**
     * Moves this <code>Driver</code> based on the current speed.
     */ 
    public void move (){
        yCoord += yVel;
        xCoord += xVel;
        bounds.x = getXCoord ();
        bounds.y = getYCoord ();
    }
    
    /**
     * Checks for equality between different <code>Driver</code> objects.
     * Two drivers are considered equal if they have the sane name, description, element, and car.
     * @return boolean: <code>true</code> if the drivers are equal, <code>false</code> otherwise.
     */ 
    public boolean equals (Driver d){
        return name.equals (d.getName ()) && description.equals (d.getDescription ()) && element.equals (d.getElement ()) && car.equals (d.getCar ());
    }
}