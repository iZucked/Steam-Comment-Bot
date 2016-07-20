/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NegativePriceConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof SlotVisit) {
			EObject obj = null;
			EStructuralFeature feature = null;
			String message = null;

			SlotAllocation allocation = ((SlotVisit) target).getSlotAllocation();
			if (allocation != null) {
				obj = allocation.getSlot();
				if (allocation.getPrice() < 0) {
					message = String.format(Constants.GENERATED_SCHEDULE_LABEL + " Slot %s has a negative price of -$%s", ((Slot) obj).getName(), allocation.getPrice()*-1);
					feature = CargoPackage.Literals.SLOT__CARGO;
				}
			}

			if (message != null) {
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message), IConstraintStatus.ERROR);
				if (obj != null) {
					failure.addEObjectAndFeature(obj, feature);
				}
				statuses.add(failure);
			}

		}

		return Activator.PLUGIN_ID;
	}
}
