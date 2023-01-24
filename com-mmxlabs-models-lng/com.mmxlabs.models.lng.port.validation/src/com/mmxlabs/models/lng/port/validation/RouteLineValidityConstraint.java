/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint which checks the reasonableness of entries in the distance
 * matrix.
 * 
 * @author Tom Hinton
 * 
 */
@NonNullByDefault
public class RouteLineValidityConstraint extends AbstractModelMultiConstraint {

	private static final int MAX_DISTANCE = 20000;

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof RouteLine distanceLine) {
			final IStatus status = validateDistance(ctx, distanceLine);
			if (status != null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) status);
				dsd.addEObjectAndFeature(target, PortPackage.eINSTANCE.getRouteLine_Distance());
				statuses.add(dsd);
			}
		}
	}

	/**
	 * @param routeLine
	 * @return
	 */
	private @Nullable IStatus validateDistance(final IValidationContext ctx, final RouteLine routeLine) {
		if (routeLine.getFrom() != null && routeLine.getTo() != null) {
			if (routeLine.getDistance() > MAX_DISTANCE || (routeLine.getDistance() < 0 && routeLine.getErrorCode() == null)) {
				return ctx.createFailureStatus(routeLine.getFrom().getName(), routeLine.getTo().getName(), routeLine.getDistance());
			}
		}
		return null;
	}

}
