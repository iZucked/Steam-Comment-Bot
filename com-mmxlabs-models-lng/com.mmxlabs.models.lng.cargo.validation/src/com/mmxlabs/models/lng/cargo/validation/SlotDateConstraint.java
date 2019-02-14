/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotDateConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();

		if (object instanceof Slot) {
			final Slot slot = (Slot) object;
			LocalDate windowStart = slot.getWindowStart();
			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("Slot", slot.getName() == null ? "(no ID)" : slot.getName());

			if (windowStart == null) {
				factory.copyName() //
						.withMessage("An arrival window must be set") //
						.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart()) //
						.make(ctx, statuses);
			} else if (windowStart.getYear() < 2000) {
				factory.copyName() //
						.withMessage(String.format("%d is not a valid year", windowStart.getYear())) //
						.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart()) //
						.make(ctx, statuses);
			} else if (windowStart.getYear() > 2100) {
				factory.copyName() //
						.withMessage(String.format("%d is not a valid year", windowStart.getYear())) //
						.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart()) //
						.make(ctx, statuses);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
