/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * Extended version of {@link DefaultClassImporter} for use with import and export of {@link SpotMarket} and {@link SpotMarketGroup} objects. This are distinct objects but share similar contents. The
 * {@link #getTrueOutputClass(EClass, String)} method needs to be overridden to allow non-direct sub-classes of the input class to be used.
 * @since 2.0
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
}
