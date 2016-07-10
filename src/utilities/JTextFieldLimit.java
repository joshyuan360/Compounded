package utilities;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class is borrowed from java2s.com and is used to limit the JTextField input length for user name input.
 * http://www.java2s.com/Code/Java/Swing-JFC/LimitJTextFieldinputtoamaximumlength.htm
 * @author Programmer of java2s.com
 * @version 1.0 June 2, 2014.
 */ 
public class JTextFieldLimit extends PlainDocument {
    /** Stores the numeric limit of number of characters that can fit in the JTextField. */
    private int limit;
    
    /** Constructor which stores the limit of the characters that can fit in the field. */
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    
    /**
     * Method which inserts the String into the JTextField and limits the field size.
     * The if statement is used to check that str is not null, and that the length of the string being put into the 
     * existing document length exceeds the limit set.
     */
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str != null && (getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
