/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.ICounterPartyVolumeProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Constraint checker to prevent counterparty volume violations
 * 
 * @author FM
 *
 */
public class CounterPartyVolumeChecker implements IPairwiseConstraintChecker, IInitialSequencesConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;
	
	@Inject
	private ICounterPartyVolumeProvider counterPartyVolumeProvider;

	public CounterPartyVolumeChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}
	
	private Set<Pair<IPortSlot, IPortSlot>> pairing = new HashSet();

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
	public void setOptimisationData(@NonNull final IPhaseOptimisationData optimisationData) {

	}

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final boolean record) {

		final IPortSlot slot1 = portSlotProvider.getPortSlot(first);
		final IPortSlot slot2 = portSlotProvider.getPortSlot(second);

		// If data is actualised, we do not care
		if (actualsDataProvider.hasActuals(slot1) && actualsDataProvider.hasActuals(slot2)) {
			return true;
		}

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
			return true;
		}
		final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(resource);
		final long vesselCapacityInM3 = nominatedVessel == null ? 0 : nominatedVessel.getCargoCapacity();

		if (slot1 instanceof ILoadOption && slot2 instanceof IDischargeOption) {
			ILoadOption load = (ILoadOption) slot1;
			IDischargeOption discharge = (IDischargeOption) slot2;
			final Pair <IPortSlot, IPortSlot> thisPair = Pair.of(slot1, slot2);
			if (!pairing.contains(thisPair)){
				boolean counterPartyVolume = counterPartyVolumeProvider.isCounterPartyVolume(slot1);
				if (counterPartyVolume) {
					// load min >= discharge min
					final long minLoadVolume = vesselCapacityInM3 == 0? load.getMinLoadVolumeMMBTU() : Math.min(load.getMinLoadVolumeMMBTU(), Calculator.convertM3ToMMBTu(vesselCapacityInM3, load.getCargoCVValue()));
					final boolean minLoadLessThanMinDischarge = minLoadVolume < discharge.getMinDischargeVolumeMMBTU(load.getCargoCVValue());
					if (minLoadLessThanMinDischarge) {
						if (record) {
							pairing.add(thisPair);
						} else {
							return false;
						}
					}
					// load max <= discharge max
					final boolean maxLoadGreaterThanMaxDischarge = load.getMaxLoadVolumeMMBTU() > discharge.getMaxDischargeVolumeMMBTU(load.getCargoCVValue());
					if (maxLoadGreaterThanMaxDischarge) {
						if (record) {
							pairing.add(thisPair);
						} else {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}

	@Override
	public void sequencesAccepted(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences) {
		pairing.clear();
		final Collection<@NonNull IResource> loopResources = rawSequences.getResources();

		for (final IResource resource : loopResources) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				continue;
			}
			final ISequence sequence = rawSequences.getSequence(resource);
			if (sequence.size() == 4) {
				checkPairwiseConstraint(sequence.get(1), sequence.get(2), resource, true);
			}
		}
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource) {
		return checkPairwiseConstraint(first, second, resource, false);
	}
}
