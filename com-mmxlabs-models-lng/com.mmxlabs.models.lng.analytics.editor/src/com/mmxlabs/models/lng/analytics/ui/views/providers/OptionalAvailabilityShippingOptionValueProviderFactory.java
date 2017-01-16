/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class OptionalAvailabilityShippingOptionValueProviderFactory implements IReferenceValueProviderFactory {

	public OptionalAvailabilityShippingOptionValueProviderFactory() {
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final PortModel pm = lngScenarioModel.getReferenceModel().getPortModel();
			if (pm == null) {
				return null;
			}
			return new OptionalAvailabilityShippingOptionPortValueProvider(pm, PortPackage.eINSTANCE.getPortModel_Ports());
		} else {
			return null;
		}
	}
}
