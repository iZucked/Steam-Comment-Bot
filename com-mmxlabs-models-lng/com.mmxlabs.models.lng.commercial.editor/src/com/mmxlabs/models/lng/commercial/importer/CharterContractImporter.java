/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class CharterContractImporter extends DefaultClassImporter {

	protected boolean shouldImportReference(final EReference reference) {
		return reference != CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT && super.shouldImportReference(reference);
	}

	protected boolean shouldExportFeature(final EReference reference) {
		return reference != CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT && super.shouldExportFeature(reference);
	}
	
	protected boolean shouldFlattenReference(final EReference reference) {
		return reference != CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CHARTER_CONTRACT && super.shouldFlattenReference(reference);
	}
}