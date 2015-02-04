/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class BaseFuelValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel) {

			final EClass referenceClass = reference.getEReferenceType();
			if (referenceClass == FleetPackage.eINSTANCE.getBaseFuel()) {
				return new SimpleReferenceValueProvider(((LNGScenarioModel)rootObject).getFleetModel(), FleetPackage.eINSTANCE.getFleetModel_BaseFuels());
			}
		}
		return null;
	}

}
