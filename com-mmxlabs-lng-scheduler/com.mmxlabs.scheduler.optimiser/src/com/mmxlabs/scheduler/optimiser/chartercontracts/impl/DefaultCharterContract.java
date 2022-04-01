/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
	public long calculateBBCost(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final IPort firstLoadPort) {
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselAvailability, vesselStartTime, firstLoadPort)) {
				return term.calculateCost(portTimesRecord, vesselAvailability, vesselStartTime, firstLoadPort);
			}
		}
		return 0L;
	}

	@Override
	public long calculateRFRevenue(final IPortTimesRecord portTimesRecord, final IVesselAvailability vesselAvailability) {
		for (final IRepositioningFeeTerm term : rfTerms) {
			if (term.match(portTimesRecord, vesselAvailability)) {
				return term.calculateCost(portTimesRecord, vesselAvailability);
			}
		}
		return 0L;
	}

	@Override
	public ICharterContractAnnotation annotateBB(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime,
			final IPort firstLoadPort) {

		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselAvailability, vesselStartTime, firstLoadPort)) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselAvailability, vesselStartTime, firstLoadPort);
				charterContractAnnotation.matchedPort = portSlot.getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselAvailability, vesselStartTime, firstLoadPort);
				break;
			}
		}
		return charterContractAnnotation;
	}

	@Override
	public ICharterContractAnnotation annotateRF(final IPortTimesRecord portTimesRecord, final IVesselAvailability vesselAvailability) {
		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IRepositioningFeeTerm term : rfTerms) {
			if (term.match(portTimesRecord, vesselAvailability)) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselAvailability);
				charterContractAnnotation.matchedPort = portTimesRecord.getFirstSlot().getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselAvailability);
				break;
			}
		}
		return charterContractAnnotation;
	}
}
