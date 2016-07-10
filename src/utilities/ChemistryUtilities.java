package utilities;

import java.util.*;
import java.util.regex.*;

/**
 * This class contains useful utility methods needed in the Compounded game for manipulating chemical equations. Total time spent: 5 hours.
 * @author Joshua Yuan.
 * @version 1.0 - May 16, 2014.
 * @version 2.0 - No changes made.
 * @version 3.0 - No changes made.
 * @version 4.0 - No changes made.
 * @version 5.0 - Debugged. Previously, getConfusingElement () would return elements with unexpectedly large
 * coefficients.
 */
public class ChemistryUtilities
{    
    /**
     * Accessor method used to return a random element in the chemical equation you specify.
     * While loop is used to indefinitely scan every token in the equation String until every possible token has been processed.
     * If structure is used to exclude tokens that are not elements, in this case the tokens can either be "+" or "->".
     * @return String reference: A random element in the chemical equation.
     * @param equation String reference: The chemical equation that you would like for a random element to be returned.
     * @param list ArrayList of Strings reference: Stores the list of possible verified elements that can be returned.
     * @param st StringTokenizer reference: Allows the use of the StringTokenizer to split a string by the spaces to determine the elements in the equation.
     * @param element String reference: Stores the next potential element to be processed.
     */
    public static String getRandomElementOrCompound (String equation){
        ArrayList<String> list = new ArrayList<String> ();
        StringTokenizer st = new StringTokenizer (equation);
        while (st.hasMoreTokens ()){
            String element = st.nextToken ();
            if (!element.equals ("+") && !element.equals ("->")){
                list.add (element);
            }
        }
        return list.get ((int)(Math.random () * list.size ()));
    }
       
    /**
     * Accessor method used to return the indexes of all the coefficients, subscripts, and charges in an element.
     * For loop is used to pass through the element to scan for numbers.
     * If structure is used to add only characters that are digits in the element to the list ArrayList.
     * @return ArrayList reference: The list of all the indexes of coefficients, subscripts, and charges in an element.
     * @param element String reference: Stores the next potential element to be processed.
     * @param list ArrayList of Strings reference: Stores the list of possible verified elements that can be returned.
     * @param i loop integer that controls the number of times the loop executes for the characters in the element.
     */
    private static ArrayList<Integer> getNumberIndices (String element){
        ArrayList<Integer> list = new ArrayList<Integer> ();
        for (int i = 0; i < element.length (); i++){
            if (Character.isDigit (element.charAt (i)) && (i == 0 || !Character.isDigit (element.charAt (i - 1)))){
                list.add (i);
            }
        }
        return list;
    }
    
    /**
     * This method returns all the numbers in the specified element. This includes coefficients, subscripts, and charges.
     * The while loop is used to find all the matches in the element specified. It runs until there are no more pattern matches for numbers.
     * @param element String reference: The element to be processed.
     * @param list ArrayList<Integer> reference: The list containing the numbers in the element string.
     * @param m Matcher reference: Used to find all of the numbers in this element.
     * @return ArrayList<Integer>: A list containing all of the numbers stored in the specified element.
     */ 
    private static ArrayList<Integer> getElementNumbers (String element){
        ArrayList<Integer> list = new ArrayList<Integer> ();
        Matcher m = Pattern.compile("\\d+").matcher (element);
        while (m.find()) {
            list.add(Integer.parseInt (m.group()));
        }
        return list;
    }
        
    /**
     * Returns an incorrect element that is similar to the specified element.
     * For loop iterates through all of the numbers in <code>elementNumbers</code>.
     * The first if structure is used to add a coefficient to the element being manipulated.
     * The second if statement is used to see if the element doesn't have any numbers attached to it.
     * The third if statement is used to add a subscript to the element if it doesn't have a subscript already.
     * The fourth if statement is used to set the offset length in the equation based on the new subscript length.
     * The last if statement reruns the method in the rare case that getConfusingElement returns the same String as
     * element.
     * The for loop iterates through all of numbers in <code>elementNumbers</code>, replacing the number in the array
     * with a new number in <code>newElement</code>.
     * @param elementNumbers ArrayList<Integer> reference: Contains the numbers in the specified element.
     * @param numberIndices ArrayList<Integer> reference: Contains the indices of the first digit of the numbers in <code>element</code>.
     * @param newElement String reference: Stores a string that is similar to <code>element</code>.
     * @param offset int: Stores the difference between the length of <code>element</code> and <code>newElement</code>.
     * @param i int: Used in the for loop.
     * @param change: The difference between a certain number in <code>element</code> and the new replacement number.
     * @param newSubscript: The new number that will replace an old number in <code>element</code>.
     * @param changed boolean: True if the element's coefficient has been changed.
     * @return String reference: An element similar, but difference than <code>element</code>.
     */ 
    public static String getConfusingElement (String element){
        ArrayList<Integer> elementNumbers = getElementNumbers (element);
        ArrayList<Integer> numberIndices = getNumberIndices (element);
        String newElement = element;
        int offset = 0;
        boolean changed = false;
        
        if (!Character.isDigit (element.charAt (0)) && Math.random () < 0.3){
            newElement = ((int)(Math.random () * 3) + 2) + newElement;
            changed = true;
            offset += newElement.length () - element.length ();
        }

        if (elementNumbers.size () == 0){
            if (!changed || Math.random () < 0.5){
                newElement += ((int)(Math.random () * 3) + 2);
            }
            return newElement;
        }
        
        for (int i = 0; i < elementNumbers.size (); i++){
            int change = ((int)(Math.random () * 4) + 1) * (Math.random () < 0.5 ? -1 : 1);
            int newSubscript = Math.abs (elementNumbers.get (i) + change);
            newElement = newElement.substring (0, numberIndices.get (i) + offset) + newSubscript + newElement.substring (numberIndices.get (i) + elementNumbers.get (i).toString ().length () + offset);
            if (("" + newSubscript).length () > elementNumbers.get (i).toString ().length ()){
                offset++;
            } else {
                if (("" + newSubscript).length () < elementNumbers.get (i).toString ().length ()){
                    offset--;
                } 
            }
        }
        
        newElement = newElement.replaceAll ("1", "");
        newElement = newElement.replaceAll ("0", "");
        
        if (!element.equals (newElement)){
            return newElement;
        }
        return getConfusingElement (element);
    }
    
}