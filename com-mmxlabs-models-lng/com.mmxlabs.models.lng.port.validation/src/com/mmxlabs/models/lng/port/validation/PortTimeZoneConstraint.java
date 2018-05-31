/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortTimeZoneConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Location) {
			final Location location = (Location) target;

			// Check a time zone is specified
			if (location.getTimeZone() == null || location.getTimeZone().isEmpty()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
				dsd.addEObjectAndFeature(location, PortPackage.eINSTANCE.getLocation_TimeZone());
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
			if (!timeZones.contains(location.getTimeZone())) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
				dsd.addEObjectAndFeature(location, PortPackage.eINSTANCE.getLocation_TimeZone());
				return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}
}
