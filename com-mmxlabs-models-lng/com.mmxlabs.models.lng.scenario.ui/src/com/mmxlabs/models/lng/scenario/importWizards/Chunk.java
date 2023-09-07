/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.jface.preference.FileFieldEditor;

import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

abstract class Chunk {
	private static final int BUFFER = 2048; // For unzipping
	
	final Map<String, String> keys;
	final Map<String, String> friendlyNames = new HashMap<>();

	final Map<String, FileFieldEditor> editors = new HashMap<>();

	Chunk() {
		super();
		this.keys = new HashMap<>();
	}

	void setFromDirectory(final File directory) {
		if (directory == null) return;
		if (isZip(directory.getAbsolutePath())) {
			setFromZip(directory.getAbsolutePath());
		}
		else if (directory.isDirectory()) {
			for (final Map.Entry<String, String> entry : friendlyNames.entrySet()) {
				final String k = entry.getKey();
				final String v = entry.getValue();
				final File sub = new File(directory, v + ".csv");
				if (sub.exists()) {
					try {
						final String str = sub.getCanonicalPath();
						editors.get(k).setStringValue(str);
						keys.put(k, str); // CME?
					} catch (final IOException e) {
					}
				}
			}
		}
	}

	void setFromZip(final String fn) {
		setFromDirectory(unzip(fn));
	}

	private File unzip(String fn) {
		try (ZipFile zip = new ZipFile(fn)) {
			// Unzips to new temp directory
			File destDir = new File(zip.getName().replace(".", "") + "_lingo_unzipped");
			while (destDir.exists()) {
				destDir = new File(destDir.getName()+"a");
			}
			destDir.mkdirs();
			destDir.deleteOnExit();
			Enumeration<? extends ZipEntry> entries = zip.entries();
			byte[] buffer = new byte[BUFFER];
			BufferedInputStream is = null;
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String fileName = entry.getName();
				// Directories shouldn't exist, only CSVs
				if (!fileName.substring(fileName.length()-4, fileName.length()).equals(".csv")) {
					continue;
				}
				File newFile = new File(destDir + File.separator + fileName);
				newFile.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(newFile);
				is = new BufferedInputStream(zip.getInputStream(entry));
				int len;
				while ((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close(); 
			}
			return destDir;
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	private static boolean isZip(String fn) {
		try {
			ZipFile zip = new ZipFile(fn);
			zip.close();
		} catch (ZipException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}

class SubModelChunk extends Chunk {
	final ISubmodelImporter importer;

	 SubModelChunk(final ISubmodelImporter importer) {
		super();
		this.importer = importer;
	}
}

class ExtraModelChunk extends Chunk {
	final IExtraModelImporter importer;

	ExtraModelChunk(final IExtraModelImporter importer) {
		super();
		this.importer = importer;
	}
}
