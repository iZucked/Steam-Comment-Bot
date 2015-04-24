/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * CSV reader; could easily delegate to spring batch or something.
 * 
 * @author Tom Hinton
 * 
 */
public class FileCSVReader extends CSVReader {

	private final String base;
	private final String filename;

	public FileCSVReader(final File file) throws IOException {
		this(file, ',');
	}

	/**
	 */
	public FileCSVReader(final File file, final char separator) throws IOException {
		this(file.toURI().toString().substring(0, file.toURI().toString().lastIndexOf("/")), file.toURI().toString(), separator);
	}

	public FileCSVReader(final String base, final String inputFileName) throws IOException {
		this(base, inputFileName, ',');
	}

	/**
	 * @param inputFileName
	 * @throws IOException
	 */
	public FileCSVReader(final String base, final String inputFileName, final char separator) throws IOException {
		super(separator, new URL(inputFileName).openStream());
		this.base = base;
		filename = inputFileName;

	}

	public FileCSVReader getAdjacentReader(final String pathFragment) throws IOException {
		return new FileCSVReader(base, base + File.separator + pathFragment, getSeparator());
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return filename;
	}

}
