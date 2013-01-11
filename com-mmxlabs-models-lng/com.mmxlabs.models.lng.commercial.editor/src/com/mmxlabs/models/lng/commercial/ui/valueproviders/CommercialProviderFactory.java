/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedMultiModelReferenceValueProvider;

public class CommercialProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;

		if (types.getAContract().isSuperTypeOf(referenceClass) || types.getASalesContract().isSuperTypeOf(referenceClass) || types.getAPurchaseContract().isSuperTypeOf(referenceClass)
				|| types.getALegalEntity().isSuperTypeOf(referenceClass)) {
			return new MergedMultiModelReferenceValueProvider(rootObject, referenceClass);
		}

		return null;
	}
}
