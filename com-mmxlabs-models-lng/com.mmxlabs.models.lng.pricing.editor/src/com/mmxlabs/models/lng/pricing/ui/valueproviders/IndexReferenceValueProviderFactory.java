/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

/**
 * @since 3.0
 */
public class IndexReferenceValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner,
			final EReference reference, final MMXRootObject rootObject) {
		final PricingModel model = rootObject.getSubModel(PricingModel.class);
		final EClass referenceClass = reference.getEReferenceType();
		
		// TODO; there needs to be more cleverness involved here.
		if (referenceClass == PricingPackage.eINSTANCE.getIndex()) {
			// look at generic data??
			// or look at reference? blah.
			final EClassifier typeArgument = reference.getEGenericType().getETypeArguments().get(0).getERawType();
			if (typeArgument == EcorePackage.eINSTANCE.getEDoubleObject()) {
				return new SimpleReferenceValueProvider(model, PricingPackage.eINSTANCE.getPricingModel_CommodityIndices());
			} else if (typeArgument == EcorePackage.eINSTANCE.getEIntegerObject()) {
				return new SimpleReferenceValueProvider(model, PricingPackage.eINSTANCE.getPricingModel_CharterIndices());
			}
		}
		
		return null;
	}
}
