/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;

/**
 * {@link IPairwiseConstraintChecker} to prevent the optimiser from wiring spot loads to spot discharges.
 * 
 * @author achurchill
 */
public class SpotToSpotConstraintChecker extends AbstractPairwiseConstraintChecker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private ISpotMarketSlotsProvider spotMarketSlots;

	public SpotToSpotConstraintChecker(@NonNull final String name) {
		super(name);
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource, final List<String> messages) {
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);

		if (firstSlot instanceof ILoadOption && secondSlot instanceof IDischargeOption) {
			// If data is actualised (i.e. the event has occurred), we do not care
			if (actualsDataProvider.hasActuals(firstSlot) && actualsDataProvider.hasActuals(secondSlot)) {
				return true;
			}
			if (spotMarketSlots.isSpotMarketSlot(first) && spotMarketSlots.isSpotMarketSlot(second)) {
				messages.add(explain(first, second, resource));
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);

		if (firstSlot instanceof ILoadOption && secondSlot instanceof IDischargeOption) {
			// If data is actualised (i.e. the event has occurred), we do not care
			if (actualsDataProvider.hasActuals(firstSlot) && actualsDataProvider.hasActuals(secondSlot)) {
				return String.format("%s: load slot %s and discharge slot %s are actualised.", this.name, firstSlot.getId(), secondSlot.getId());
			}
			if (spotMarketSlots.isSpotMarketSlot(first) && spotMarketSlots.isSpotMarketSlot(second)) {
				return String.format("%s: load slot %s and discharge slot %s are both spot markets.", this.name, firstSlot.getId(), secondSlot.getId());
			} else {
				return String.format("%s: load slot %s and discharge slot %s are not both spot markets.", this.name, firstSlot.getId(), secondSlot.getId());
			}
		}

		return null;
	}

}
