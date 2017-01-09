/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class VesselAvailabilityVesselValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final LNGReferenceModel referenceModel = lngScenarioModel.getReferenceModel();
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			final FleetModel fleetModel = referenceModel.getFleetModel();

			return new SimpleReferenceValueProvider(fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS) {

				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					// determine the current vessel attached to the availability
					return getAllVesselsList(cargoModel, fleetModel, target);
				}

				private List<Pair<String, EObject>> getAllVesselsList(final CargoModel cargoModel, final FleetModel fleetModel, final EObject target) {
					return getSortedNames(fleetModel.getVessels(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				}
				
				private List<Pair<String, EObject>> getRestrictedVesselsList(final CargoModel cargoModel, final FleetModel fleetModel, final EObject target) {
					Vessel currentValue = null;

					if (target instanceof VesselAvailability) {
						currentValue = ((VesselAvailability) target).getVessel();
					}

					// make a list of admissible vessels
					final Set<Vessel> admissible = new HashSet<Vessel>();

					// if there is no vessel assigned to this availability,
					if (currentValue == null) {
						// allow it to be any vessel...
						admissible.addAll(fleetModel.getVessels());
						// ... which not already available
						for (final VesselAvailability availability : cargoModel.getVesselAvailabilities()) {
							admissible.remove(availability.getVessel());
						}
					}
					// if there is a vessel assigned to this availability,
					else {
						// don't allow any other value
						admissible.add(currentValue);
					}

					return getSortedNames(new BasicEList<Vessel>(admissible), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				}

			};

		}

		return null;
	}

}
