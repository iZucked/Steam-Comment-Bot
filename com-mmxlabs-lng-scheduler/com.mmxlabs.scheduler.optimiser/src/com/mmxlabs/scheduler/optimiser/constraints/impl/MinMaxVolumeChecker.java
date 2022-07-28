/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

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
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	public MinMaxVolumeChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
		final Iterator<ISequenceElement> iter = sequence.iterator();
		ISequenceElement prev = null;
		ISequenceElement cur = null;

		while (iter.hasNext()) {
			prev = cur;
			cur = iter.next();
			if (prev != null && cur != null) {
				if (!checkPairwiseConstraint(prev, cur, resource, messages)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE || vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE) {
			return true;
		}

		if (slot1 instanceof ILoadOption && slot2 instanceof IDischargeOption) {
			ILoadOption load = (ILoadOption) slot1;
			IDischargeOption discharge = (IDischargeOption) slot2;
			if (Math.min(load.getMinLoadVolumeMMBTU(), Calculator.convertM3ToMMBTu(vesselCharter.getVessel().getCargoCapacity(), load.getCargoCVValue())) > discharge.getMaxDischargeVolumeMMBTU(load.getCargoCVValue())) {
				// min > max
				if (messages != null)
					messages.add(String.format("%s: Load %s min load volume is greater than discharge %s max discharge volume", this.name, load.getId(), discharge.getId()));
				return false;
			}
			if (load.getMaxLoadVolumeMMBTU() < discharge.getMinDischargeVolumeMMBTU(load.getCargoCVValue())) {
				// max < min
				if (messages != null)
					messages.add(String.format("%s: Load %s max load volume is less than discharge %s min discharge volume", this.name, load.getId(), discharge.getId()));
				return false;
			}
		}
		return true;
	}
}
