package highscores;
/**
 * This class represent a game player of Compounded. It is used to load and display the high scores.
 * Total time spent: 1 hour.
 * @author Joshua Yuan.
 * @version 1.0 May 18, 2014.
 * @version 2.0 - No changes made.
 * @version 3.0 - No changes made.
 * @version 4.0 - No changes made.
 * @version 5.0 - Mutator methods have been removed, since the state of GamePlayers will not need to be changed once
 * it is created. The constructor with one parameter has been removed, since it is no longer necessary now that
 * high scores has been implemented differently.
 */ 
public class GamePlayer
{
    /** Stores the player's username. */
    private String name;
    /** Stores the player's level for their high score. */
    private String level;
    /** Stores the player's score for the level. */
    private int score;
    
    /**
     * Constructor creates a new GamePlayer with the specified name, level, and score.
     * @param name String reference: Stores the new player's username.
     * @param level String reference: Stores the new player's level.
     * @param score String reference: Stores the new player's score.
     */
    public GamePlayer (String name, String level, int score){
        this.name = name;
        this.level = level;
        this.score = score;
    }
    
    /**
     * Accessor method that returns the name of the player in this record.
     * @return String reference: the player name.
     */
    public String getName (){
        return name;
    }
    
    /**
     * Accessor method that returns the level of the player in this record.
     * @return String reference: the player level.
     */
    public String getLevel (){
        return level;
    }
    
    /**
     * Accessor method that return the score of the player in this record.
     * @return String reference: the player score.
     */
    public int getScore (){
        return score;
    }
}