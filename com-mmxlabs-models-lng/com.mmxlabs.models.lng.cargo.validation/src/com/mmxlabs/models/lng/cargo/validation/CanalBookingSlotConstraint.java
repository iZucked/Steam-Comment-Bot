/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 */
public class CanalBookingSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof CanalBookingSlot canalBookingSlot) {

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withName("Panama canal bookings");

			if (canalBookingSlot.getRouteOption() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__ROUTE_OPTION) //
						.withMessage("Canal must be specified.") //
						.make(ctx));
			}

			if (canalBookingSlot.getBookingDate() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_DATE) //
						.withMessage("Date must be specified.") //
						.make(ctx));
			}

			if (canalBookingSlot.getCanalEntrance() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__CANAL_ENTRANCE) //
						.withMessage("Entry point must be specified.") //
						.make(ctx));
			}
		}
	}

}
