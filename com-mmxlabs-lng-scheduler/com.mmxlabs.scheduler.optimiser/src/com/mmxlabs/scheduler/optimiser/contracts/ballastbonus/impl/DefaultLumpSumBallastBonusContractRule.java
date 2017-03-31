package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.ILumpSumBallastBonusContractRule;

public class DefaultLumpSumBallastBonusContractRule extends BallastBonusContractRule implements ILumpSumBallastBonusContractRule{
	private final @NonNull ILongCurve lumpSumCurve;
	
	public DefaultLumpSumBallastBonusContractRule(Set<IPort> redeliveryPorts, final @NonNull ILongCurve lumpSumCurve) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
	}
	
	@Override
	public long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		return lumpSumCurve.getValueAtPoint(time);
	}

	@Override
	public boolean match(IPortSlot slot, IVesselAvailability vesselAvailability, int time) {
		return getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty();
	}

	@Override
	public IBallastBonusRuleAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time) {
		LumpSumBallastBonusRuleAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusRuleAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateBallastBonus(lastSlot, vesselAvailability, time);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
