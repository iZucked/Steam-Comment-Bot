/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IConstraintChecker} implementation to enforce load windows are before discharge windows. This does not care about sequencing, the {@link PortTypeConstraintChecker} is left to handle that.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeSortConstraintChecker implements IPairwiseConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IPortTypeProvider portTypeProvider;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	/**
	 */
	public TimeSortConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {

		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final VesselInstanceType vesselInstanceType = vesselProvider.getVesselAvailability(resource).getVesselInstanceType();
			if (vesselInstanceType == VesselInstanceType.UNKNOWN || vesselInstanceType == VesselInstanceType.DES_PURCHASE || vesselInstanceType == VesselInstanceType.FOB_SALE) {
				continue;
			}
			final ISequence sequence = sequences.getSequence(resource);
			assert sequence != null;
			if (!checkSequence(sequence, messages, vesselInstanceType)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

	}

	/**
	 * Check ISequence for {@link PortType} ordering violations.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	public final boolean checkSequence(@NonNull final ISequence sequence, @Nullable final List<String> messages, @NonNull final VesselInstanceType instanceType) {

		ITimeWindow lastTimeWindow = null;
		PortType lastType = null;

		for (final ISequenceElement t : sequence) {
			final PortType currentType = portTypeProvider.getPortType(t);

			final IPortSlot currentSlot = portSlotProvider.getPortSlot(t);
			final ITimeWindow tw = currentType == PortType.Round_Trip_Cargo_End ? null : currentSlot.getTimeWindow();
			if (instanceType != VesselInstanceType.ROUND_TRIP || (lastType == PortType.Load && currentType == PortType.Discharge)) {

				if (lastTimeWindow != null && tw != null) {
					if (tw.getExclusiveEnd() <= lastTimeWindow.getInclusiveStart()) {
						if (messages != null) {
							messages.add("Current time window is before previous time window");
						}
						return false;
					}
				}
			}
			lastTimeWindow = tw;
			lastType = currentType;

		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final VesselInstanceType instanceType = vesselProvider.getVesselAvailability(resource).getVesselInstanceType();
		if (instanceType == VesselInstanceType.ROUND_TRIP) {
			// Cargo pairs are independent of each other, so only check real load->discharge state and ignore rest
			final PortType t1 = portTypeProvider.getPortType(first);
			final PortType t2 = portTypeProvider.getPortType(second);
			// Accept, or fall through
			if (!(t1 == PortType.Load && t2 == PortType.Discharge)) {
				return true;
			}
		}
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);
		final ITimeWindow firstTimeWindow = firstSlot.getTimeWindow();
		final ITimeWindow secondTimeWindow = secondSlot.getTimeWindow();
		if (firstTimeWindow != null && secondTimeWindow != null) {
			if (secondTimeWindow.getExclusiveEnd() <= firstTimeWindow.getInclusiveStart()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);
		final ITimeWindow firstTimeWindow = firstSlot.getTimeWindow();
		final ITimeWindow secondTimeWindow = secondSlot.getTimeWindow();
		if (firstTimeWindow != null && secondTimeWindow != null) {
			if (secondTimeWindow.getExclusiveEnd() <= firstTimeWindow.getInclusiveStart()) {
				return "Current time window is before previous time window";
			}
		}

		return null;
	}
}
