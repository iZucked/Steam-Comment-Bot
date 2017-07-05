/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Constraint checker to prevent min/max volume violations
 * 
 * @author Alex Churchill
 *
 */
public class MinMaxVolumeChecker implements IPairwiseConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IActualsDataProvider actualsDataProvider;

	private final int maxLadenDuration = 60 * 24;

	public MinMaxVolumeChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev, cur;
		prev = cur = null;

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		return checkConstraints(sequences, changedResources);
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			return true;
		}

		if (slot1 instanceof ILoadOption && slot2 instanceof IDischargeOption) {
			ILoadOption load = (ILoadOption) slot1;
			IDischargeOption discharge = (IDischargeOption) slot2;
			if (load.getId().contains("SC_31") || discharge.getId().contains("ENARSA-T-Esc-1-1")) {
				int z = 0;
			}
			long minLoadVolumeMMBTU = load.getMinLoadVolumeMMBTU();
			long maxDischargeVolumeMMBTU = discharge.getMaxDischargeVolumeMMBTU(load.getCargoCVValue());
			if (Math.min(load.getMinLoadVolumeMMBTU(), Calculator.convertM3ToMMBTu(vesselAvailability.getVessel().getCargoCapacity(), load.getCargoCVValue())) > discharge.getMaxDischargeVolumeMMBTU(load.getCargoCVValue())) {
				// min > max
				return false;
			}
			if (load.getMaxLoadVolumeMMBTU() < discharge.getMinDischargeVolumeMMBTU(load.getCargoCVValue())) {
				// max < max
				return false;
			}
		}
		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}
