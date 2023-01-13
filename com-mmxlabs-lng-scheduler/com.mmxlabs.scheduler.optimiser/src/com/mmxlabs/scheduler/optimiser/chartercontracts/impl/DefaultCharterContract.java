/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.impl;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.chartercontracts.IBallastBonusTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IRepositioningFeeTerm;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class DefaultCharterContract implements ICharterContract {

	protected final List<IBallastBonusTerm> bbTerms;
	protected final List<IRepositioningFeeTerm> rfTerms;

	public DefaultCharterContract(final List<IBallastBonusTerm> bbTerms, final List<IRepositioningFeeTerm> rfTerms) {
		this.bbTerms = bbTerms;
		this.rfTerms = rfTerms;
	}

	@Override
	public long calculateBBCost(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselCharter vesselCharter, final VesselStartState vesselStartState,
			PreviousHeelRecord heelRecord) {
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselCharter, vesselStartState)) {
				return term.calculateCost(portTimesRecord, vesselCharter, vesselStartState, heelRecord);
			}
		}
		return 0L;
	}

	@Override
	public long calculateRFRevenue(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter) {
		for (final IRepositioningFeeTerm term : rfTerms) {
			if (term.match(portTimesRecord, vesselCharter)) {
				return term.calculateCost(portTimesRecord, vesselCharter);
			}
		}
		return 0L;
	}

	@Override
	public @Nullable ICharterContractAnnotation annotateBB(final IPortTimesRecord portTimesRecord, final IPortSlot portSlot, final IVesselCharter vesselCharter,
			final VesselStartState vesselStartState, PreviousHeelRecord heelRecord) {

		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IBallastBonusTerm term : bbTerms) {
			if (term.match(portTimesRecord, vesselCharter, vesselStartState)) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselCharter, vesselStartState, heelRecord);
				charterContractAnnotation.matchedPort = portSlot.getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselCharter, vesselStartState, heelRecord);
				break;
			}
		}
		return charterContractAnnotation;
	}

	@Override
	public @Nullable ICharterContractAnnotation annotateRF(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter) {
		final CharterContractAnnotation charterContractAnnotation = new CharterContractAnnotation();
		for (final IRepositioningFeeTerm term : rfTerms) {
			if (term.match(portTimesRecord, vesselCharter)) {
				charterContractAnnotation.cost = term.calculateCost(portTimesRecord, vesselCharter);
				charterContractAnnotation.matchedPort = portTimesRecord.getFirstSlot().getPort();
				charterContractAnnotation.termAnnotation = term.annotate(portTimesRecord, vesselCharter);
				break;
			}
		}
		return charterContractAnnotation;
	}
}
