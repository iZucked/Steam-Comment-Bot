/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
			final EClass referenceClass = reference.getEReferenceType();
			final FleetPackage fleet = FleetPackage.eINSTANCE;

			if (referenceClass == TypesPackage.Literals.AVESSEL_SET) {
				return new MergedReferenceValueProvider(fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, FleetPackage.Literals.FLEET_MODEL__VESSEL_CLASSES,
						FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUPS);
			} else if (referenceClass == fleet.getVessel()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_Vessels());
			} else if (referenceClass == fleet.getVesselClass()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses());
			} else if (referenceClass == fleet.getVesselGroup()) {
				return new SimpleReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselGroups());
			}
		}

		return null;
	}

}
