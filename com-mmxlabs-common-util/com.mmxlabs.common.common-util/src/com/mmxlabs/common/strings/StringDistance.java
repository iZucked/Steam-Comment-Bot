/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.strings;

public interface StringDistance {

	/**
	 * Returns the amount of operations it takes to get from the source string to the target string
	 * @param source The source string, e.g. relevant
	 * @param target The target string, e.g. elephant
	 * @return The distance between the two strings, e.g. 3 with the example inputs above
	 */
	int distance(String source, String target);
	
	/**
	 * Currently returns {@link DamerauLevenshteinDistance}
	 * @return
	 */
	static StringDistance defaultInstance() {
		return new DamerauLevenshteinDistance();
	}
}
