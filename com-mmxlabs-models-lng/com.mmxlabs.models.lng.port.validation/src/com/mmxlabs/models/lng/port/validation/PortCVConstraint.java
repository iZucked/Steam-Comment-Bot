/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
	public PortCVConstraint() {
		setRange(PortPackage.Literals.PORT__CV_VALUE, 1.0, 40.0);
	}

	@Override
	protected String getPluginId() {
		return Activator.PLUGIN_ID;
	}
	
	@Override
	protected boolean shouldValidateFeature(EObject target, EStructuralFeature feature) {
		return (target instanceof Port && ((Port) target).getCapabilities().contains(PortCapability.LOAD));
	}

	/*
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Port) {
			final Port port = (Port) target;

			if (port.getCapabilities().contains(PortCapability.LOAD)) {
				double cv = port.getCvValue();
				if (cv < minCv || cv > maxCv) {
					String message = String.format("Port CV value %.2f (should be between %.2f and %.2f)", cv, minCv, maxCv);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_CvValue());
					return dsd;					
				}
			}

		}
		return ctx.createSuccessStatus();
	}
	*/
	
}
