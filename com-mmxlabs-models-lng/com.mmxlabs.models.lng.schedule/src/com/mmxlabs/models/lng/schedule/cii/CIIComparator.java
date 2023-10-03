/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.cii;

import java.util.Comparator;
import java.util.List;

public class CIIComparator implements Comparator<String> {

	private static final String GRADES = "ABCDE";

	@Override
	public int compare(String o1, String o2) {
		if (!GRADES.contains(o1) || !GRADES.contains(o2)) {
			throw new IllegalArgumentException("Incorrect grades to be compared");
		}
		return o1.compareTo(o2);
	}

	public static int compareGrades(String first, String second) {
		return new CIIComparator().compare(first, second);
	}
}
