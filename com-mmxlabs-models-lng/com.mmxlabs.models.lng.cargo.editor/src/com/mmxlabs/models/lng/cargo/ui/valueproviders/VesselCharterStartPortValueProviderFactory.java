/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class VesselCharterStartPortValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGReferenceModel referenceModel = lngScenarioModel.getReferenceModel();
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			final PortModel portModel = referenceModel.getPortModel();

			return new SimpleReferenceValueProvider(portModel, PortPackage.Literals.PORT_MODEL__PORTS) {

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
					return getRestrictedPortsList(cargoModel, portModel, target);
				}

				private List<Pair<String, EObject>> getAllPortsList(final CargoModel cargoModel, final PortModel portModel, final EObject target) {
					return getSortedNames(portModel.getPorts(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				}

				private List<Pair<String, EObject>> getRestrictedPortsList(final CargoModel cargoModel, final PortModel portModel, final EObject target) {
					Port currentValue = null;
					Vessel currentVessel = null;
					if (target instanceof VesselCharter) {
						final VesselCharter vesselCharter = (VesselCharter) target;
						currentValue = vesselCharter.getStartAt();
						currentVessel = vesselCharter.getVessel();
					}

					// make a list of admissible vessels
					final Set<Port> admissible = new HashSet<>();
					admissible.addAll(portModel.getPorts());

					if (currentVessel != null) {
						if (!currentVessel.getVesselOrDelegateInaccessiblePorts().isEmpty()) {
							admissible.removeAll(SetUtils.getObjects(currentVessel.getVesselOrDelegateInaccessiblePorts()));
						}
					}

					// Allow the current value
					if (currentValue != null) {
						// allow it to be any port...
						admissible.add(currentValue);
					}

					final List<Pair<String, EObject>> sortedNames = getSortedNames(new LinkedList<Port>(admissible), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
					sortedNames.add(0, new Pair<>("<Start anywhere>", null));

					return sortedNames;
				}

			};

		}

		return null;
	}

}
