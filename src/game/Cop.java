package game;

import java.awt.event.*;

/**
 * This is the Cop class that allows the player to control the steering of the police car and capture the correct suspect. Total time spent: George - 8 hours, Joshua - 8 hours.
 * @author George Lim
 * @author Joshua Yuan
 * @version 1.0 May 16, 2014.
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - Complete Cop class rewrite which now includes arrest mode.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - June 12, 2013 - Method relocation for better organization.
 */ 
public class Cop extends KeyAdapter {
    /** Stores the X position of the police car. */
    private int xCoord = 479;
    /** Stores the Y position of the police car. */
    private int yCoord = 350;
    /** Stores the direction at which the car should move upon user input. */
    private double moveDirection;
    /** Stores the direction of tilt the steering wheel should turn based on user input. */
    private double tiltChange;
    /** Stores the degrees the steering wheel should be at for tilting based on user input. */
    private double tilt;
    /** Stores the hull/health of the car. */
    private double hull = 100;
    /** Stores the state at which the steering should return back to its default position. */
    private boolean returnToZero;
    /** Stores the state at which the car is undergoing an arrest. */
    private boolean pursuitModeActivated;
    /** Stores the state at which a suspect is being pulled over. */
    private boolean suspectPulledOver;
    /**
     * Accessor method that gets the police car's current x position.
     * @return int: The police car's current x position.
     */
    public int getXCoord (){
        return xCoord;
    }
    /**
     * Accessor method that gets the police car's current y position.
     * @return int: The police car's current y position.
     */
    public int getYCoord (){
        return yCoord;
    }
    /**
     * Mutator method that sets the new position x and y for the Cop car.
     * @param x int: Stores the x position of the police car.
     * @param y int: Stores the y position of the police car.
     */
    public void setLocation(int x, int y) {
        xCoord = x;
        yCoord = y;
    }
    /**
     * Mutator method that sets the move direction of the police car upon user input with arrow keys.
     * @param direction double: The direction that the car is supposed to move.
     */
    public void setMoveDirection(double direction) {
        moveDirection = direction;
    }
    /**
     * Accessor method that gets the current health of the police car.
     * @return double: The current health of the police car.
     */
    public double getHull (){
        return hull;
    }
    /**
     * Mutator method that changes the durability of the police car upon taking damage.
     * @param amount double: The amount of health to be decreased.
     */
    public void decrementHull (double amount){
        hull = (hull < 0 ? 0 : hull - amount);
    }
    /**
     * Accessor method that gets the current tilt degrees the steering wheel should be as the car is moving.
     * @return double: The tilt degrees of the steering wheel.
     */
    public double getTilt (){
        return tilt;
    }
    /**
     * Accessor method that returns the state at which the Cop car is undergoing an arrest.
     * @return boolean: The state at which the Cop car is undergoing an arrest.
     */
    public boolean isPursuitModeActivated () {
        return pursuitModeActivated;
    }
    /**
     * Accessor method that returns the state at which a suspect is being pulled over.
     * @return boolean: The state at which a suspect is being pulled over.
     */
    public boolean isSuspectPulledOver (){
        return suspectPulledOver;
    }
    /**
     * Mutator method that sets the state at which the steering should return to its original position.
     * @param toZero boolean: The state at which the steering should return to its original position.
     */
    public void setReturnToZero (boolean toZero){
        returnToZero = toZero;
    }
    /**
     * Mutator method that sets the state at which the Cop car is undergoing an arrest.
     * @param activated boolean: The state at which the Cop car is undergoing an arrest.
     */
    public void setArrestModeActivated (boolean activated){
        pursuitModeActivated = activated;
    }
    /**
     * Mutator method that sets the state of whether or not the suspect is being pulled over.
     * @param pulledOver boolean: The state at which a suspect is being pulled over.
     */
    public void setSuspectPulledOver (boolean pulledOver){
        suspectPulledOver = pulledOver;
    }
    /**
     * Controls the movement of the steering wheel by changing the tilt angle based on user input, controls the movement of the police car by updating that police car's position, and updates the health of the car by checking for collision with road boundaries.
     * The first if statement is used to check if the steering wheel is slowly turning back to zero.
     * The second if statement is used to change the tilt angle based on the current tilt angle.
     * The third if statement is used to change the limit of the steering wheel based on the current tilt angle.
     * The fourth if statement is used to check for road collision and decrease the police car health as well as move the car closer to the center of the road if the statement is true.
     * Finally the position of the car is updated according to user input.
     */
    public void move(){
        if (returnToZero){
            if (tilt > 0.5) {
                tilt-= 1;
            } else if (tilt < -0.5) {
                tilt+= 1;
            } else {
                tilt = 0;
            }
        } else {
            tilt += tiltChange;
            if (tilt > 80) {
                tilt = 80;
            } else {
                if (tilt < -80) {
                    tilt = -80;
                }
            }
        }
        if (xCoord <= Game.roadLeft){
            xCoord = Game.roadLeft + 10;
            decrementHull (0.5);
        } else if (xCoord + 60 >= Game.roadRight){
            xCoord = Game.roadRight - 70;
            decrementHull (0.5);
        } else {
            xCoord += moveDirection;
        }
    }
    /**
     * Moves the police car towards the suspect to be interrogated.
     * The first if statement is used to control whether or not the car has reached the suspect vehicle, and if it has, activate the 2 booleans that dictate whether or not the car is currently interrogating the suspect.
     * The second if statement is used to move the car left or right as well as tilt the steering wheel based on the position that the car is currently at relative to the suspect car.
     * The third if statement is used to move the car up or down depending on the distance apart from the Cop car relative to the suspect vehicle.
     */
    public void moveTowards (int x, int y){
        if (x == xCoord && y == yCoord){
            suspectPulledOver = true;
            returnToZero = true;
        } else {
            if (x < xCoord){
                xCoord--;
                tiltChange = -0.5;
            } else if (x > xCoord){
                xCoord++;
                tiltChange = 0.5;
            } else {
                returnToZero = true;
            }
            if (y < yCoord){
                yCoord--;
            } else {
                if (y > yCoord) {
                    yCoord++;
                }
            }
        }
    }
    /**
     * Performs action based on the key that the user presses.
     * The if statement structure used in this method performs actions based on the arrow key that the user presses.
     * @param keyCode Stores the key code of the key that the user has pressed, used for performing actions based on the key.
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT){
            moveDirection = -1;
            returnToZero = false;
            tiltChange = -0.5;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            moveDirection = 1;
            returnToZero = false;
            tiltChange = 0.5;
        } else {
            if (keyCode == KeyEvent.VK_SPACE) {
                pursuitModeActivated = true;
            }
        }
    }    
    /**
     * Performs action based on the key that the user has released after holding.
     * The if statement structure used in this method performs actions based on the key that the user presses.
     * @param keyCode Stores the key code of the key that the user has pressed, used for performing actions based on the key.
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            moveDirection = 0;
            returnToZero = true;
        }
    }
}