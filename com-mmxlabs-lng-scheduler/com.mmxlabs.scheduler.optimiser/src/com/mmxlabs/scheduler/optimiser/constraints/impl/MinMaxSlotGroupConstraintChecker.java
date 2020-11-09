/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintInfoGetter;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.IMaxSlotCountConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo.ViolationType;

/**
 * {@link IPairwiseConstraintChecker} to keep groups of slots in their required ranges.
 * 
 * @author achurchill
 */
public class MinMaxSlotGroupConstraintChecker implements IPairwiseConstraintChecker, IConstraintInfoGetter {

	@Inject
	private IMaxSlotCountConstraintDataProvider maxSlotCountConstraintProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private final @NonNull String name;

	public MinMaxSlotGroupConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	/**
	 * Note: this does not make sense for this constraint
	 */
	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return true;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		final Set<ISequenceElement> unusedSet = getUnusedSet(sequences);
		final List<ConstraintInfo<?, ?, IDischargeOption>> allMinDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMinDischargeGroupCounts();
		final List<ConstraintInfo<?, ?, IDischargeOption>> allMaxDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMaxDischargeGroupCounts();
		final List<ConstraintInfo<?, ?, ILoadOption>> allMinLoadGroupCounts = maxSlotCountConstraintProvider.getAllMinLoadGroupCounts();
		final List<ConstraintInfo<?, ?, ILoadOption>> allMaxLoadGroupCounts = maxSlotCountConstraintProvider.getAllMaxLoadGroupCounts();

		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMinDischargeGroupCounts) {
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() < constraintInfo.getBound()) {
				return false;
			}
		}
		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMaxDischargeGroupCounts) {
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
				return false;
			}
		}
		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMinLoadGroupCounts) {
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() < constraintInfo.getBound()) {
				return false;
			}
		}
		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMaxLoadGroupCounts) {
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		return checkConstraints(sequences, changedResources);
	}

	private Set<ISequenceElement> getUnusedSet(@NonNull final ISequences sequences) {
		return new HashSet<>(sequences.getUnusedElements());
	}

	public @NonNull List<Object> getFailedConstraintInfos(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		List<Object> failedConstraintInfos = new ArrayList<>();
		final Set<ISequenceElement> unusedSet = getUnusedSet(sequences);
		final List<ConstraintInfo<?, ?, IDischargeOption>> allMinDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMinDischargeGroupCounts();
		final List<ConstraintInfo<?, ?, IDischargeOption>> allMaxDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMaxDischargeGroupCounts();
		final List<ConstraintInfo<?, ?, ILoadOption>> allMinLoadGroupCounts = maxSlotCountConstraintProvider.getAllMinLoadGroupCounts();
		final List<ConstraintInfo<?, ?, ILoadOption>> allMaxLoadGroupCounts = maxSlotCountConstraintProvider.getAllMaxLoadGroupCounts();

		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMinDischargeGroupCounts) {
			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
			if (cnt < constraintInfo.getBound()) {
				failedConstraintInfos.add(constraintInfo);
				constraintInfo.setViolatedAmount(ViolationType.Min, (int)cnt);
			}
		}
		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMaxDischargeGroupCounts) {
			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
				failedConstraintInfos.add(constraintInfo);
				constraintInfo.setViolatedAmount(ViolationType.Max, (int)cnt);
			}
		}
		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMinLoadGroupCounts) {
			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() < constraintInfo.getBound()) {
				failedConstraintInfos.add(constraintInfo);
				constraintInfo.setViolatedAmount(ViolationType.Min, (int)cnt);
			}
		}
		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMaxLoadGroupCounts) {
			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
				failedConstraintInfos.add(constraintInfo);
				constraintInfo.setViolatedAmount(ViolationType.Max, (int)cnt);
			}
		}

		return failedConstraintInfos;
	}
}
