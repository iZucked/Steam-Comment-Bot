/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

/**
 * This is a restricted case reference value provider which filters the port list depending on the SpotMarket type.
 * 
 * @author Simon Goodall & FM
 * 
 */
public class SpotMarketContractValueProviderFactory implements IReferenceValueProviderFactory {
	
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel) {
			final CommercialModel commercialModel = ((LNGScenarioModel) rootObject).getReferenceModel().getCommercialModel();

			if (reference == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedContracts()) {
				if (owner == SpotMarketsPackage.eINSTANCE.getDESSalesMarket() || owner == SpotMarketsPackage.eINSTANCE.getFOBSalesMarket()) {
					return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts());
				} else {
					return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts());
				}
			}
		}

		return null;
	}
}
