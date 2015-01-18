/**
 * Programming AE2
 * Processes report on letter frequencies
 */
public class LetterFrequencies
{
	/** Size of the alphabet */
	private final int SIZE = 26;
	
	/** Count for each letter */
	private int [] alphaCounts;
	
	/** The alphabet */
	private char [] alphabet; 
												 	
	/** Average frequency counts */
	private double [] avgCounts = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0,
							       0.2, 0.8, 4.0, 2.4, 6.7, 7.5, 1.9, 0.1, 6.0,  
								   6.3, 9.1, 2.8, 1.0, 2.4, 0.2, 2.0, 0.1};

	/** Character that occurs most frequently */
	private char maxCh;

	/** Total number of characters encrypted/decrypted */
	private int totChars;
	
	/**
	 * Instantiates a new letterFrequencies object.
	 */
	public LetterFrequencies()
	{
		//create and populate arrays
		alphabet = new char [SIZE];
		alphaCounts = new int [SIZE];
		for (int i = 0; i < SIZE; i++) {
			alphabet[i] = (char)('A' + i);
			alphaCounts[i] = 0; //alphaCounts starts at 0 in every position
		}
	}
	
	/**
	 * Accessor method for totChars
	 * @return totChars
	 */
	public int getTotChars() 
	{
		return totChars;
	}
		
	/**
	 * Increases frequency details for given character
	 * @param ch the character just read
	 */
	public void addChar(char ch)
	{
		int position = ch - 'A'; //get position of char in alphabet
		alphaCounts[position]++; //increase corresponding frequency in alphaCounts
		totChars++; //add 1 to total amount of characters
	}
	
	/**
	 * Gets the maximum frequency
	 * @return the maximum frequency
	 */
	private double getMaxPC()
	{
		int maxSoFar = alphaCounts[0]; //set maxSoFar at alphaCounts[0]
	  
		for(int i = 1; i<SIZE; i++) { //loop through alphaCounts
			if(alphaCounts[i] >= maxSoFar) { //equal or larger frequency found
				maxSoFar = alphaCounts[i]; //set maxSoFar to larger (or equal but occurring later) frequency
				maxCh = alphabet[i]; //set maxCh to character at corresponding position of alphabet
			}
		}
		return (maxSoFar*100.0)/totChars;  // return frequency of maxCh as percentage of total
	}
	
	/**
	 * Returns a String consisting of the full frequency report
	 * @return the report
	 */
	public String getReport() 
	{
	    //set title
		String title = "LETTER ANALYSIS\r\n\r\n";
		
		//set table headings
		String headings = String.format("%-10s%-10s%-10s%-10s%6s%n", "Letter", "Freq", "Freq%", "AvgFreq%",
				"Diff");
		
		//set table content by calling getTableContent()
		String content = getTableContent();
		
		//set last line
		double maxPC = getMaxPC(); //gets max percentage, also updates maxCh accordingly
		String lastLine = String.format("%nThe most frequent letter is %s at %.1f%%", maxCh, maxPC);
		
		//return String consisting of whole report
		String report = title+headings+content+lastLine;
		return report;
	}
	
	/**
	 * creates content of table - frequency info for each letter
	 * @return table content as a String
	 */
	private String getTableContent()
	{
		//StringBuilder used for this to avoid inefficiencies from concatenating in loop
		StringBuilder contentBuilder = new StringBuilder();
		//to avoid dividing by 0 in case of a file not containing any uppercase letters
		if(totChars==0)
			totChars=1; 
		
		for(int i=0; i < SIZE; i++) { //loop to produce info for each letter of alphabet
			double freqPC = ((alphaCounts[i]*100.0)/totChars); //calculate frequency %
			double difference = freqPC-avgCounts[i]; // calculate difference from average freq %
			//add new row to content
			contentBuilder.append(String.format("%-10s%-10d%-10.1f%-10.1f%6.1f%n", alphabet[i], alphaCounts[i],
				freqPC, avgCounts[i], difference));
		}
		return contentBuilder.toString(); //return full content as a String
	}
}
