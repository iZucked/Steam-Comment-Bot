/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class MullImporter extends DefaultClassImporter {

	public ImportResults importEntityRow(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final EClass rowClass = getTrueOutputClass(eClass, "MullEntityRow");
		try {
			final EObject instance = rowClass.getEPackage().getEFactoryInstance().create(rowClass);
			assert instance != null;
			final ImportResults results = new ImportResults(instance);
			importAttributes(row, context, rowClass, instance);
			final IFieldMap iFieldMap = row instanceof IFieldMap ? (IFieldMap) row : new FieldMap(row);
			importReferences(iFieldMap, context, rowClass, instance);
			return results;
		} catch (IllegalArgumentException illegal) {
			context.addProblem(context.createProblem("Ill-formed MULL entity profile row", true, true, true));
			return new ImportResults(null);
		}
	}
}
