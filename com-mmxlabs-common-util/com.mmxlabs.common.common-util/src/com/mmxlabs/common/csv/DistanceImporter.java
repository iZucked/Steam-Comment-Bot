/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DistanceImporter {

	protected TableImporter ti;

	public DistanceImporter() {

	}

	public DistanceImporter(final InputStream is) throws IOException {
		this();
		importDistances(is);
	}

	/**
	 * Uses pathname to create a new File instance (see {@link File#File(String)}).
	 * 
	 * @param pathname
	 *            A pathname string.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DistanceImporter(final String pathname) throws FileNotFoundException, IOException {
		this(new FileInputStream(new File(pathname)));
	}

	// Map<String, Map<String, Integer>> distanceMap = new HashMap<String,
	// Map<String, Integer>>();

	public void importDistances(final InputStream is) throws IOException {
		ti = new TableImporter(is);
	}

	// public void putEntry(String A, String B, Integer D) {
	// putEntryInt(A, B, D);
	// putEntryInt(B, A, D);
	// }
	//
	// void putEntryInt(String A, String B, Integer D) {
	// Map<String, Integer> m;
	// if (distanceMap.containsKey(A)) {
	// m = distanceMap.get(A);
	// } else {
	// m = new HashMap<String, Integer>();
	// distanceMap.put(A, m);
	// }
	// m.put(B, D);
	// }

	public Integer getDistance(final String A, final String B) {
		// if (distanceMap.containsKey(A)) {
		// Map<String, Integer> m = distanceMap.get(A);
		// if (m.containsKey(B)) {
		// return m.get(B);
		// }
		// }
		//
		try {
			if (ti.contains(A, B)) {
				return Integer.parseInt(ti.getCell(A, B));
			} else if (ti.contains(B, A)) {
				return Integer.parseInt(ti.getCell(B, A));
			}
		} catch (final NumberFormatException nfe) {

		}

		return Integer.MAX_VALUE;
	}

	public Iterable<String> getKeys() {
		return ti.getRowKeys();
	}
}
