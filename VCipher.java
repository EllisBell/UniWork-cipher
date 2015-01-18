/**
 * Programming AE2
 * Class contains Vigenere cipher and methods to encode and decode a character
 */
public class VCipher
{
	private char [] alphabet;   //the letters of the alphabet
	private final int SIZE = 26;
	private char [][] cipher;
	
	/** 
	 * The constructor generates the cipher
	 * @param keyword the cipher keyword
	 */
	public VCipher(String keyword) 
	{	
		//create alphabet array
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++) {
			alphabet[i] = (char)('A' + i);
		}
		
		//create cipher array
		cipher = new char[keyword.length()][SIZE];
		//populate each row/array of cipher array by copying relevant parts of alphabet array
		for(int rowIndex = 0; rowIndex < cipher.length; rowIndex++) { //loop through each row
			//find position in alphabet of first letter of row (which is at position rowIndex within keyword)
			int indexOfFirst = keyword.charAt(rowIndex)-'A';
			//get size of first part of row - letters from the relevant keyword letter up to 'Z'
			int sizeUpToZ = SIZE-indexOfFirst;
			//get size of second part of row - the remaining letters starting from 'A'
			int sizeFromA = SIZE-sizeUpToZ;
			//copy relevant parts of alphabet array to row of cipher array
			System.arraycopy(alphabet, indexOfFirst, cipher[rowIndex], 0, sizeUpToZ);
			System.arraycopy(alphabet, 0, cipher[rowIndex], sizeUpToZ, sizeFromA);
		}
		
		//print content of arrays
		for(int i = 0; i < SIZE; i++) {
			System.out.print(alphabet[i]);
		}
		
		System.out.println();
		
		for(int row=0; row < keyword.length(); row++) {
			for(int col=0; col < SIZE; col++) {
				System.out.print(cipher[row][col]);
			}
			System.out.println();
		}
	}
	
	
	/**
	 * Encode a character
	 * @param ch the character to be encoded
	 * @param lf the LetterFrequencies object created in CipherGui
	 * @return the encoded character
	 */	
	public char encode(char ch, LetterFrequencies lf) 
	{
		int rowIndex = lf.getTotChars() % cipher.length; //get which row to use for encoding
		int colPosition = ch-'A'; //find position of character within alphabet
	    //get character at corresponding position of relevant row of cipher
	    char encryptedChar = cipher[rowIndex][colPosition];
		return encryptedChar; 
	}
	
	/**
	 * Decode a character
	 * @param ch the character to be decoded
	 * @param lf the LetterFrequencies object created in CipherGui
	 * @return the decoded character
	 */  
	public char decode(char ch, LetterFrequencies lf) 
	{ 
		int rowIndex = lf.getTotChars() % cipher.length; //get which row to use for decoding
		int colPosition=0;
	    boolean found = false;
	    while(!found && colPosition<SIZE) {//look for character in relevant row of cipher
	    	if(ch == cipher[rowIndex][colPosition]) 
	    		found = true; //character's position within row of cipher has been found, exit loop
	    	else
	    		colPosition++; //if not found, try next position
	    }
	    //get character at corresponding position of alphabet
		char decryptedChar = alphabet[colPosition];
		return decryptedChar;
	}
}
