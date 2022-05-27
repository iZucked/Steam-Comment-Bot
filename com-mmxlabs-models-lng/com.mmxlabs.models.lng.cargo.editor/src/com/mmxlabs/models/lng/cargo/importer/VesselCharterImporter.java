/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class VesselCharterImporter extends DefaultClassImporter {

	@Override
	protected boolean shouldImportReference(final EReference reference) {
		return reference != CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT && super.shouldImportReference(reference);
	}

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature reference) {
		return reference != CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT && super.shouldExportFeature(reference);
	}

	@Override
	protected boolean shouldFlattenReference(final EReference reference) {
		return reference != CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT && super.shouldFlattenReference(reference);
	}
}