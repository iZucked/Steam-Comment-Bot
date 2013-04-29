/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
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
			final FleetModel fleetModel = ((LNGScenarioModel)rootObject).getFleetModel();
			final ScenarioFleetModel sfm = ((LNGScenarioModel) rootObject).getPortfolioModel().getScenarioFleetModel();

			return new SimpleReferenceValueProvider(fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS) {
				
				@Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target,
						EStructuralFeature field) {
					// prepare to create a collection of unassigned vessels
					final Set<Vessel> unassigned = new HashSet<Vessel>(fleetModel.getVessels());		
					
					// remove assigned vessels from "unassigned" set
					for (VesselAvailability availability: sfm.getVesselAvailabilities()) {
						unassigned.remove(availability.getVessel());
					}
					
					// re-add the vessel assigned to the current target (since it should be a legitimate selection)
					if (target instanceof VesselAvailability) {
						unassigned.add(((VesselAvailability) target).getVessel());
					}
					
					return getSortedNames(new BasicEList<Vessel>(unassigned), MMXCorePackage.Literals.NAMED_OBJECT__NAME);						
				}
				
			};
			
		}

		return null;
	}

}
