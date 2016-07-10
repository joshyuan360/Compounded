package utilities;

import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

/**
 * This methods provides methods for the game class that are used to generate "random" driver names and descriptions. Total time spent: 1 hour.
 * @author Joshua Yuan.
 * @version 1.0 May 16, 2014.
 * @version 2.0 - May 23, 2014 - No changes made.
 * @version 3.0 - May 30, 2014 - <code>getRandomDescription ()</code> code has been simplified.
 * @version 4.0 - June 6, 2014 - No changes made.
 * @version 5.0 - The entire class has been simplified by adding a helper method called arrayLoader. All three
 * arrays are now loaded using the same method.
 */ 
public class DriverUtilities
{
    /** Contains all of the first names stored in FirstNames.pok. */
    private ArrayList<String> firstNames = new ArrayList<String> ();
    /** Contains all of the last names stored in LastNames.pok. */
    private ArrayList<String> lastNames = new ArrayList<String> ();
    /** Contains all of the criminal descriptions stored in Descriptions.pok. */
    private ArrayList<String> descriptions = new ArrayList<String> ();
    
    /** Constructor loads the three ArrayList objects. */
    public DriverUtilities (){
        arrayLoader ("FirstNames.pok", firstNames);
        arrayLoader ("LastNames.pok", lastNames);
        arrayLoader ("Descriptions.pok", descriptions);
    }
    
    /**
     * Loads the specified list with strings that are stored in the file specified by fileName.
     * The if structure checks if the file header is valid. The inner if structure checks if the file being read
     * is the Descriptions file.
     * The try-catch catches an IOException. The while loop iterates through all of the lines in Descriptions.pok.
     * @param fileName String reference: The name of the file to be loaded.
     * @param list ArrayList<String> reference: The array to be loaded from the file.
     * @param input BufferedReader reference: Used to read into the file specified by fileName.
     * @param line String reference: The line currently being read in from the file.
     * @param e IOException reference: Stores info about the IOException that may be thrown.
     * @throws IOException if an error occurs while reading a file.
     */ 
    private void arrayLoader (String fileName, ArrayList<String> list){
        try {
            BufferedReader input = new BufferedReader (new FileReader ("./utilities/" + fileName));
            if (!input.readLine ().equals ("Pokilangitis file verification header.")){
                JOptionPane.showMessageDialog (null, fileName + " could not be loaded.\nPlease reinstall the program.", "File Error", JOptionPane.ERROR_MESSAGE);
                System.exit (-1);
            }
            
            String line;
            while ((line = input.readLine ()) != null){
                if (fileName.equals ("Descriptions.pok")){
                    list.add (line);
                } else {
                    list.add (line.substring (0, line.indexOf (" ")));
                }
            }
        } catch (IOException e){
        }
    } 
    
    /**
     * Returns a random description of a criminal.
     * @return String reference: A random description of a criminal.
     */ 
    public String getRandomDescription (){
        return descriptions.get ((int) (Math.random () * descriptions.size ()));
    }
    
    /**
     * Returns a random name consisting of a given name and a surname.
     * @param randIndex1 int: A random number between 0 (inclusive) and the number of first names to choose from (exclusive).
     * @param randIndex2 int: A random number between 0 (inclusive) and the number of last names to choose from (exclusive).
     * @return String reference: A random name of a person.
     */ 
    public String getRandomName (){
        int randIndex1 = (int) (Math.random () * firstNames.size ());
        int randIndex2 = (int) (Math.random () * lastNames.size ());
                   
        return firstNames.get (randIndex1).substring (0, 1) + firstNames.get (randIndex1).substring (1).toLowerCase () + " " + 
            lastNames.get (randIndex2).substring (0, 1) + lastNames.get (randIndex2).substring (1).toLowerCase ();
    }
    
}