/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.InternalNameMapper;
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

	@Inject
	private InternalNameMapper internalNameMapper;

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
				if (messages != null) {
					messages.add(explain(first, second));
				}
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	private String explain(final ISequenceElement first, final ISequenceElement second) {
		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);

		if (firstSlot instanceof ILoadOption && secondSlot instanceof IDischargeOption) {
			// If data is actualised (i.e. the event has occurred), we do not care
			if (actualsDataProvider.hasActuals(firstSlot) && actualsDataProvider.hasActuals(secondSlot)) {
				return String.format("load slot %s and discharge slot %s are actualised.", internalNameMapper.generateString(firstSlot), internalNameMapper.generateString(secondSlot));
			}
			if (spotMarketSlots.isSpotMarketSlot(first) && spotMarketSlots.isSpotMarketSlot(second)) {
				return String.format("load slot %s and discharge slot %s are both spot markets.", internalNameMapper.generateString(firstSlot), internalNameMapper.generateString(secondSlot));
			} else {
				return String.format("load slot %s and discharge slot %s are not both spot markets.", internalNameMapper.generateString(firstSlot), internalNameMapper.generateString(secondSlot));
			}
		}

		return null;
	}

}
