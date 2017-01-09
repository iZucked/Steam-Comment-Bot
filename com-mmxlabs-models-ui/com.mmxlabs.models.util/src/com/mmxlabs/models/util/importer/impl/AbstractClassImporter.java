/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 */
public abstract class AbstractClassImporter implements IClassImporter {

	@Override
	public Collection<EObject> importObjects(@NonNull final EClass targetClass, @NonNull final CSVReader reader, @NonNull final IMMXImportContext context) {
		final List<EObject> result = new LinkedList<EObject>();

		Map<String, String> row;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				final ImportResults imported = importObject(null, targetClass, row, context);
				result.addAll(imported.getCreatedObjects());
			}
		} catch (final IOException e) {
			context.addProblem(context.createProblem("IO Error " + e.getMessage(), true, true, false));
		} finally {
			context.popReader();
		}

		assert result != null;
		return result;
	}

}
