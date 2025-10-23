//Connor Durkin
//CS 570 
//Homework 6

package homework6;

//IMPORT
	import java.io.IOException;
	import java.util.*;
	import java.io.*;

//ClASS DECLARATION: ANAGRAMS	
	
/** Computes the list of words in a given dictionary 
 *  that has the most number of anagrams. 
 */
public class Anagrams {

	
// CONSTANTS & PARAMETERS DECLRATIONS
	
	//array initialized with the first 26 prime numbers
	final Integer[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23 ,29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};	
	
	//hash table that will associate each letter in the alphabet with a prime number
	Map<Character,Integer> letterTable;
	
	/** hashtable that will store entries, each of which will include a 
	 *  key, being the hash code of the word, and the values, 
	 *  being an array list of words with the same hashcode (anagrams)
	 */ 
	Map<Long,ArrayList<String>> anagramTable;
	
// CONSTRUCTOR: ANAGRAMS

	/** Contructs Anagrams class, which invokes the buildLetterTape class
	 * 	and initializes the hashmap anagramTable
	 * 	@post: buildLetterTable and anagramTable initialized
	 */
	public Anagrams () {
		buildLetterTable();
		anagramTable = new HashMap<Long,ArrayList<String>>();
	}
	

//CONSTRUCTOR: buildLetterTable
	
	/** Builds the hashtble letterTable which matches each letter 
	 *  of the alphabet with a prime number
	 *  @pre: letterTable unbuilt and empty
	 *  @post: letterTable constructed and filled with alphabet/prime numbers
	 */
	private void buildLetterTable() {
		
		letterTable = new HashMap<Character,Integer>();
		
		int i = 0;
		
		for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
			letterTable.put(alphabet, primes[i]);
			i++;
		}
		
	}
	
//METHOD: ADDWORD	
	
	/** Adds the word being processed from the dictionary into the 
	 *  anagramTable. If the word is an anagram of already added word, 
	 *  it's added to the respective arraylist. If it's a new word, it's
	 *  added as its own entry.  
	 *  @pre no entry includes String s in anagramTable
	 *  @param s: the word being analyzed/added to anagramList
	 *  @post the string s is stored within an entry in anagramTable
	 */
	private void addWord(String s) {
		
		//create hashcode for string using myHashCode method
		long hashCode = myHashCode(s);
		
		//if the anagramTable does not include this hashcode, no anagram has been stored
		//add hashcode and associated string as an entry into anagramtable
		if(!anagramTable.containsKey(hashCode)) {
			ArrayList<String> evaluatedword = new ArrayList<String>();
			evaluatedword.add(s);
			anagramTable.put(hashCode, evaluatedword);
		} 
		
		//if the anagramTable does include the hashcode, an anagram has already been stored
		//pull the arraylist of the hashcode, add the string, then replace former arraylist with updated arraylist
		else {
			ArrayList<String> evaluatedword = anagramTable.get(hashCode);
			evaluatedword.add(s);
			anagramTable.put(hashCode, evaluatedword);
		}
		
	}
	
	
//METHOD: MYHASHCODE
	
	/* Given a string s, computes its hashcode by multiplying
	 * the prime numbers associated with each of its characters,
	 * as defined by the letterTable
	 * @pre word, s, does not have hashcode
	 * @param s - the word being turned into a hashcode
	 * @return hashCode - long value that is hashcode for string s 
	 */
	private Long myHashCode(String s) {
	
		
		//initialize long hashCode to be returned
		long hashCode = 1;
		
		//cycle through each character and multiple current value of hashCode by the new prime number
		//check for an characters that are not letters; if number, throw IllegalArgumentException
		
		for (int i = 0; i < s.length(); i++) {
			
			char currentchar = s.charAt(i);
			
			if (Character.isLetter(currentchar)) {
				hashCode *= letterTable.get(currentchar);
			} else {
				throw new IllegalArgumentException("The word analyzed includes a character that is not a letter. Only letters can be evaluated by this program."); 
			}
			
		}
		
		//return hashCode as a Long value
		return (Long)hashCode;
		
		
	}
	
	
//METHOD: PROCESSFILE 
	
	/* Receives the name of a text file containing words, one per line
	 * and builds the hash table anagramTable using addWord method. 
	 * @param s: file name to be processed 
	 * @post: anagramTable filled with entries of anagrams 
	 */
	private void processFile (String s) throws IOException {
		FileInputStream fstream = new FileInputStream(s);
		BufferedReader br = new BufferedReader (new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			this.addWord(strLine);
		}
		br.close();
	}
	
//METHOD: getMaxEntries
	
	/* Returns the entries in the anagramTable that have the largest number of anagrams
	 * If more than one list of anagrams of the maximum size, a list of all will be returned
	 * @post: returns an arraylist of entries, which includes the shared key and list of anagrams
	 */
	private ArrayList<Map.Entry<Long,ArrayList<String>>> getMaxEntries(){
		
		//create Iterator, to be used to process all entries in Map
		Iterator<Map.Entry<Long, ArrayList<String>>> iterator = anagramTable.entrySet().iterator();
		
		//initialize return value, maxreturn, being an arraylist of map entries
		ArrayList<Map.Entry<Long,ArrayList<String>>> maxreturn = new ArrayList<Map.Entry<Long,ArrayList<String>>>();
		
		//use while loop to process all entries in map
		while (iterator.hasNext()) {
			
			Map.Entry<Long,ArrayList<String>> entry = iterator.next();
			
			//check if maxreturn is empty. Add current entry if so.
			if(maxreturn.isEmpty()) {
				maxreturn.add(entry);
			} 
			
			//check if current entry has more anagrams than current maxreturn
			//if so, clear maxreturn and add new entry
			else if (entry.getValue().size() > maxreturn.get(0).getValue().size()) {
				maxreturn.clear();
				maxreturn.add(entry);
			}
			
			//check if current entry has equal amount of anagrams as current maxreturn
			//if so, add current entry to maxreturns
			else if (entry.getValue().size() == maxreturn.get(0).getValue().size()) {
				maxreturn.add(entry);
			}
			
		}
		
		//return maxreturn
		return maxreturn;
		
    	}

//METHOD: MAIN	
	
	/* Processes the text file words_alpha.txt
	 * Prints: processing time, key of max anagrams, list of max anagrams, and length of list
	 */
	public static void main(String[] args) {
		
		Anagrams a = new Anagrams (); 
		
		final long startTime = System.nanoTime();
		try {
			a.processFile("words_alpha.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList <Map.Entry <Long, ArrayList <String>>> maxEntries = a.getMaxEntries();
		final long estimatedTime = System.nanoTime() - startTime;
		final double seconds = ((double)estimatedTime/1000000000);
		System.out.println("Time: "+ seconds);
		System.out.println("Key of max anagrams: " + maxEntries.get(0).getKey());
		System.out.println("List of max anagrams: "+ maxEntries.get(0).getValue().toString());
		System.out.println("Length of list of max anagrams: "+ maxEntries.get(0).getValue().size());
	}

	

}
