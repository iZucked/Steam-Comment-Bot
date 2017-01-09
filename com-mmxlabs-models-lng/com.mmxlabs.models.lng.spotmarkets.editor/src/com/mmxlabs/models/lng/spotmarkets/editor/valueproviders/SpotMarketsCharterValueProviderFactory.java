/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class SpotMarketsCharterValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final SpotMarketsModel cm = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
			if (SpotMarketsPackage.eINSTANCE.getCharterInMarket().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
			} else if (SpotMarketsPackage.eINSTANCE.getCharterOutMarket().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterOutMarkets());
			}
		}
		return null;
	}
}
