/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.chartercontracts.IBallastBonusTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IRepositioningFeeTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class DefaultCharterContract implements ICharterContract {

	protected final List<IBallastBonusTerm> bbTerms;
	protected final List<IRepositioningFeeTerm> rfTerms;

	public DefaultCharterContract(final List<IBallastBonusTerm> bbTerms, final List<IRepositioningFeeTerm> rfTerms) {
		this.bbTerms = bbTerms;
		this.rfTerms = rfTerms;
	}

	@Override
	public long calculateBBCost(final IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselAvailability, vesselStartTime, lastSlot.getPort())) {
				return term.calculateCost(portTimesRecord, vesselAvailability, vesselStartTime, lastSlot.getPort());
			}
		}
		return 0L;
	}
	
	@Override
	public long calculateRFRevenue(final IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		for (final IRepositioningFeeTerm term: rfTerms) {
			if (term.match(portTimesRecord, vesselAvailability)) {
				return term.calculateCost(portTimesRecord, vesselAvailability);
			}
		}
		return 0L;
	}

	@Override
	public ICharterContractAnnotation annotateBB(final IPortTimesRecord portTimesRecord, IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		
		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselAvailability, vesselStartTime, lastSlot.getPort())) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselAvailability, vesselStartTime, lastSlot.getPort());
				charterContractAnnotation.matchedPort = lastSlot.getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselAvailability, vesselStartTime, lastSlot.getPort());
				break;
			}
		}
		return charterContractAnnotation;
	}
	
	@Override
	public ICharterContractAnnotation annotateRF(final IPortTimesRecord portTimesRecord, IPort firstLoad, final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IRepositioningFeeTerm term: rfTerms) {
			if (term.match(portTimesRecord, vesselAvailability)) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselAvailability);
				charterContractAnnotation.matchedPort = lastSlot.getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselAvailability);
				break;
			}
		}
		return charterContractAnnotation;
	}
}
