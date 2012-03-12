/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CommercialProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner,
			final EReference reference, final MMXRootObject rootObject) {
		final CommercialModel model = rootObject.getSubModel(CommercialModel.class);
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;
		final CommercialPackage commercial = CommercialPackage.eINSTANCE;
		
		if (commercial.getPurchaseContract().isSuperTypeOf(referenceClass)) {
			return new SimpleReferenceValueProvider(model, commercial.getCommercialModel_PurchaseContracts());
		} else if (commercial.getSalesContract().isSuperTypeOf(referenceClass)) {
				return new SimpleReferenceValueProvider(model, commercial.getCommercialModel_SalesContracts());
		} else if (types.getALegalEntity().isSuperTypeOf(referenceClass)) {
			return new SimpleReferenceValueProvider(model, commercial.getCommercialModel_Entities());
		} else if (types.getAContract().isSuperTypeOf(referenceClass)) {
			return new MergedReferenceValueProvider(model, commercial.getCommercialModel_PurchaseContracts(), commercial.getCommercialModel_SalesContracts());
		}
		
		return null;
	}
}
