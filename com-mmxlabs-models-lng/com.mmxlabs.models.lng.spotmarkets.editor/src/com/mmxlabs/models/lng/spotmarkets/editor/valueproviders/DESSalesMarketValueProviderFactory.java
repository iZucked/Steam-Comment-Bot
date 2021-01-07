/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class DESSalesMarketValueProviderFactory  implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final EClass referenceClass = reference.getEReferenceType();
			if (referenceClass == SpotMarketsPackage.eINSTANCE.getDESSalesMarket()) {
				SpotMarketGroup spg = ((LNGScenarioModel) rootObject).getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket();
				return new SimpleReferenceValueProvider(spg, SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets());
			}
		}
		return null;
	}
}
