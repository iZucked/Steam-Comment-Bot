/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class BaseLegalEntityImporter extends DefaultClassImporter {
	protected boolean shouldImportReference(final EReference reference) {
		return super.shouldImportReference(reference) && reference != CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK
				&& reference != CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK && reference != CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK;
	}

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		return super.shouldExportFeature(feature) && feature != CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK && feature != CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK
				&& feature != CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK;
	}
}
