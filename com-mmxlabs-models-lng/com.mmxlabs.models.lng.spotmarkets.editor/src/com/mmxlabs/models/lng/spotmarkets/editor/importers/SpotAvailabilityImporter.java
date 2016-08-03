/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.lng.pricing.importers.DataIndexImporter;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class SpotAvailabilityImporter extends DefaultClassImporter {

	@Override
	protected boolean shouldImportReference(final EReference reference) {
		return reference != SpotMarketsPackage.Literals.SPOT_AVAILABILITY__CURVE && super.shouldImportReference(reference);
	}

	protected void importReferences(final IFieldMap row, final IMMXImportContext context, final EClass rowClass, final EObject instance) {
		super.importReferences(row, context, rowClass, instance);
		final EReference reference = SpotMarketsPackage.Literals.SPOT_AVAILABILITY__CURVE;
		final String lcrn = reference.getName().toLowerCase();

		final IFieldMap subKeys = row.getSubMap(lcrn + DOT);
		if (!subKeys.isEmpty()) {
			final DataIndexImporter classImporter = new DataIndexImporter();
			classImporter.setParseAsInt(true);
			EObject value = classImporter.importObject(instance, reference.getEReferenceType(), subKeys, context).importedObject;
			if (value != null) {
				instance.eSet(reference, value);
			}
		}
	}

	@Override
	protected void exportReference(EObject object, EReference reference, Map<String, String> result, IMMXExportContext context) {
		// TODO Auto-generated method stub
		super.exportReference(object, reference, result, context);
	}
}
