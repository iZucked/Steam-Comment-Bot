/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class PortValueProviderFactory implements IReferenceValueProviderFactory {
	private static final Logger log = LoggerFactory.getLogger(PortValueProviderFactory.class);

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();

		if (rootObject instanceof LNGScenarioModel) {

			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			if (referenceClass == PortPackage.eINSTANCE.getPort()) {
				return new SimpleReferenceValueProvider(lngScenarioModel.getPortModel(), PortPackage.eINSTANCE.getPortModel_Ports());
			} else if (referenceClass == PortPackage.eINSTANCE.getPortGroup()) {
				return new SimpleReferenceValueProvider(lngScenarioModel.getPortModel(), PortPackage.eINSTANCE.getPortModel_PortGroups());
			} else if (referenceClass == TypesPackage.eINSTANCE.getAPortSet()) {
				return new MergedReferenceValueProvider(lngScenarioModel.getPortModel(), PortPackage.eINSTANCE.getPortModel_Ports(), PortPackage.eINSTANCE.getPortModel_PortGroups(),
						PortPackage.eINSTANCE.getPortModel_SpecialPortGroups());
			} else {
				log.warn("Port value provider factory cannot produce provider for " + referenceClass.getName() + " from " + referenceClass.getEPackage().getNsURI() + ", " + "called with "
						+ owner.getName() + "." + reference.getName());
			}
		}
		return null;
	}
}
