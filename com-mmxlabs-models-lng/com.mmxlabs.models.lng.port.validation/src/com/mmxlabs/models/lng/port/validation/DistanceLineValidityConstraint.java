/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.validation.DetailConstraintStatusDecorator;

/**
 * A constraint which checks the reasonableness of entries in the distance matrix.
 * @author Tom Hinton
 * 
 */
public class DistanceLineValidityConstraint extends AbstractModelConstraint {
	private static final int MAX_DISTANCE = 20000;

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof DistanceLine) {
			final DistanceLine distanceLine = (DistanceLine) target;
			final IStatus status = validateDistance(ctx, distanceLine);

			if (status != null) {
				
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) status);
				dsd.addEObjectAndFeature(target, PortPackage.eINSTANCE.getDistanceLine_Distance());
				return dsd;
			}
		}
		return ctx.createSuccessStatus();
	}

	/**
	 * @param distanceLine
	 * @return
	 */
	private IStatus validateDistance(final IValidationContext ctx,
			final DistanceLine distanceLine) {
		if (distanceLine.getFromPort() != null
				&& distanceLine.getToPort() != null) {
			if (distanceLine.getDistance() < 0
					|| distanceLine.getDistance() > MAX_DISTANCE) {
				return ctx.createFailureStatus(distanceLine.getFromPort()
						.getName(), distanceLine.getToPort().getName(),
						distanceLine.getDistance());
			}
		}
		return null;
	}

}
