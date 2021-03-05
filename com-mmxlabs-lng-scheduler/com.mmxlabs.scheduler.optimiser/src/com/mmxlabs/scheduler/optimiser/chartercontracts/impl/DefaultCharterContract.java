/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class DefaultCharterContract implements ICharterContract {

	protected final List<ICharterContractTerm> terms;

	public DefaultCharterContract(final List<ICharterContractTerm> terms) {
		this.terms = terms;
	}

	@Override
	public long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		for (final ICharterContractTerm term : terms) {
			if (lastSlot.getPortType() == PortType.Start || term.match(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				return term.calculateCost(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
			}
		}
		return 0L;
	}

	@Override
	public ICharterContractAnnotation annotate(IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		
		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final ICharterContractTerm term : terms) {
			if (term.match(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime)) {
				charterContractAnnotation.cost = term.calculateCost(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
				charterContractAnnotation.matchedPort = lastSlot.getPort();
				charterContractAnnotation.termAnnotation = term.annotate(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
				break;
			}
		}
		return charterContractAnnotation;
	}
}
