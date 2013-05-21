/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class VesselValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final FleetModel fleetModel = ((LNGScenarioModel)rootObject).getFleetModel();
			final EClass referenceClass = reference.getEReferenceType();
			final TypesPackage types = TypesPackage.eINSTANCE;
			final FleetPackage fleet = FleetPackage.eINSTANCE;

			if (referenceClass == fleet.getVessel()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_Vessels());
			} else if (referenceClass == fleet.getVesselClass()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses());
			} else if (referenceClass == types.getAVesselSet()) {
				return new MergedReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses(), fleet.getFleetModel_Vessels(), fleet.getFleetModel_SpecialVesselGroups(),
						fleet.getFleetModel_VesselGroups());
			} else if (referenceClass == fleet.getVesselGroup()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselGroups());
			}
		}

		return null;
	}

}
