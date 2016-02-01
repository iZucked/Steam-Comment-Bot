/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * Extended version of {@link DefaultClassImporter} for use with import and export of {@link SpotMarket} and {@link SpotMarketGroup} objects. This are distinct objects but share similar contents. The
 * {@link #getTrueOutputClass(EClass, String)} method needs to be overridden to allow non-direct sub-classes of the input class to be used.
 */
public class SpotMarketImporter extends DefaultClassImporter {

	@Override
	protected EClass getTrueOutputClass(final EClass outputEClass, final String kind) {
		if (kind == null) {
			return outputEClass;
		}
		final EPackage ePackage = outputEClass.getEPackage();
		// find a subclass with the right name
		for (final EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier instanceof EClass) {
				final EClass possibleSubClass = (EClass) classifier;
				// Remove the subclass check that is in the super implementation.
				if ((possibleSubClass.isAbstract() == false) && possibleSubClass.getName().equalsIgnoreCase(kind)) {
					return (EClass) classifier;
				}
			}
		}

		return outputEClass;
	}

	@Override
	protected void importAttributes(final Map<String, String> row, final IMMXImportContext context, final EClass rowClass, final EObject instance) {
		// Set pricing event defaults
		if (instance instanceof SpotMarket) {
			final SpotMarket spotMarket = (SpotMarket) instance;

			SpotType type = null;
			if (row.containsKey("type")) {
				type = SpotType.getByName(row.get("type"));
			}
			if (type != null) {
				switch (type) {
				case DES_PURCHASE:
					spotMarket.setPricingEvent(PricingEvent.START_DISCHARGE);
					break;
				case DES_SALE:
					spotMarket.setPricingEvent(PricingEvent.START_DISCHARGE);
					break;
				case FOB_PURCHASE:
					spotMarket.setPricingEvent(PricingEvent.START_LOAD);
					break;
				case FOB_SALE:
					spotMarket.setPricingEvent(PricingEvent.START_LOAD);
					break;

				}
			}
		}
		super.importAttributes(row, context, rowClass, instance);
	}
}
