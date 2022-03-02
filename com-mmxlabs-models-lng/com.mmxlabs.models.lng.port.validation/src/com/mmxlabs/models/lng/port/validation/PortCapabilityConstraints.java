/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortCapabilityConstraints extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Port port) {
			for (final PortCapability pc : port.getCapabilities()) {
				switch (pc) {
				case DISCHARGE: {
					if (port.getDischargeDuration() < 1) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Discharge ports must have a discharge duration"));
						dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_DischargeDuration());
						failures.add(dsd);
					}
				}
					break;
				case LOAD:
					if (port.getLoadDuration() < 1) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Load ports must have a load duration"));
						dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_LoadDuration());
						failures.add(dsd);
					}
					break;
				case DRYDOCK:
				case MAINTENANCE:
				default:
					break;

				}

			}
		}

	}
}
