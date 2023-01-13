/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * CSV reader; could easily delegate to spring batch or something.
 * 
 * @author Tom Hinton
 * 
 */
public class FileCSVReader extends CSVReader {

	private @Nullable String fileName;

	public FileCSVReader(final File file) throws IOException {
		this(file, ',');
	}

	/**
	 */
	public FileCSVReader(final File file, final char separator) throws IOException {
		super(separator, new FileInputStream(file));
		this.fileName = file.getCanonicalPath();
	}

	/**
	 * @param inputFileName
	 * @throws IOException
	 */
	public FileCSVReader(final URL url) throws IOException {
		this(url, ',');
	}

	/**
	 * @param inputFileName
	 * @throws IOException
	 */
	public FileCSVReader(final URL url, final char separator) throws IOException {
		super(separator, url.openStream());
		this.fileName = url.toString();
	}
	
	public FileCSVReader(final @NonNull URI uri) throws IOException {
		this(uri, ',');
	}

	/**
	 */
	public FileCSVReader(final @NonNull URI uri, final char separator) throws IOException {
		super(separator, new ExtensibleURIConverterImpl().createInputStream(uri));
		this.fileName = uri.toString();
	}



	@Nullable
	public String getFileName() {
		return fileName;
	}
}
