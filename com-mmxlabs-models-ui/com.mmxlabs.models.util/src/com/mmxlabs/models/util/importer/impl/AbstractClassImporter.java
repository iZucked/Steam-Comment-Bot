package com.mmxlabs.models.util.importer.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * @since 8.0
 */
public abstract class AbstractClassImporter implements IClassImporter {

	@Override
	public Collection<EObject> importObjects(final EClass targetClass, final CSVReader reader, final IImportContext context) {
		final List<EObject> result = new LinkedList<EObject>();

		Map<String, String> row;
		try {
			context.pushReader(reader);
			while (null != (row = reader.readRow(true))) {
				ImportResults imported = importObject(null, targetClass, row, context);
				//result.add(imported.importedObject);
				result.addAll(imported.getCreatedObjects());
			}
		} catch (final IOException e) {
			context.addProblem(context.createProblem("IO Error " + e.getMessage(), true, true, false));
		} finally {
			context.popReader();
		}

		return result;
	}

}
