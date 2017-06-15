/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 */
public class CanalBookingSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof CanalBookingSlot) {
			final CanalBookingSlot canalBookingSlot = (CanalBookingSlot) target;

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus().withName(ScenarioElementNameHelper.getTypeName(canalBookingSlot));

			if (canalBookingSlot.getRoute() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__ROUTE) //
						.withMessage("Canal must be specified.") //
						.make(ctx));
			}
			
			if (canalBookingSlot.getBookingDate() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_DATE) //
						.withMessage("Date must be specified.") //
						.make(ctx));
			}else {
				// TODO: fill in
			}
			
			if (canalBookingSlot.getEntryPoint() == null) {
				statuses.add(baseFactory.copyName() //
						.withObjectAndFeature(canalBookingSlot, CargoPackage.Literals.CANAL_BOOKING_SLOT__ENTRY_POINT) //
						.withMessage("Entry point must be specified.") //
						.make(ctx));
			}
		}
		return Activator.PLUGIN_ID;
	}

}
