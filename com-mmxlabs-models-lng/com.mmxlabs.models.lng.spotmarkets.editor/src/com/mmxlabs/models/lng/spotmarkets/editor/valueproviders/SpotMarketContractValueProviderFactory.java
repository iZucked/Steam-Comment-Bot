/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
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
