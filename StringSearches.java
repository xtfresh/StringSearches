import java.util.Arrays;


public class StringSearches {

	/**
	 * Return a table for use with Boyer-Moore.
	 * 
	 * map[c] = the length - 1 - last index of c in the needle
	 * map[c] = the length if c doesn't appear in the needle
	 * 
	 * the map should have an entry for every character, 0 to Character.MAX_VALUE
	 */
	public static int[] buildCharTable(String needle) {
		int[] map = new int[Character.MAX_VALUE + 1];
		for (int i = 0; i<map.length;i++){
			map[i] = needle.length();
		}
		for(int i = 0; i < needle.length(); i++){
			map[needle.charAt(i)] = Math.max(needle.length() - needle.lastIndexOf(needle.charAt(i)) - 1, 1);
		}
		return map;
	}

	/**
	 * Run Boyer-Moore on the given strings, looking for needle in haystack.
	 * Return an array of the indices of the occurrence of the needle in the
	 * haystack. 
	 * 
	 * If there are matches that start at index 4, 7, and 9 in the
	 * haystack, return an array containing only 4, 7, and 9. If there are no
	 * matches return an empty array, new int[0]
	 * 
	 * Running time matters, you will not get full credit if it is not
	 * implemented correctly
	 * 
	 * 
	 */
	public static int[] boyerMoore(String needle, String haystack) {
		if(needle.length() ==0 || haystack.length() == 0 || needle.length() > haystack.length())
			return null;
		
		int[] charTable = buildCharTable(needle);
		int[] indexes = new int[haystack.length()/needle.length()];
		int lettersMatched = 0;
		int matched = 0;
		int j = needle.length()-1;
		
		for (int i = needle.length()-1; i >= 0;){
			if(j > haystack.length()-1)
				break;
			if(needle.charAt(i)==haystack.charAt(j)){
				lettersMatched++;
				if(lettersMatched == needle.length()){
					indexes[matched] = j;
					matched++;
					i = needle.length()-1;
					j += needle.length()+1;
					lettersMatched = 0;
				}
				j--;
				i--;
				
			}
			else{
				j += needle.length()-Math.min(i, 1 + charTable[j]);
				i = needle.length()-1;
				lettersMatched = 0;
			}
				
				
		}
		
		return Arrays.copyOf(indexes,matched);
	}

	/**
	 * Return a table for use with KMP. In this table, table[i] is the length of
	 * the longest possible prefix that matches a proper suffix in the string
	 * needle.substring(0, i)
	 */
	public static int[] buildTable(String needle) {
		 int[] table = new int[needle.length()];
		 int pos = 2;
		 int cnd = 0;
		 
		 table[0] = -1; 
		 table[1] = 0; 
		 while(pos < needle.length()) {
			 if(needle.charAt(pos-1) == needle.charAt(cnd)) {
				 table[pos] = cnd;
				 cnd += 1;
				 pos += 1;
			 }else if(cnd > 0)
				 	cnd = table[cnd];
			 	 	else{
					 table[pos] = 0;
					 pos += 1;
			 	 	}
			 }
		 return table;
	}

	/**
	 * Run Knuth-Morris-Pratt on the given strings, looking for needle in
	 * haystack. Return an array of the indices of the occurrence of the needle
	 * in the haystack.
	 * 
	 * If there are matches that start at index 4, 7, and 9 in the
	 * haystack, return an array containing only 4, 7, and 9. If there are no
	 * matches return an empty array, new int[0]
	 */
	public static int[] kmp(String needle, String haystack) {
		if(needle.length() == 0 || haystack.length() == 0 || needle.length() > haystack.length())
			return null;
		
		int[] table = buildTable(needle);
		int[] indexes = new int[haystack.length()/needle.length()];
		int matched = 0;
		int j = 0;
		
		for (int i = 0; i < haystack.length();){
			if(needle.charAt(j) == haystack.charAt(i)){
				j++;
				i++;
				if(j == needle.length()){
					indexes[matched] = i-j;
					matched++;
					j = 0;
				}
			}else 
				if(j > 0){
					j = table[j];
				}else
					i++;
				
		}
			
		return Arrays.copyOf(indexes,matched);
	}

	// This is the base you should use, don't change it
	public static final int BASE = 1332;

	/**
	 * Given the hash for a string, return the hash for that string removing
	 * oldChar from the front and adding newChar to the end.
	 * 
	 * Power is BASE raised to the power of the length of the needle
	 */
    public static int updateHash(int oldHash, int power, char newChar,char oldChar) {
    	int updatedHash = (oldHash * BASE) - (((int)oldChar) * power) + (int)newChar;
    	return updatedHash;
    }

	/**
	* Hash the given string, using the formula given in the homework
	*/
	public static int hash(String s) {
		int count = 0;
		int temp = 1;
	    for(int i = s.length() - 1; i >= 0; i--){
			count += ((int)s.charAt(i)) * temp;
			temp *= BASE;
		}
	    return count;
	}
	
	/**
	* Run Rabin-Karp on the given strings, looking for needle in haystack.
	* Return an array of the indices of the occurrence of the needle in the
	* haystack.
	*
	* If there are matches that start at index 4, 7, and 9 in the
	* haystack, return an array containing only 4, 7, and 9. If there are no
	* matches return an empty array, new int[0]
	*/
	public static int[] rabinKarp(String needle, String haystack) {
		if(needle.length() == 0 || haystack.length() == 0 || needle.length() > haystack.length())
			return null;
		int[] index = new int[haystack.length() / needle.length()];
		int hashCompare = hash(needle);
		int tempHash = hash(haystack.substring(0, needle.length()));
		int matched = 0;
		int pwr = 1;
		
		for(int i = 0; i < needle.length(); i++){
			pwr *= BASE;
		}
		
		for(int i = 0; i <= haystack.length() - needle.length() + 1;){
			if(hashCompare == tempHash){
			    if(needle.equals(haystack.substring(i, i + needle.length()))){
			        index[matched] = i;
			        matched++;
			        i += needle.length();
			        if(i + needle.length() <= haystack.length()){
			            tempHash = hash(haystack.substring(i, i + needle.length()));			            
			        }
			        else break;
			    }
			}
			if(i + needle.length() < haystack.length()){
			    tempHash = updateHash(tempHash, pwr, haystack.charAt(i + needle.length()), haystack.charAt(i));
			    i++;
			}else 
				break;
		}
		return Arrays.copyOf(index, matched);
	}

}
