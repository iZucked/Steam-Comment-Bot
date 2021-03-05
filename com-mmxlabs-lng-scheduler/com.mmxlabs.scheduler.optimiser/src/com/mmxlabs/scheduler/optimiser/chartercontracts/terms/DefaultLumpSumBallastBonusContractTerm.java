/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.BallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class DefaultLumpSumBallastBonusContractTerm extends BallastBonusContractTerm{
	private final @NonNull ILongCurve lumpSumCurve;

	public DefaultLumpSumBallastBonusContractTerm(final Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
	}

	@Override
	public boolean match(final IPort loadPort, final IPortSlot slot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty();
	}

	@Override
	public long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return lumpSumCurve.getValueAtPoint(vesselEndTime);
	}

	@Override
	public ICharterContractTermAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		final LumpSumBallastBonusTermAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusTermAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateCost(null, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
