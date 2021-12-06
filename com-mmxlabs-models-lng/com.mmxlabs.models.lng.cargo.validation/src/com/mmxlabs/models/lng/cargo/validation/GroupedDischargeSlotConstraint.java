/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class GroupedDischargeSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();

		if (!(extraContext.getContainer(object) instanceof CargoModel)) {
			return Activator.PLUGIN_ID;
		}
		if (object instanceof GroupedSlotsConstraint) {
			final GroupedSlotsConstraint groupConstraint = (GroupedSlotsConstraint) object;
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withName("Grouped Constraint");

			final List<Slot> dischargeSlotGroup = groupConstraint.getSlots();
			final int bound = groupConstraint.getMinimumBound();
			if (dischargeSlotGroup.isEmpty()) {
				factory.copyName().withMessage("Slot group must be non-empty.").withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx,
						statuses);
			} else {
				final boolean dischargeSlotGroupBoundValid = dischargeSlotGroup.size() >= bound;
				if (!dischargeSlotGroupBoundValid) {
					factory.copyName().withMessage("Discharge slot group must have at least minimum number.")
							.withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
				}
				final boolean dischargeSlotGroupAllOptional = dischargeSlotGroup.stream().allMatch(Slot::isOptional);
				if (!dischargeSlotGroupAllOptional) {
					factory.copyName().withMessage("All discharge slots in group must be optional.")
							.withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
				}
				if (dischargeSlotGroupBoundValid && dischargeSlotGroupAllOptional) {
					final long boundTruncatedDischargeHasCargoCount = dischargeSlotGroup.stream().unordered().filter(s -> s.getCargo() != null).limit(bound).count();
					if (boundTruncatedDischargeHasCargoCount < bound) {
						factory.copyName().withMessage("Bound constraint should be satisfied by basecase")
								.withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
					}
				}
			}

			if (bound == 0) {
				factory.copyName().withMessage("Minimum bound must be positive.").withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx,
						statuses);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
