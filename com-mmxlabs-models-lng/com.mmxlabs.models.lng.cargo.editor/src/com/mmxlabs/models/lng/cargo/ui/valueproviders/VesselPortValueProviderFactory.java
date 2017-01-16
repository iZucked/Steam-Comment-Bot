/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * This is a restricted case reference value provider which filters the port list on vessel start/end to those compatible with the vessel.
 * 
 * @author hinton
 * 
 */
public class VesselPortValueProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegate;

	public VesselPortValueProviderFactory() {
		this.delegate = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (delegate == null)
			return null;
		final IReferenceValueProvider delegateFactory = delegate.createReferenceValueProvider(owner, reference, rootObject);
		if (reference == CargoPackage.eINSTANCE.getVesselAvailability_StartAt() || reference == CargoPackage.eINSTANCE.getVesselAvailability_EndAt()) {
			return new IReferenceValueProvider() {
				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {

					// ??
					if (changedFeature == CargoPackage.eINSTANCE.getSlot_Contract()) {
						return true;
					} else if (changedFeature == FleetPackage.eINSTANCE.getVessel_InaccessiblePorts()) {
						return true;
					}

					return delegateFactory.updateOnChangeToFeature(changedFeature);
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					return delegateFactory.getNotifiers(referer, feature, referenceValue);
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					return delegateFactory.getName(referer, feature, referenceValue);
				}

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					final List<Pair<String, EObject>> delegateValue = delegateFactory.getAllowedValues(target, field);

					// I assume delegateValue is list of all ports??

					if (target instanceof VesselAvailability) {
						final VesselAvailability va = (VesselAvailability) target;
						final Vessel vessel = va.getVessel();
						if (vessel != null) {
							final EList<APortSet<Port>> ips = vessel.getInaccessiblePorts();
							final ArrayList<Pair<String, EObject>> filteredList = new ArrayList<Pair<String, EObject>>();
							for (final Pair<String, EObject> p : delegateValue) {
								// search recursively
								if (!contains(ips, p.getSecond())) {
									filteredList.add(p);
								}
							}
							return filteredList;
						}
					}
					return delegateValue;
				}

				@Override
				public void dispose() {
					delegateFactory.dispose();
				}
			};
		} else {
			return delegateFactory;
		}
	}

	private boolean contains(final EList<APortSet<Port>> ips, final EObject value) {

		for (final APortSet<Port> aPort : ips) {
			if (aPort instanceof Port) {
				final Port port = (Port) aPort;
				if (port == value)
					return true;
			} else if (aPort instanceof PortGroup) {
				final PortGroup portGroup = (PortGroup) aPort;
				return contains(portGroup.getContents(), value);
			}
		}
		return false;
	}

}
