/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.IOException;
import java.io.Writer;

/**
 * Simple class to write CSV-style output to a particular output stream.<br>
 * Usage:<br>
 * 
 * CSVWriter w = new CSVWriter(output); <br>
 * w.addValue("first field"); <br>
 * w.addValue("second field"); <br>
 * w.endRow();
 * 
 * 
 * @author Simon McGregor
 * 
 */
public class CSVWriter {
	private final char separator; // this is the character which appears between fields
	private final char delimiter; // this is the character which appears either end of a field
	private final String escapedDelimiter; // this is the string with which the delimiter is replaced inside a field
	private final String newLine = "\n"; // this is the character which terminates a row

	private final String separatorString; // String version of the separator for convenience
	private final String delimiterString; // String version of the delimiter for convenience

	private final Writer writer; // the stream to which output is written

	private boolean rowStarted = false; // track whether a row has been started yet

	public CSVWriter(final Writer w, final char sep, final char delim, final String escaped) {
		separator = sep;
		separatorString = "" + sep;
		delimiter = delim;
		delimiterString = "" + delim;
		escapedDelimiter = escaped;
		writer = w;
	}

	public CSVWriter(final Writer w, final char sep, final char delim) {
		this(w, sep, delim, "\\" + delim);
	}

	public CSVWriter(final Writer w, final char sep) {
		this(w, sep, '"');
	}

	public CSVWriter(final Writer w) {
		this(w, '\t');
	}

	public void addValue(final String value) throws IOException {
		if (rowStarted) {
			writer.write(separator);
		}
		writer.write(escape(value));
		rowStarted = true;
	}

	public void endRow() throws IOException {
		writer.write(newLine);
		rowStarted = false;
	}

	private String escape(final String value) {
		if (value.contains(delimiterString) || value.contains(separatorString) || value.contains(newLine)) {
			return delimiterString + value.replace("" + delimiter, escapedDelimiter) + delimiterString;
		}
		return value;
	}

}
