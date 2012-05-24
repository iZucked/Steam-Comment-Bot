/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Check that each {@link IVessel} with {@link VesselInstanceType} of {@link VesselInstanceType#VIRTUAL VIRTUAL} has a maximum of four elements on its corresponding {@link ISequence}, and that at
 * least one of those elements is an un-shipped port slot (a FOB sale or DES purchase). Other sequencing rules will ensure that the load/discharge pairing is correct.
 * 
 * @author hinton
 * 
 */
public class VirtualVesselConstraintChecker implements IConstraintChecker {
	private final String name;
	private final String vesselProviderKey;
	private final String portSlotProviderKey;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	public VirtualVesselConstraintChecker(final String name, final String vesselProviderKey, final String portSlotProviderKey) {
		super();
		this.name = name;
		this.vesselProviderKey = vesselProviderKey;
		this.portSlotProviderKey = portSlotProviderKey;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		for (final IResource resource : sequences.getResources()) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			if (vessel.getVesselInstanceType() == VesselInstanceType.VIRTUAL) {
				if (isInvalid(sequences.getSequence(resource))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check whether this sequence is valid for a virtual vessel
	 * 
	 * @param sequence
	 *            - a sequence which should be on a virtual vessel (vessel instance type is VIRTUAL)
	 * @return true if there is a problem with this sequence, false if the sequence is OK.
	 */
	private boolean isInvalid(final ISequence sequence) {
		if (sequence.size() > 4) {
			return true;
		}

		// Check that at one of the slots on the sequence is unshipped.
		boolean foundOne = false;
		for (final ISequenceElement element : sequence) {

			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			if ((portSlot instanceof ILoadOption) && !(portSlot instanceof ILoadSlot)) {
				// This check could be disabled if we allow multiple virtual slots on a route
				if (foundOne) {
					return true;
				}
				foundOne = true;

			}

			if ((portSlot instanceof IDischargeOption) && !(portSlot instanceof IDischargeSlot)) {
				// This check could be disabled if we allow multiple virtual slots on a route
				if (foundOne) {
					return true;
				}
				foundOne = true;

			}
		}

		return !foundOne;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {
		vesselProvider = optimisationData.getDataComponentProvider(vesselProviderKey, IVesselProvider.class);
		portSlotProvider = optimisationData.getDataComponentProvider(portSlotProviderKey, IPortSlotProvider.class);
	}
}
