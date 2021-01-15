/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.ILumpSumBallastBonusContractRule;

public class DefaultLumpSumBallastBonusContractRule extends BallastBonusContractRule implements ILumpSumBallastBonusContractRule {
	private final @NonNull ILongCurve lumpSumCurve;

	public DefaultLumpSumBallastBonusContractRule(final Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
	}

	@Override
	public long calculateBallastBonus(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return lumpSumCurve.getValueAtPoint(vesselEndTime);
	}

	@Override
	public boolean match(final IPortSlot slot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		return getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty();
	}

	@Override
	public IBallastBonusRuleAnnotation annotate(final IPortSlot lastSlot, final IVesselAvailability vesselAvailability, final int vesselStartTime, final int vesselEndTime) {
		final LumpSumBallastBonusRuleAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusRuleAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateBallastBonus(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
