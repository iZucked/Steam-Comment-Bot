/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractFeatureRangeConstraint;

public class PortCVConstraint extends AbstractFeatureRangeConstraint {
	@Override
	public void createConstraints() {
		setRange(PortPackage.Literals.PORT__CV_VALUE, 1.0, 40.0, "Port CV");
	}

	@Override
	protected String getPluginId() {
		return Activator.PLUGIN_ID;
	}
	
	@Override
	protected boolean shouldValidateFeature(EObject target, EStructuralFeature feature) {
		return (target instanceof Port && ((Port) target).getCapabilities().contains(PortCapability.LOAD));
	}

	
}
