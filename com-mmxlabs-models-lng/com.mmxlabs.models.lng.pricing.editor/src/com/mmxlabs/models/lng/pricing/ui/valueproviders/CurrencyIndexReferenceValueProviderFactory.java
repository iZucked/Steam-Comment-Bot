/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CurrencyIndexReferenceValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final PricingModel model = ScenarioModelUtil.getPricingModel(lngScenarioModel);
			final EClass referenceClass = reference.getEReferenceType();

			if (referenceClass == PricingPackage.eINSTANCE.getCurrencyCurve()) {
				return new SimpleReferenceValueProvider(model, PricingPackage.eINSTANCE.getPricingModel_CurrencyCurves());
			}
		}

		return null;
	}
}
