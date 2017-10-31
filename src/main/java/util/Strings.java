package util;

public class Strings {
	
	/**
	 * Check if a String is null or empty.
	 * 
	 * @param s
	 *            String to check
	 * @return true if the String is null or empty, false otherwise.
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	/**
	 * Capitalise a word. First letter gets capitalised and the rest letters are
	 * converted to lower case.
	 * 
	 * @param word
	 *            to capitalise
	 * @return capitalised word
	 */
	public static String capitalise(String word) {

		if (isNullOrEmpty(word)) { return word; }

		char firstChar = Character.toUpperCase(word.charAt(0));
		String restOfTheLetter = word.substring(1).toLowerCase();

		return firstChar + restOfTheLetter;
	}
}
