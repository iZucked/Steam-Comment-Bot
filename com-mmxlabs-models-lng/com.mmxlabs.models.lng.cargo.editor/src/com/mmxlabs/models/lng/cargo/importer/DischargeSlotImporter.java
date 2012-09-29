/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 2.0
 */
public class DischargeSlotImporter extends DefaultClassImporter {
	@Override
	protected boolean shouldImportReference(final EReference reference) {
		if (reference == CargoPackage.eINSTANCE.getDischargeSlot_Cargo())
			return false;
		return super.shouldImportReference(reference);
	}

	@Override
	protected boolean shouldExportFeature(EStructuralFeature feature) {
		if (feature == CargoPackage.eINSTANCE.getDischargeSlot_Cargo())
			return false;
		return super.shouldExportFeature(feature);
	}
}
