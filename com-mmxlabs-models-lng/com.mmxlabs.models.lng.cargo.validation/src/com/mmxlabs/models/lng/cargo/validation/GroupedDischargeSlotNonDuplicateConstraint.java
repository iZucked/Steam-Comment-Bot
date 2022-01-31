/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class GroupedDischargeSlotNonDuplicateConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GROUPED_OPTIONAL_SLOTS_CONSTRAINTS)) {
			return;
		}
		final EObject object = ctx.getTarget();
		if (object instanceof CargoModel cargoModel) {
			final Map<DischargeSlot, List<GroupedDischargeSlotsConstraint>> seenDischargeSlots = new HashMap<>();
			final Map<DischargeSlot, List<GroupedDischargeSlotsConstraint>> overlappingDischargeSlots = new HashMap<>();
			for (final GroupedDischargeSlotsConstraint groupedDischargeSlotsConstraint : cargoModel.getGroupedDischargeSlots()) {
				for (final DischargeSlot dischargeSlot : groupedDischargeSlotsConstraint.getSlots()) {
					final List<GroupedDischargeSlotsConstraint> constraints = seenDischargeSlots.get(dischargeSlot);
					if (constraints != null) {
						if (constraints.size() == 1) {
							overlappingDischargeSlots.put(dischargeSlot, constraints);
						}
						constraints.add(groupedDischargeSlotsConstraint);
					} else {
						final List<GroupedDischargeSlotsConstraint> newConstraints = new ArrayList<>();
						seenDischargeSlots.put(dischargeSlot, newConstraints);
						newConstraints.add(groupedDischargeSlotsConstraint);
					}
				}
			}
			seenDischargeSlots.clear();
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withName("Grouped Constraint");
			for (final Entry<DischargeSlot, List<GroupedDischargeSlotsConstraint>> overlap : overlappingDischargeSlots.entrySet()) {
				final DetailConstraintStatusFactory currentFactory = factory.copyName().withMessage(String.format("Discharge slot %s used by multiple grouped slot constraints", overlap.getKey().getName()));
				for (final GroupedDischargeSlotsConstraint groupConstraint : overlap.getValue()) {
					currentFactory.withObjectAndFeature(groupConstraint, CargoPackage.eINSTANCE.getGroupedSlotsConstraint_Slots());
				}
				currentFactory.make(ctx, statuses);
			}
		}
	}
}
