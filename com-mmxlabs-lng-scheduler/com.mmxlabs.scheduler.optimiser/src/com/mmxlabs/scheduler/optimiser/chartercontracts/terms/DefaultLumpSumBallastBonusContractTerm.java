/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.BallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumBallastBonusTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public class DefaultLumpSumBallastBonusContractTerm extends BallastBonusContractTerm {
	private final ILongCurve lumpSumCurve;

	public DefaultLumpSumBallastBonusContractTerm(final Set<IPort> redeliveryPorts, final ILongCurve lumpSumCurve) {
		super(redeliveryPorts);
		this.lumpSumCurve = lumpSumCurve;
	}

	@Override
	public boolean match(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPort) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		return slot.getPortType() == PortType.End && (getRedeliveryPorts().contains(slot.getPort()) || getRedeliveryPorts().isEmpty());
	}

	@Override
	public long calculateCost(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPort) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		return lumpSumCurve.getValueAtPoint(vesselEndTime);
	}

	@Override
	public @Nullable ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability, int vesselStartTime, IPort vesselStartPort) {
		final LumpSumBallastBonusTermAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusTermAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateCost(portTimesRecord, vesselAvailability, vesselStartTime, vesselStartPort);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
