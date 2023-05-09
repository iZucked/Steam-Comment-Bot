/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.VesselUsageConstraintInfo;

/**
 * {@link IPairwiseConstraintChecker} to keep groups of slots in their required
 * ranges.
 * 
 * @author achurchill
 */
public class VesselUsageSlotGroupConstraintChecker implements IPairwiseConstraintChecker, IResourceElementConstraintChecker {

	@Inject
	private IVesselUsageConstraintDataProvider vesselUsageConstraintDataProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	private final @NonNull String name;

	public VesselUsageSlotGroupConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
		final Set<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = vesselUsageConstraintDataProvider.getAllDischargeVesselUses();
		final Set<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = vesselUsageConstraintDataProvider.getAllLoadVesselUses();

		final Map<Object, Integer> counts = new HashMap<>();

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			for (final ISequenceElement element : sequence) {
				final IPortSlot slot = portSlotProvider.getPortSlot(element);
				final IVessel vessel = vesselProvider.getVesselCharter(resource).getVessel();
				Stream.concat(allLoadVesselConstraintInfos.stream(), allDischargeVesselUsesConstraintInfos.stream())//
						.filter(x -> x.getSlots().contains(slot) && x.getVessels().contains(vessel))//
						.map(x -> x.getProfileConstraint())//
						.forEach(x -> counts.compute(x, (k, v) -> v == null ? 1 : v + 1));
			}
		}
		for (final VesselUsageConstraintInfo<?, ?, ILoadOption> constraintInfo : allLoadVesselConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraint()), 0);
			if (vesselUses != constraintInfo.getBound()) {
				if (messages != null) {
					messages.add(String.format("%s: Load Slot Vessel Usage not equal to constraint", this.name));
				}
				return false;
			}
		}
		for (final VesselUsageConstraintInfo<?, ?, IDischargeOption> constraintInfo : allDischargeVesselUsesConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraint()), 0);
			if (vesselUses != constraintInfo.getBound()) {
				if (messages != null) {
					messages.add(String.format("%s: Discharge Slot Vessel Usage not equal to constraint", this.name));
				}
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource, @Nullable List<@NonNull String> messages) {
		return true;
	}

	@Override
	public boolean checkElement(@NonNull ISequenceElement element, @NonNull IResource resource, @Nullable List<@NonNull String> messages) {
		final IPortSlot slot = portSlotProvider.getPortSlot(element);
		final IVessel vessel = vesselProvider.getVesselCharter(resource).getVessel();
		final Set<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = vesselUsageConstraintDataProvider.getAllDischargeVesselUses();
		final Set<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = vesselUsageConstraintDataProvider.getAllLoadVesselUses();

		return Stream.concat(allLoadVesselConstraintInfos.stream(), allDischargeVesselUsesConstraintInfos.stream())//
				.noneMatch(x -> x.getBound() == 0 && x.getVessels().contains(vessel) && x.getSlots().contains(slot));
	}

//	public @NonNull List<@NonNull Object> getFailedConstraintInfos(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
//		List<Object> failedConstraintInfos = new ArrayList<>();
//		final Set<ISequenceElement> unusedSet = getUnusedSet(sequences);
//		final List<ConstraintInfo<?, ?, IDischargeOption>> allMinDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMinDischargeGroupCounts();
//		final List<ConstraintInfo<?, ?, IDischargeOption>> allMaxDischargeGroupCounts = maxSlotCountConstraintProvider.getAllMaxDischargeGroupCounts();
//		final List<ConstraintInfo<?, ?, ILoadOption>> allMinLoadGroupCounts = maxSlotCountConstraintProvider.getAllMinLoadGroupCounts();
//		final List<ConstraintInfo<?, ?, ILoadOption>> allMaxLoadGroupCounts = maxSlotCountConstraintProvider.getAllMaxLoadGroupCounts();
//
//		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMinDischargeGroupCounts) {
//			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
//			if (cnt < constraintInfo.getBound()) {
//				failedConstraintInfos.add(constraintInfo);
//				constraintInfo.setViolatedAmount(ViolationType.Min, (int)cnt);
//			}
//		}
//		for (final ConstraintInfo<?, ?, IDischargeOption> constraintInfo : allMaxDischargeGroupCounts) {
//			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
//			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
//				failedConstraintInfos.add(constraintInfo);
//				constraintInfo.setViolatedAmount(ViolationType.Max, (int)cnt);
//			}
//		}
//		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMinLoadGroupCounts) {
//			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
//			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() < constraintInfo.getBound()) {
//				failedConstraintInfos.add(constraintInfo);
//				constraintInfo.setViolatedAmount(ViolationType.Min, (int)cnt);
//			}
//		}
//		for (final ConstraintInfo<?, ?, ILoadOption> constraintInfo : allMaxLoadGroupCounts) {
//			long cnt = constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count();
//			if (constraintInfo.getSlots().stream().filter(s -> !unusedSet.contains(portSlotProvider.getElement(s))).count() > constraintInfo.getBound()) {
//				failedConstraintInfos.add(constraintInfo);
//				constraintInfo.setViolatedAmount(ViolationType.Max, (int)cnt);
//			}
//		}
//		return failedConstraintInfos;
//	}
}
