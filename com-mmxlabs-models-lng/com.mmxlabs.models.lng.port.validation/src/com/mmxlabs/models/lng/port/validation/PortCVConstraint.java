/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortCVConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Port) {
			final Port port = (Port) target;

			if (port.getCapabilities().contains(PortCapability.LOAD)) {
				if (port.getCvValue() < 1.0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value it too low."));
					dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_CvValue());
					return dsd;
				} else if (port.getCvValue() > 40.0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high."));
					dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_CvValue());
					return dsd;
				}
			}

		}
		return ctx.createSuccessStatus();
	}
}
