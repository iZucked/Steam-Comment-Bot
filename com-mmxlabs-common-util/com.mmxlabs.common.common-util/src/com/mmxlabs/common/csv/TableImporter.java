/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableImporter {
	private final List<String> rowKeys, colKeys;

	private final Map<String, Map<String, String>> contents;

	public TableImporter(final InputStream is) throws IOException {
		rowKeys = new ArrayList<String>();
		colKeys = new ArrayList<String>();
		contents = new HashMap<String, Map<String, String>>();
		load(is);
	}

	protected void load(final InputStream is) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));

			String line = null;
			boolean firstRow = true;
			while ((line = br.readLine()) != null) {
				final String[] cells = line.split(",");
				if (firstRow) {
					for (int i = 1; i < cells.length; i++) {
						colKeys.add(cells[i].trim());
					}
					firstRow = false;
				} else {
					rowKeys.add(cells[0].trim());
					final HashMap<String, String> map = new HashMap<String, String>();
					for (int i = 1; i < cells.length; i++) {
						final String str = cells[i].trim();
						if (!str.isEmpty()) {
							map.put(colKeys.get(i - 1), str);
						}
					}
					contents.put(cells[0].trim(), map);
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public String getCell(final String row, final String column) {
		return contents.get(row).get(column);
	}

	public Iterable<String> getRowKeys() {
		return Collections.unmodifiableList(rowKeys);
	}

	public Iterable<String> getColumnKeys() {
		return Collections.unmodifiableList(colKeys);
	}

	public boolean contains(final String a, final String b) {
		return contents.containsKey(a) && contents.get(a).containsKey(b);
	}
}
