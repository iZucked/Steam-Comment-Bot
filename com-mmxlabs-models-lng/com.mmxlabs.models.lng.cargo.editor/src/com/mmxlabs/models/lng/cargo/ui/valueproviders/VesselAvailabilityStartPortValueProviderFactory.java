/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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

public class VesselAvailabilityStartPortValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGReferenceModel referenceModel = lngScenarioModel.getReferenceModel();
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			final PortModel portModel = referenceModel.getPortModel();

			return new SimpleReferenceValueProvider(portModel, PortPackage.Literals.PORT_MODEL__PORTS) {

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					return getRestrictedPortsList(cargoModel, portModel, target);
				}

				private List<Pair<String, EObject>> getAllPortsList(final CargoModel cargoModel, final PortModel portModel, final EObject target) {
					return getSortedNames(portModel.getPorts(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				}

				private List<Pair<String, EObject>> getRestrictedPortsList(final CargoModel cargoModel, final PortModel portModel, final EObject target) {
					Port currentValue = null;
					Vessel currentVessel = null;
					if (target instanceof VesselAvailability) {
						final VesselAvailability vesselAvailability = (VesselAvailability) target;
						currentValue = vesselAvailability.getStartAt();
						currentVessel = vesselAvailability.getVessel();
					}

					// make a list of admissible vessels
					final Set<Port> admissible = new HashSet<>();
					admissible.addAll(portModel.getPorts());

					if (currentVessel != null) {
						if (!currentVessel.getInaccessiblePorts().isEmpty()) {
							admissible.removeAll(SetUtils.getObjects(currentVessel.getInaccessiblePorts()));
						} else {
							if (!currentVessel.getVesselClass().getInaccessiblePorts().isEmpty()) {
								admissible.removeAll(SetUtils.getObjects(currentVessel.getVesselClass().getInaccessiblePorts()));
							}
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
