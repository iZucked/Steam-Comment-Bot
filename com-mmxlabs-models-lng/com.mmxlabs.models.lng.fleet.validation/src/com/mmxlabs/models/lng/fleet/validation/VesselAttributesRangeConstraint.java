/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.ui.validation.AbstractFeatureRangeConstraint;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class VesselAttributesRangeConstraint extends AbstractFeatureRangeConstraint {

	@Override
	protected boolean shouldValidateFeature(EObject object, EStructuralFeature feature) {

		return (object instanceof VesselStateAttributes vsa && vsa.eContainer() instanceof Vessel vessel && !vessel.isMarker());
	}

	@Override
	protected void createConstraints() {
		setRange(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, 0.0, null, "NBO rate");
		setRange(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, 0.0, null, "Idle base rate");
	}

	@Override
	protected boolean shouldValidateNonMarkerInitialVesselAttributes(EObject object, EStructuralFeature feature) {

		return feature.isUnsettable() && (object instanceof VesselStateAttributes vsa && vsa.eContainer() instanceof Vessel vessel && vessel.getReference() == null);
	}
}
