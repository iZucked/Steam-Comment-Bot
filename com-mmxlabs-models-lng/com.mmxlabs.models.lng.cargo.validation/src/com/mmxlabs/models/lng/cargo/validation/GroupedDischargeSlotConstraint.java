/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class GroupedDischargeSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GROUPED_OPTIONAL_SLOTS_CONSTRAINTS)) {
			return;
		}
		final EObject object = ctx.getTarget();
		if (!(extraContext.getContainer(object) instanceof CargoModel)) {
			return;
		}
		if (object instanceof GroupedSlotsConstraint groupedSlotsConstraint) {
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withName("Grouped Constraint");

			final List<Slot> dischargeSlotGroup = groupedSlotsConstraint.getSlots();
			final int bound = groupedSlotsConstraint.getMinimumBound();
			if (dischargeSlotGroup.isEmpty()) {
				factory.copyName().withMessage("Slot group must be non-empty.").withObjectAndFeature(groupedSlotsConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx,
						statuses);
			} else {
				final boolean dischargeSlotGroupBoundValid = dischargeSlotGroup.size() >= bound;
				if (!dischargeSlotGroupBoundValid) {
					factory.copyName().withMessage("Discharge slot group must have at least minimum number.")
							.withObjectAndFeature(groupedSlotsConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
				}
				final boolean dischargeSlotGroupAllOptional = dischargeSlotGroup.stream().allMatch(Slot::isOptional);
				if (!dischargeSlotGroupAllOptional) {
					factory.copyName().withMessage("All discharge slots in group must be optional.")
							.withObjectAndFeature(groupedSlotsConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
				}
				if (dischargeSlotGroupBoundValid && dischargeSlotGroupAllOptional) {
					final long boundTruncatedDischargeHasCargoCount = dischargeSlotGroup.stream().unordered().filter(s -> s.getCargo() != null).limit(bound).count();
					// Check if less than bound. Greater or equal means constraint is satisfied
					if (boundTruncatedDischargeHasCargoCount < bound) {
						factory.copyName().withMessage("Bound constraint should be satisfied by basecase")
								.withObjectAndFeature(groupedSlotsConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx, statuses);
					}
				}
			}

			if (bound <= 0) {
				factory.copyName().withMessage("Minimum bound must be positive.").withObjectAndFeature(groupedSlotsConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots()).make(ctx,
						statuses);
			}
		}
	}
}
