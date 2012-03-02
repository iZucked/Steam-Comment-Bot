/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Provides some convenience methods for the import / export process
 * 
 * @author Tom Hinton
 * 
 */
public class ImporterUtil {
	static final ImporterUtil INSTANCE = new ImporterUtil();

	private ImporterUtil() {
	}

	public static final ImporterUtil getInstance() {
		return INSTANCE;
	}

	public Map<String, Collection<Map<String, String>>> exportEObjects(final EClass exportClass, final List<? extends EObject> objectsToExport) {

		final EObjectImporter exporter = EObjectImporterFactory.getInstance().getImporter(exportClass);
		final Map<String, Collection<Map<String, String>>> result = exporter.exportObjects(objectsToExport);

		return result;
	}

	/**
	 * Write out the given maps
	 * 
	 * @param value
	 */
	public void writeObjects(final String filename, final Collection<Map<String, String>> value) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));

			final LinkedHashSet<String> keys = new LinkedHashSet<String>();
			for (final Map<String, String> map : value) {
				keys.addAll(map.keySet());
			}

			boolean comma = false;
			for (final String col : keys) {
				bw.write((comma ? ", " : "") + col);
				comma = true;
			}

			// bw.newLine();

			for (final Map<String, String> row : value) {
				bw.newLine();
				comma = false;
				for (final String col : keys) {
					final String v = row.get(col);
					final String ve = v == null ? "" : (v.contains(",") ? "\"" + v.replace("\"", "\"\"") + "\"" : v);
					bw.write((comma ? "," : "") + ve);
					comma = true;
				}
			}

			bw.flush();
		} catch (final IOException ex) {
			return;
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					
				}
			}
		}
	}
}
