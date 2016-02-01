/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractFeatureRangeConstraint;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class VesselAttributesRangeConstraint extends AbstractFeatureRangeConstraint {
	
	@Override
	protected String getPluginId() {
		return Activator.PLUGIN_ID;
	}



	@Override
	protected boolean shouldValidateFeature(EObject object,
			EStructuralFeature feature) {
		return (object instanceof VesselStateAttributes);
	}



	@Override
	protected void createConstraints() {
		setRange(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE, 0.0, null, "NBO rate");
		setRange(FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE, 0.0, null, "Idle base rate");
	}
}
