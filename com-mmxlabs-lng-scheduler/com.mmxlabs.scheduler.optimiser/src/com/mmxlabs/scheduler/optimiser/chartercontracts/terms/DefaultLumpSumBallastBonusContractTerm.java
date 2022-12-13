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
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
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
	public boolean match(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState) {

		final IPort port;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			final IPortSlot slot = portTimesRecord.getReturnSlot();
			port = slot.getPort();
		} else {

			final IPortSlot slot = portTimesRecord.getFirstSlot();
			if (slot.getPortType() != PortType.End) {
				return false;
			}
			port = slot.getPort();
		}

		return getRedeliveryPorts().isEmpty() || getRedeliveryPorts().contains(port);
	}

	@Override
	public long calculateCost(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState) {
		final IPortSlot slot = portTimesRecord.getFirstSlot();

		final int vesselEndTime = portTimesRecord.getFirstSlotTime() + portTimesRecord.getSlotDuration(slot);

		return lumpSumCurve.getValueAtPoint(vesselEndTime);
	}

	@Override
	public @Nullable ICharterContractTermAnnotation annotate(final IPortTimesRecord portTimesRecord, final IVesselCharter vesselCharter, final VesselStartState vesselStartState) {
		final LumpSumBallastBonusTermAnnotation lumpSumBallastBonusRuleAnnotation = new LumpSumBallastBonusTermAnnotation();
		lumpSumBallastBonusRuleAnnotation.lumpSum = calculateCost(portTimesRecord, vesselCharter, vesselStartState);
		return lumpSumBallastBonusRuleAnnotation;
	}
}
