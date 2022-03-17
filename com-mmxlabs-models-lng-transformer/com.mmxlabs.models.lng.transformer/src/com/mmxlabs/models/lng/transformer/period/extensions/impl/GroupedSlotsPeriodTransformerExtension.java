/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period.extensions.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.period.extensions.IPeriodTransformerExtension;

public class GroupedSlotsPeriodTransformerExtension implements IPeriodTransformerExtension {

	@Override
	public @Nullable List<Slot<?>> getExtraDependenciesForSlot(Slot<?> slot) {
		return null;
	}

	@Override
	public void processSlotInclusionsAndExclusions(@NonNull CargoModel cargoModel, @NonNull Schedule schedule, @NonNull Collection<Slot<?>> excludedSlots, @NonNull Collection<Cargo> excludedCargoes) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GROUPED_OPTIONAL_SLOTS_CONSTRAINTS)) {
			return;
		}
		final List<GroupedDischargeSlotsConstraint> groupedDischargeSlotsConstraints = cargoModel.getGroupedDischargeSlots();
		final Set<GroupedDischargeSlotsConstraint> groupedDischargeSlotsConstraintsToRemove = new HashSet<>();
		for (final GroupedDischargeSlotsConstraint groupedDischargeSlotsConstraint : groupedDischargeSlotsConstraints) {
			int boundReduction = 0;
			final Set<DischargeSlot> dischargeSlotsToRemove = new HashSet<>();
			for (final DischargeSlot dischargeSlot : groupedDischargeSlotsConstraint.getSlots()) {
				if (excludedSlots.contains(dischargeSlot)) {
					dischargeSlotsToRemove.add(dischargeSlot);
				}
				final Cargo cargo = dischargeSlot.getCargo();
				if (cargo != null && excludedCargoes.contains(cargo)) {
					// discharge slot is also excluded
					dischargeSlotsToRemove.add(dischargeSlot);
					// reduce bound by 1
					++boundReduction;
				}
			}
			final int newBound = groupedDischargeSlotsConstraint.getMinimumBound() - boundReduction;
			if (newBound > 0) {
				// There is still a constraint to enforce
				// Possible optimisation: if newBound equals leftover discharge slots, make discharge slots non-optional and remove constraint?
				if (!dischargeSlotsToRemove.isEmpty()) {
					// remove any discharge slots that need removing
					groupedDischargeSlotsConstraint.getSlots().removeAll(dischargeSlotsToRemove);
				}
				groupedDischargeSlotsConstraint.setMinimumBound(newBound);
			} else {
				// Note if newBound == 0, we have free choice over remaining slots so we do not need a constraint
				// The constraint is satisfied by removed cargoes - validation should ensure this - we do not need to include it.
				groupedDischargeSlotsConstraintsToRemove.add(groupedDischargeSlotsConstraint);
			}
		}
		if (!groupedDischargeSlotsConstraintsToRemove.isEmpty()) {
			cargoModel.getGroupedDischargeSlots().removeAll(groupedDischargeSlotsConstraintsToRemove);
		}
	}

}
