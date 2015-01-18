import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;

/** 
 * Programming AE2
 * Class to display cipher GUI and listen for events
 */
public class CipherGUI extends JFrame implements ActionListener
{
	//instance variables which are the components
	private JPanel top, bottom, middle;
	private JButton monoButton, vigenereButton;
	private JTextField keyField, messageField;
	private JLabel keyLabel, messageLabel;

	//application instance variables
	private MonoCipher mcipher;
	private VCipher vcipher;
	private LetterFrequencies letFreq;
	private String fileName; //core part of filename
	private boolean encrypt; //boolean to indicate whether to encrypt or decrypt
	
	/**
	 * The constructor adds all the components to the frame
	 */
	public CipherGUI()
	{
		this.setSize(400,150);
		this.setLocation(100,100);
		this.setTitle("Cipher GUI");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.layoutComponents();
	}
	
	/**
	 * Helper method to add components to the frame
	 */
	public void layoutComponents()
	{
		//top panel is yellow and contains a text field of 10 characters
		top = new JPanel();
		top.setBackground(Color.yellow);
		keyLabel = new JLabel("Keyword : ");
		top.add(keyLabel);
		keyField = new JTextField(10);
		top.add(keyField);
		this.add(top,BorderLayout.NORTH);
		
		//middle panel is yellow and contains a text field of 10 characters
		middle = new JPanel();
		middle.setBackground(Color.yellow);
		messageLabel = new JLabel("Message file : ");
		middle.add(messageLabel);
		messageField = new JTextField(10);
		middle.add(messageField);
		this.add(middle,BorderLayout.CENTER);
		
		//bottom panel is green and contains 2 buttons
		
		bottom = new JPanel();
		bottom.setBackground(Color.green);
		//create mono button and add it to the top panel
		monoButton = new JButton("Process Mono Cipher");
		monoButton.addActionListener(this);
		bottom.add(monoButton);
		//create vigenere button and add it to the top panel
		vigenereButton = new JButton("Process Vigenere Cipher");
		vigenereButton.addActionListener(this);
		bottom.add(vigenereButton);
		//add the top panel
		this.add(bottom,BorderLayout.SOUTH);
	}
	
	/**
	 * Listen for and react to button press events
	 * (use helper methods below)
	 * @param e the event
	 */
	public void actionPerformed(ActionEvent e) 
	{
	    boolean vig; //boolean to indicate which type of encryption/decryption selected
	    if(getKeyword() & processFileName()) {  //first check if filename and keyword are valid
	    	if(e.getSource() == monoButton) {
	    		mcipher = new MonoCipher(keyField.getText()); //create new MonoCipher object
	    		vig = false; //set vig to false
	    	}
	    	else {
	    		vcipher = new VCipher(keyField.getText()); //create new VCipher object
	    		vig = true; //set vig to true
	    	}
	    	letFreq = new LetterFrequencies(); //create new LetterFrequencies object
	    	//call processFile, which returns true if IO operations are successful
	    	boolean finished = processFile(vig); 
	    	if(finished) {
	    		makeFreqFile(fileName, letFreq); //call method to produce frequencies file
	    		System.exit(0); //terminate program after one successful encryption/decryption
	    	}
	    }
	}
	
	/** 
	 * Obtains cipher keyword
	 * If the keyword is invalid, a message is produced
	 * @return whether a valid keyword was entered
	 */
	private boolean getKeyword()
	{
	    boolean goodWord = true; //boolean indicating whether keyword is valid
		String keyword = keyField.getText();
	  
		//checks for empty string first
		if(keyword.isEmpty()) {
	    	goodWord = false; //if no keyword has been input, set goodWord to false
	    }
		//if string not empty pass keyword to wordCheck to check if word is valid
		//set goodWord to result of wordCheck
		else {
	    	goodWord = wordCheck(keyword); //charCheck returns true if word is OK, false otherwise
	    }
		
		if(!goodWord) {// if keyword is invalid, error message is given 
			JOptionPane.showMessageDialog(null, "Please enter a valid keyword. Rules:\n"
    			+ "1.capital letters only 2.no repeating letters", 
				"Come on now", JOptionPane.ERROR_MESSAGE);
			keyField.setText(""); //clear keyword textfield
		}
		return goodWord; //return boolean value of goodWord	
	}
	
	/**charCheck checks if a word's characters are all unique upper case letters
	 * @param word the word
	 * @return whether characters of word are valid
	 */
	private boolean wordCheck(String word) 
	{
		boolean goodLetters = true; // boolean indicating whether all characters are valid
		int letterIndex = 0;
    	while(goodLetters && letterIndex<word.length()) { //loop through word while no problem found
    		 //check if letter is uppercase
    		if(word.charAt(letterIndex) >= 'A' && word.charAt(letterIndex) <= 'Z') {
    			//if upper case, check if letter is repeated - if it is, goodLetters is false, loop is exited
    			goodLetters = (noRepetition(letterIndex, word));
    			letterIndex++;
    		}
    		else { //if letter is not uppercase, goodLetters is false, exits loop
    			goodLetters = false;
    		}	
    	}
    	return goodLetters; //return boolean value of goodLetters after checks completed
	}
	
	/**
	 * checkRepeat checks if a character is repeated within the keyword
	 * @param i the position within the word of the character to be checked
	 * @param s the word
	 * @return true if character does not repeat, false otherwise 
	 */
	private boolean noRepetition(int i, String s) 
	{
		boolean noRep = true; //boolean value indicating whether a character is repeated
		int nextChar = i+1; //starts checking against position of character + 1
		//loop comparing letter being checked against each letter in front of it
		while(noRep && nextChar<s.length()) { 
			if(s.charAt(i) == s.charAt(nextChar))
				noRep = false; //if repeated character is found, noRep set to false, exit loop
			else
				nextChar++;
		}
		return noRep; //return boolean value of noRep after check
	}
	
	/** 
	 * Obtains filename from GUI
	 * The details of the filename and the type of coding are extracted
	 * If the filename is invalid, a message is produced 
	 * The details obtained from the filename must be remembered
	 * @return whether a valid filename was entered
	 */
	private boolean processFileName()
	{
	    fileName = messageField.getText();
	    if(fileName.isEmpty()) {//first check for empty string
	    	JOptionPane.showMessageDialog(null, "Don't forget to enter a file name", 
					"No file name entered", JOptionPane.ERROR_MESSAGE);
	    	return false; //if no filename has been entered, return false
	    }
	    
	    			
	    char last = fileName.charAt(fileName.length()-1); //get last character of filename entered
	    
	    if(last == 'P' || last == 'C') { //check for valid filename (must end in P or C)
	    	fileName = fileName.substring(0,fileName.length()-1); //set core fileName (without last letter)
	    	if(last == 'P')
	    		encrypt = true; //last letter P indicates a file to be encrypted
	    	else
	    		encrypt = false; //last letter C indicates a file to be decrypted
	    	return true; //finally, return true
	    }
	   
	    else { //if filename entered does not end in P or C, show error message and return false
	    	JOptionPane.showMessageDialog(null, "Please enter valid file name", 
					"Come on now", JOptionPane.ERROR_MESSAGE);
	    	messageField.setText(""); //clear message textfield
	    	return false;
	    }
	}
	
	/** 
	 * Reads the input text file character by character
	 * Each character is encoded or decoded as appropriate
	 * and written to the output text file
	 * @param vigenere whether the encoding is Vigenere (true) or Mono (false)
	 * @return whether the I/O operations were successful
	 */
	private boolean processFile(boolean vigenere)
	{
		
		FileReader reader = null;
		FileWriter writer = null;	
		
		try {
			try {
				reader = new FileReader(getInputName()); //create new FileReader object
				writer = new FileWriter(getOutputName()); //create new FileWriter object
				boolean done = false;
				while(!done) { //loop through until end of file
					int next = reader.read();
					if(next == -1) { //no more characters in file
						done = true; //set done to true, exit loop
					}
					else {
						char c = (char) next;
						//process each character (encrypt/decrypt/do nothing) and write result to file
						writer.write(processChar(c, vigenere)); 
					}
				}
			}
			finally { //close files
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			}
			return true; //if IO operations successful, return true
		}

		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "The file you entered was not found", 
					"Sorry", JOptionPane.ERROR_MESSAGE);
	    	messageField.setText("");
			return false; //if IO operations not successful, return false
		}
	}
	
	/**
	 * gets the name of the file to be read from
	 * @return the name of the file
	 */
	private String getInputName() 
	{
		if(encrypt) //if objective is encryption, name of file to be read should end in 'P'
			return fileName + "P.txt";
		else //if objective is decryption, name of file to be read should end in 'C'
			return fileName + "C.txt";
	}
	
	/**
	 * gets the name of the file to be written to
	 * @return the name of the file
	 */
	private String getOutputName() 
	{
		if(encrypt) //if objective is encryption, name of file to be written should end in 'C'
			return fileName + "C.txt";
		else //if objective is decryption, name of file to be written should end in 'D'
			return fileName + "D.txt";
	}
	
	/**
	 * takes char and checks if is upper-case alphabetical - if it is, encodes or decodes accordingly
	 * and passes to letter frequencies method addChar.
	 * @param ch the character to be encoded/decoded/left the same
	 * @param vigenere boolean indicating which cipher to use
	 * @return char (which has either been encoded, decoded or left untouched)
	 */
	private char processChar(char ch, boolean vigenere) 
	{	
		if(ch >= 'A' && ch <= 'Z') { //first check if char is uppercase letter; if not it is left the same
			if(encrypt) { //then if it is to be encrypted or decrypted
				if(!vigenere) { //then what type of encryption
					ch = mcipher.encode(ch);
				}
				else {
					ch = vcipher.encode(ch, letFreq);
				}
			}
			else { //in case char is to be decrypted
				if(!vigenere) { //again check what type of decryption
					ch = mcipher.decode(ch);
				}
				else {
					ch = vcipher.decode(ch, letFreq);
				}
			}
			//after encryption/decryption give new character to LetterFrequencies object
			letFreq.addChar(ch);
		}
		//finally, return the character
		return ch;
	}
	
	/**
	 * Writes the letter frequencies report to a file
	 * @param fName the root file name
	 * @param lf the LetterFrequencies object
	 */
	private void makeFreqFile(String fName, LetterFrequencies lf) 
	{
		PrintWriter freqWriter = null;
		String freqFileName = fName + "F.txt"; //frequencies filename should end in 'F'
		try {
			try{
				freqWriter = new PrintWriter(freqFileName); //create new PrintWriter object
				String report = lf.getReport(); //get frequencies report from LetterFrequencies object
				freqWriter.print(report); //write report to file
			}
			finally {
				if(freqWriter != null)
					freqWriter.close(); //close file
			}
		}
	
		catch(IOException e) { //in case of an IO problem with frequencies file
			JOptionPane.showMessageDialog(null, "There has been a problem with the Frequencies File", 
					"Sorry", JOptionPane.ERROR_MESSAGE);
	    	messageField.setText("");;
		}
	}
}
