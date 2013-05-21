/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

public class CommercialProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();

		if (rootObject instanceof LNGScenarioModel) {
			if (CommercialPackage.eINSTANCE.getPurchaseContract().isSuperTypeOf(referenceClass)) {

				return new MergedReferenceValueProvider(((LNGScenarioModel) rootObject).getCommercialModel(), CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts());
			} else if (CommercialPackage.eINSTANCE.getSalesContract().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(((LNGScenarioModel) rootObject).getCommercialModel(), CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts());
			} else if (CommercialPackage.eINSTANCE.getContract().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(((LNGScenarioModel) rootObject).getCommercialModel(), CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts(),
						CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts());
			} else if (CommercialPackage.eINSTANCE.getLegalEntity().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(((LNGScenarioModel) rootObject).getCommercialModel(), CommercialPackage.eINSTANCE.getCommercialModel_Entities());

			}
		}

		return null;
	}
}
