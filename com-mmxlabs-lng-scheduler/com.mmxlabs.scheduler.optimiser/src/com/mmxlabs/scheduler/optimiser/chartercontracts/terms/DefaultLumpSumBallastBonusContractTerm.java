/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
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
	public boolean match(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, int vesselStartTime, IPort vesselStartPort) {

		final IPort port;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			IPortSlot slot = portTimesRecord.getReturnSlot();
			port = slot.getPort();
		} else {

			IPortSlot slot = portTimesRecord.getFirstSlot();
			if (slot.getPortType() != PortType.End) {
				return false;
			}
			port = slot.getPort();
		}

		return getRedeliveryPorts().isEmpty() || getRedeliveryPorts().contains(port);
	}

	@Override
	public long calculateCost(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, int vesselStartTime, IPort vesselStartPort) {
		IPortSlot slot = portTimesRecord.getFirstSlot();

		int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		return lumpSumCurve.getValueAtPoint(vesselEndTime);
	}

	@Override
	public @Nullable ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, int vesselStartTime, IPort vesselStartPort) {
		final LumpSumBallastBonusTermAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusTermAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateCost(portTimesRecord, vesselCharter, vesselStartTime, vesselStartPort);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
