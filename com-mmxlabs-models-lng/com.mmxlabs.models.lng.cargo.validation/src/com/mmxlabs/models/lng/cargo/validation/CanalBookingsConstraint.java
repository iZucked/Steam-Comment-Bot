/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 */
public class CanalBookingsConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof CanalBookings) {

			CanalBookings canalBookings = (CanalBookings) target;
			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withName("Panama bookings");

			if (canalBookings.getStrictBoundaryOffsetDays() > canalBookings.getRelaxedBoundaryOffsetDays()) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS) //
						.withObjectAndFeature(canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS) //
						.withMessage("Relaxed period must be greater than or equals to strict period") //
						.make(ctx));
			}
		}
		return Activator.PLUGIN_ID;
	}

}
