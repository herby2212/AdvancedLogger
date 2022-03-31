package de.Herbystar.AVL;

public class Utils {
	
	/**
	 * Formats raw modlist string for further processing.
	 */
	public static String getReadableString(String input) {
		for(char c : input.toCharArray()) {
			if((c < ' ') || (c > '�')) {
				input = input.replace(c + "", " ");
			}
		}
		return input.trim();
	}

}
