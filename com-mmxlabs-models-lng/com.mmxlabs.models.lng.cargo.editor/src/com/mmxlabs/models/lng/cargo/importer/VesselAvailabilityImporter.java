/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class VesselAvailabilityImporter extends DefaultClassImporter {

	protected boolean shouldImportReference(final EReference reference) {
		return reference != CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT && super.shouldImportReference(reference);
	}

	protected boolean shouldExportFeature(final EReference reference) {
		return reference != CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT && super.shouldExportFeature(reference);
	}
	
	protected boolean shouldFlattenReference(final EReference reference) {
		return reference != CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT && super.shouldFlattenReference(reference);
	}
}