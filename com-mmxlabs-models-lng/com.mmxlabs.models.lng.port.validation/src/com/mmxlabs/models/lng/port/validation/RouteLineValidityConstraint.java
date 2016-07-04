/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint which checks the reasonableness of entries in the distance matrix.
 * 
 * @author Tom Hinton
 * 
 */
public class RouteLineValidityConstraint extends AbstractModelConstraint {
	private static final int MAX_DISTANCE = 20000;

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof RouteLine) {
			final RouteLine distanceLine = (RouteLine) target;
			final IStatus status = validateDistance(ctx, distanceLine);

			if (status != null) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) status);
				dsd.addEObjectAndFeature(target, PortPackage.eINSTANCE.getRouteLine_Distance());
				return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}

	/**
	 * @param routeLine
	 * @return
	 */
	private IStatus validateDistance(final IValidationContext ctx, final RouteLine routeLine) {
		if (routeLine.getFrom() != null && routeLine.getTo() != null) {
			if (routeLine.getFullDistance() < 0 || routeLine.getFullDistance() > MAX_DISTANCE) {
				return ctx.createFailureStatus(routeLine.getFrom().getName(), routeLine.getTo().getName(), routeLine.getDistance());
			}
		}
		return null;
	}

}
