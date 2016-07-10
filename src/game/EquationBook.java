package game;

import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

/**
 * This class stores all the equations to be loaded into the game. Total time spent: 1 hour.
 * @author Joshua Yuan
 * @version 1.0 May 16, 2014
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - Equations loaded are now dependant on the level of the game, a helper method to load the files is now being used.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - June 12, 2014 - No changes made.
 */ 
public class EquationBook
{
    /** Stores all the possible equations as stored in the POK files. */
    private ArrayList<String> equations = new ArrayList<String> ();
    
    /** 
     * Loads all the equations used for the game of the specified level. 
     * @param level int: Specifies the maximum difficulty of equations that can be stored in this EquationBook.
     */
    public EquationBook (int level){
        loadEquations (level);
    }
    
    /**
     * Loads all of the equations stored in <code>input</code> into <code>equations</code>.
     * The try-catch catches an IOException. The if statement checks if the file has a malid header. The for loop
     * iterates through all of the lines stored in the file specified by fileName.
     * @param fileName String reference: The name of the file to be read.
     * @param input BufferedReader reference: The file which contains the equations.
     * @param line String reference: The line currently being read from the file.
     * @param i int: Used in the for loop.
     * @param e IOException reference: Contains info about the IOException, if thrown.
     * @throws IOException if an error occurs while reading to the file.
     */ 
    private void loadFile (String fileName){
        try {
            BufferedReader input = new BufferedReader (new FileReader ("./game/" + fileName));
            String line;
            if (!input.readLine ().equals ("Pokilangitis chemistry file.")){
                JOptionPane.showMessageDialog (null, "The chemistry files for this program could not be loaded.\nPlease reinstall the program.", "File Error", JOptionPane.ERROR_MESSAGE);
                System.exit (-1);
            }
            for (int i = 0 ; (line = input.readLine ()) != null ; i++){
                equations.add (line);
            }
        } catch (IOException e){
        }
    }
    
    /**
     * Loads all the equations from the necessary database files to the game.
     * @param level int: The maximum difficulty of equations that will be stored in <code>equations</code>.
     */
    private void loadEquations (int level){
        for (int i = 1; i <= level; i++){
            loadFile ("Equations" + i + ".pok");
        }
    }
    
    /**
     * Accessor method that gets a random equation from the equation ArrayList to be displayed to the user.
     * @return ChemicalEquation: A random equation from the equation ArrayList.
     */
    public ChemicalEquation getRandomEquation (){
        return new ChemicalEquation (equations.get ((int)(Math.random () * equations.size ())));
    }
}