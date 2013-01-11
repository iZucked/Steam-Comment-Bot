/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class BaseFuelValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner,
			EReference reference, MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();
		if (referenceClass == FleetPackage.eINSTANCE.getBaseFuel() || 
				referenceClass == TypesPackage.eINSTANCE.getABaseFuel()) {
			return new SimpleReferenceValueProvider(rootObject.getSubModel(FleetModel.class), FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
		}
		return null;
	}

}
