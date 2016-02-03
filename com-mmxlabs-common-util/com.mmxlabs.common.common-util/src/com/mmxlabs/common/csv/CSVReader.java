/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

/**
 * CSV reader; could easily delegate to spring batch or something.
 * 
 * @author Tom Hinton
 * 
 */
public class CSVReader implements Closeable {
	private enum State {
		NORMAL, ESCAPED, AFTER_QUOTE
	}

	private final String[] headerLine;
	private final Map<String, String> originalHeaderLine = new HashMap<String, String>();

	private final Set<String> unusedHeaders = new HashSet<String>();

	private final char separator;

	private BufferedReader reader;
	private int lineNumber = 1;
	private FieldMap currentLine;

	/**
	 * @param inputFileName
	 * @throws IOException
	 */

	public CSVReader(final char separator, @NonNull final InputStream inputStream) throws IOException {
		this.separator = separator;
		reader = new BufferedReader(new InputStreamReader(inputStream));
		headerLine = readLine();
		if (headerLine == null) {
			return;
		}
		for (int i = 0; i < headerLine.length; i++) {
			final String lc = headerLine[i].toLowerCase();
			originalHeaderLine.put(lc, headerLine[i]);
			headerLine[i] = lc;
		}
		unusedHeaders.addAll(originalHeaderLine.keySet());
	}

	public String getCasedColumnName(final String lowerCaseName) {
		return originalHeaderLine.get(lowerCaseName);
	}

	public String[] readLine() throws IOException {
		String line = reader.readLine();
		lineNumber++;
		if (line == null) {
			return null;
		}
		final LinkedList<String> fields = new LinkedList<String>();
		StringBuffer temp = new StringBuffer();
		State state = State.NORMAL;
		boolean firstTry = true;

		// Loop round when a newline occurs within a quoted string (ESCAPED)
		while (firstTry || state == State.ESCAPED) {
			if (firstTry == false) {
				line = reader.readLine();
				temp.append("\n");
				if (line == null) {
					break;
				}
				lineNumber++;
			}
			firstTry = false;
			for (int i = 0; i < line.length(); i++) {
				final char c = line.charAt(i);
				switch (state) {
				case NORMAL:
					if (c == separator) {
						fields.add(temp.toString().trim());
						temp = new StringBuffer();
					} else if (c == '"') {
						state = State.ESCAPED;
					} else {
						temp.append(c);
					}
					break;
				case ESCAPED:
					if (c == '"') {
						state = State.AFTER_QUOTE;
					} else {
						temp.append(c);
					}
					break;
				case AFTER_QUOTE:
					if (c == '"') {
						temp.append(c);
					} else {
						state = State.NORMAL;
						if (c == separator) {
							fields.add(temp.toString().trim());
							temp = new StringBuffer();
						}
					}
					break;
				}
			}
		}
		fields.add(temp.toString().trim());
		return fields.toArray(new String[] {});
	}

	public Set<String> getUnusedHeaders() {
		final HashSet<String> result = new LinkedHashSet<String>();
		for (final String s : unusedHeaders) {
			result.add(getCasedColumnName(s));
		}
		return result;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> readRowFields() throws IOException {
		final Map<String, String> row = new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = -4630946181378550729L;

			@Override
			public String get(final Object arg0) {
				unusedHeaders.remove(arg0);
				return super.get(arg0);
			}
		};
		final String[] fields = readLine();
		if (fields == null) {
			return null;
		}
		for (int i = 0; (i < headerLine.length) && (i < fields.length); i++) {
			final String key = headerLine[i];
			final String value = fields[i];
			if (row.containsKey(key)) {
				if (value != null && !value.isEmpty()) {
					// Only overwrite if value is not null or empty
					row.put(key, value);
				}
			} else {
				row.put(key, value);
			}
		}
		return row;
		// return (currentLine = new FieldMap(row));
	}

	/**
	 * Reads the next row of the CSV file as an IFieldMap.
	 * 
	 * @param ignoreBlank
	 *            Whether or not to ignore blank rows when selecting the next row
	 * @return An IFieldMap representing the contents of the row.
	 * @throws IOException
	 */
	public IFieldMap readRow(final boolean ignoreBlankRows) throws IOException {
		Map<String, String> result;
		while ((result = readRowFields()) != null) {
			for (final String value : result.values()) {
				if (value.equals("") == false || ignoreBlankRows == false) {
					return (currentLine = new FieldMap(result));
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
	}

	public String getLastField() {
		if (currentLine != null) {
			return currentLine.getLastAccessedKey();
		} else {
			return "";
		}
	}

	public char getSeparator() {
		return separator;
	}
}
