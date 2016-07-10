package game;
/**
 * This class represents a chemical equation. Total time spent: 3 hours.
 * @author Joshua Yuan.
 * @version 1.0 May 29, 2014.
 * @version 2.0 - No changes made.
 * @version 3.0 - No changes made.
 * @version 4.0 - Debugged. Previously, the getIncompleteEquation () did not always correctly determine the correct
 * value of missingIndex.
 * @version 5.0 - No change made.
 */ 
public class ChemicalEquation
{
    /** Stores the String containing the whole chemical equation. */
    private String equation;
    /** Stores the answer/missing element required to complete the chemical equation. */
    private String missingElement;
    
    /**
     * Class constructor creates a new <code>ChemicalEquation</code> with the specified <code>equation</code>.
     * @param equation The chemical equation of this <code>ChemicalEquation</code>.
     */
    public ChemicalEquation (String equation){
        this.equation = " " + equation + " ";
        missingElement = utilities.ChemistryUtilities.getRandomElementOrCompound (equation);
    }
    
    /**
     * Accessor method used to get the incomplete chemical equation and return it to the user.
     * @return String reference: The incomplete chemical equation to be solved by the game plaer.
     * @param missingIndex int: Stores the index at which the missing element is located in the incomplete chemical equation.
     */
    public String getIncompleteEquation (){
        int missingIndex = equation.indexOf (" " + missingElement + " ") + 1;
        return equation.substring (1, missingIndex) + "?" + equation.substring (missingIndex + missingElement.length (), equation.length () - 1);
    }
    
    /**
     * Accessor method used to get the missing element from the equation.
     * @return String reference: The missing element, which replaces the ? to solve the equation returned by getIncompleteEquation.
     */
    public String getMissingElement (){
        return missingElement;
    }
}