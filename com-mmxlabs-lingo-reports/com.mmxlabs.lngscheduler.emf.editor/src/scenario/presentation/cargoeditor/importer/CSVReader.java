/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * CSV reader; could easily delegate to spring batch or something.
 * 
 * @author Tom Hinton
 * 
 */
public class CSVReader {
	private enum State {
		NORMAL, ESCAPED, AFTER_QUOTE
	}

	private BufferedReader reader;
	private final String[] headerLine;
	private final Map<String, String> originalHeaderLine = new HashMap<String, String>();
	
	/**
	 * @param inputFileName
	 * @throws IOException
	 */
	public CSVReader(final String inputFileName) throws IOException {
		reader = new BufferedReader(new FileReader(inputFileName));
		headerLine = readLine();
		for (int i = 0; i < headerLine.length; i++) {
			final String lc = headerLine[i].toLowerCase();
			originalHeaderLine.put(lc, headerLine[i]);
			headerLine[i] = lc;
		}
	}
	
	public String getCasedColumnName(final String lowerCaseName) {
		return originalHeaderLine.get(lowerCaseName);
	}

	private String[] readLine() throws IOException {
		final String line = reader.readLine();
		if (line == null) return null;
		final LinkedList<String> fields = new LinkedList<String>();
		StringBuffer temp = new StringBuffer();
		State state = State.NORMAL;
		for (int i = 0; i < line.length(); i++) {
			final char c = line.charAt(i);
			switch (state) {
			case NORMAL:
				if (c == ',') {
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
				}
				break;
			}
		}
		fields.add(temp.toString().trim());
		return fields.toArray(new String[] {});
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> readRow() throws IOException {
		final Map<String, String> row = new HashMap<String, String>();
		final String[] fields = readLine();
		if (fields == null)
			return null;
		for (int i = 0; i < headerLine.length && i < fields.length; i++) {
			row.put(headerLine[i], fields[i]);
		}
		return row;
	}

}
