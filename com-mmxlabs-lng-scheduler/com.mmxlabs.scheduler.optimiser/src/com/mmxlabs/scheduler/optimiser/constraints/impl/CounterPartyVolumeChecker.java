/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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

	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final boolean record,
			final List<String> messages) {

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
			final Pair<IPortSlot, IPortSlot> thisPair = Pair.of(slot1, slot2);
			if (!pairing.contains(thisPair)) {
				boolean counterPartyVolume = counterPartyVolumeProvider.isCounterPartyVolume(slot1);
				if (counterPartyVolume) {
					// load min >= discharge min
					final long minLoadVolume = vesselCapacityInM3 == 0 ? load.getMinLoadVolumeMMBTU()
							: Math.min(load.getMinLoadVolumeMMBTU(), Calculator.convertM3ToMMBTu(vesselCapacityInM3, load.getCargoCVValue()));
					final long minDischargeVolume = discharge.getMinDischargeVolumeMMBTU(load.getCargoCVValue());
					final boolean minLoadLessThanMinDischarge = minLoadVolume < minDischargeVolume;
					if (minLoadLessThanMinDischarge) {
						if (record) {
							pairing.add(thisPair);
						} else {
							if (messages != null)
								messages.add(String.format(
									"%s: Counterparty volume nomination for buy %s and sell %s. Min load volume (%d) "
									+ "is less than min discharge volume (%d). Nominated vessel (%s) capacity is %d"
									, this.name, load.getId(), discharge.getId(), minLoadVolume, minDischargeVolume, 
									nominatedVessel == null? "N/A" : nominatedVessel.getName(), vesselCapacityInM3));
							return false;
						}
					}
					// load max <= discharge max
					final long maxDischargeVolume = discharge.getMaxDischargeVolumeMMBTU(load.getCargoCVValue());
					final long maxLoadVolume = load.getMaxLoadVolumeMMBTU();
					final boolean maxLoadGreaterThanMaxDischarge = maxLoadVolume > maxDischargeVolume;
					if (maxLoadGreaterThanMaxDischarge) {
						if (record) {
							pairing.add(thisPair);
						} else {
							if (messages != null)
								messages.add(String.format(
									"%s: Counterparty volume nomination for buy %s and sell %s. Max load volume (%d) "
									+ "is greater than max discharge volume (%d). Nominated vessel (%s) capacity is %d"
									, this.name, load.getId(), discharge.getId(), maxLoadVolume, maxDischargeVolume, 
									nominatedVessel == null? "N/A" : nominatedVessel.getName(), vesselCapacityInM3));
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void sequencesAccepted(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, final List<String> messages) {
		pairing.clear();
		final Collection<@NonNull IResource> loopResources = rawSequences.getResources();

		for (final IResource resource : loopResources) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.DES_PURCHASE) {
				continue;
			}
			final ISequence sequence = rawSequences.getSequence(resource);
			if (sequence.size() == 4) {
				checkPairwiseConstraint(sequence.get(1), sequence.get(2), resource, true, messages);
			}
		}
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource, final List<String> messages) {
		return checkPairwiseConstraint(first, second, resource, false, messages);
	}
}
