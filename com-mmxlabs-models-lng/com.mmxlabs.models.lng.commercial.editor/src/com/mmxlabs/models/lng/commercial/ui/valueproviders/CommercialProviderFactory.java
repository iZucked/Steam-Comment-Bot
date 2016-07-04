/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialModel;
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
			final CommercialModel commercialModel = ((LNGScenarioModel) rootObject).getReferenceModel().getCommercialModel();

			if (CommercialPackage.eINSTANCE.getPurchaseContract().isSuperTypeOf(referenceClass)) {

				return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts());
			} else if (CommercialPackage.eINSTANCE.getSalesContract().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts());
			} else if (CommercialPackage.eINSTANCE.getContract().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts(),
						CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts());
			} else if (CommercialPackage.eINSTANCE.getBaseLegalEntity().isSuperTypeOf(referenceClass)) {
				return new MergedReferenceValueProvider(commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_Entities());

			}
		}

		return null;
	}
}
