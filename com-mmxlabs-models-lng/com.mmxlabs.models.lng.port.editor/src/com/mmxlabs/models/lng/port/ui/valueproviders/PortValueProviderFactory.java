/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class PortValueProviderFactory implements IReferenceValueProviderFactory {
	private static final Logger log = LoggerFactory
			.getLogger(PortValueProviderFactory.class);

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner,EReference reference, MMXRootObject rootObject) {
		final EClass referenceClass = reference.getEReferenceType();
		
		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		if (portModel != null) {
			for (final PortCapability capability : PortCapability.values()) {
				boolean found = false;
				for (final CapabilityGroup g : portModel.getSpecialPortGroups()) {
					if (g.getCapability().equals(capability)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					final CapabilityGroup g = PortFactory.eINSTANCE.createCapabilityGroup();
					g.setName("All " + capability.getName() + " Ports");
					g.setCapability(capability);
					portModel.getSpecialPortGroups().add(g);
				}
			}
		}
		
		if (referenceClass == PortPackage.eINSTANCE.getPort() || referenceClass == TypesPackage.eINSTANCE.getAPort()) {
			return new SimpleReferenceValueProvider(rootObject.getSubModel(PortModel.class), PortPackage.eINSTANCE.getPortModel_Ports());
		} else if (referenceClass == PortPackage.eINSTANCE.getPortGroup()) {
			return new SimpleReferenceValueProvider(rootObject.getSubModel(PortModel.class), PortPackage.eINSTANCE.getPortModel_PortGroups());
		} else if (referenceClass == TypesPackage.eINSTANCE.getAPortSet()) {
			return new MergedReferenceValueProvider(rootObject.getSubModel(PortModel.class), PortPackage.eINSTANCE.getPortModel_Ports(), PortPackage.eINSTANCE.getPortModel_PortGroups(), PortPackage.eINSTANCE.getPortModel_SpecialPortGroups());
		} else {
			log.warn("Port value provider factory cannot produce provider for " + referenceClass.getName() + " from " + referenceClass.getEPackage().getNsURI() + ", "
					+ "called with " + owner.getName() + "." + reference.getName());
			return null;
		}
	}
}
