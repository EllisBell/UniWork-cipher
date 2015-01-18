/**
 * Programming AE2
 * Contains monoalphabetic cipher and methods to encode and decode a character.
 */
public class MonoCipher
{
	/** The size of the alphabet. */
	private final int SIZE = 26;

	/** The alphabet. */
	private char [] alphabet;
	
	/** The cipher array. */
	private char [] cipher;

	/**
	 * Instantiates a new mono cipher.
	 * @param keyword the cipher keyword
	 */
	public MonoCipher(String keyword)
	{
		//create alphabet
		alphabet = new char [SIZE];
		for (int i = 0; i < SIZE; i++) {
			alphabet[i] = (char)('A' + i);
		}
		
		//create cipher
		cipher = new char[SIZE];
		//create first part of cipher by inserting keyword
		for(int i = 0; i<keyword.length(); i++) {
			cipher[i] = keyword.charAt(i);
		}
		
		//create second part of cipher from remaining letters of alphabet in reverse
		int cipherSize = keyword.length(); // current size of array starts at length of keyword
		int alphIndex = SIZE-1; //start alphabet index at 'Z'
		StringBuilder word = new StringBuilder(keyword); //make StringBuilder from keyword to use .delete() below
		while(cipherSize < SIZE) { //loop through until cipher is full
			boolean found = false;
			int i=0;
			//take each letter and check if it is already in the keyword/cipher
			while(!found && i < word.length()) { 
				if(alphabet[alphIndex] == word.charAt(i)){  
					found = true;
					/* if letter being checked is in keyword, remaining letters don't need to be 
					 * checked against that letter of the keyword - so it can be removed*/
					word.delete(i,i+1);
				}
				else 
					i++;
			}
			if(!found) { //if letter not found, add to cipher in next available position
				cipher[cipherSize] = alphabet[alphIndex];
				cipherSize++; //increment current size of cipher
			}
			alphIndex--; //decrement to check next letter of alphabet
		}
		
		//print content of alphabet and cipher
		for(char letter : alphabet) {
			System.out.print(letter);
		}
		
		System.out.println();
		
		for(char letter : cipher) {
			System.out.print(letter);
		}
	}
	
	/**
	 * Encode a character
	 * @param ch the character to be encoded
	 * @return the encoded character
	 */
	public char encode(char ch)
	{
		int position = ch-'A'; //get position of character in alphabet
		char encryptedChar = cipher[position]; 
		return encryptedChar;  //return the character in the corresponding position of cipher
	}

	/**
	 * Decode a character
	 * @param ch the character to be encoded
	 * @return the decoded character
	 */
	public char decode(char ch)
	{
	    int position = 0;
	    boolean found = false;
	    while(!found && position<SIZE) { //look for character in cipher
	    	if(ch==cipher[position]) {
	    		found = true; //character's position in cipher has been obtained, exit loop
	    	}
	    	else
	    		position++; //if not found, try next position
	    }
		return alphabet[position];  // return the character in the corresponding position of cipher
	}
}
