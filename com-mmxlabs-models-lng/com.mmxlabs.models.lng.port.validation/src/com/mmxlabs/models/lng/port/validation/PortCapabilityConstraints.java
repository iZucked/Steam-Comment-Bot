/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortCapabilityConstraints extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof Port) {
			final Port port = (Port) target;

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
				case DRYDOCK:
					break;
				case LOAD:
					if (port.getLoadDuration() < 1) {

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Load ports must have a load duration"));
						dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_LoadDuration());
						failures.add(dsd);
					}
					if (port.getCvValue() < 0.0000001) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Load ports must have a CV value"));
						dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_CvValue());
						failures.add(dsd);
					}
					break;
				case MAINTENANCE:
					break;

				}

			}
		}
		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}
