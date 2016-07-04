/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class RouteValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);

			final EClass referenceClass = reference.getEReferenceType();
			if (PortPackage.eINSTANCE.getRoute().isSuperTypeOf(referenceClass)) {
				return new SimpleReferenceValueProvider(portModel, PortPackage.eINSTANCE.getPortModel_Routes());
			}
		}
		return null;
	}

}
