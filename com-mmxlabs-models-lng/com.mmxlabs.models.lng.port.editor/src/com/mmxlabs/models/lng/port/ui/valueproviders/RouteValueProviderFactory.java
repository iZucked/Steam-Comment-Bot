/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class RouteValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner,
			final EReference reference, final MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();
		if (TypesPackage.eINSTANCE.getARoute().isSuperTypeOf(referenceClass)) {
			return new SimpleReferenceValueProvider(rootObject.getSubModel(PortModel.class), PortPackage.eINSTANCE.getPortModel_Routes());
		}
		return null;
	}

}
