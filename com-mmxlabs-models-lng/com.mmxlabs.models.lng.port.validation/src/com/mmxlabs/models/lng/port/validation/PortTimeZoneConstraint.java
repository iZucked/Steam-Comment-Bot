/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortTimeZoneConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Port) {
			final Port port = (Port) target;

			// Check a time zone is specified
			if (port.getTimeZone() == null || port.getTimeZone().isEmpty()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
				dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_TimeZone());
				return dsd;
			}

			// Build up map of valid time zones
			final Set<String> timeZones = new HashSet<String>();
			for (final String s : TimeZone.getAvailableIDs()) {
				if (s.indexOf("/") != -1) {
					timeZones.add(s);
				}
			}
			// Check specified timezone is valid
			if (!timeZones.contains(port.getTimeZone())) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
				dsd.addEObjectAndFeature(port, PortPackage.eINSTANCE.getPort_TimeZone());
				return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
