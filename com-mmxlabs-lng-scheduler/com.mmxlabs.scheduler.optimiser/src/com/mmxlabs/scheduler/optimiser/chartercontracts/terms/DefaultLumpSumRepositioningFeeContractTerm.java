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
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.RepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

@NonNullByDefault
public class DefaultLumpSumRepositioningFeeContractTerm extends RepositioningFeeContractTerm {
	private final ILongCurve lumpSumCurve;

	public DefaultLumpSumRepositioningFeeContractTerm(final Set<IPort> startPorts, final ILongCurve lumpSumCurve) {
		super(startPorts);
		this.lumpSumCurve = lumpSumCurve;
	}

	@Override
	public boolean match(final IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter) {
		if (getStartPorts().isEmpty()) {
			// Matches against anything
			return true;
		}
		final IPort port = getFirstPort(vesselCharter, portTimesRecord);
		if (port != null) {
			return getStartPorts().contains(port);
		}
		return false;
	}

	protected @Nullable IPort getFirstPort(IVesselCharter vesselCharter, final IPortTimesRecord portTimesRecord) {
		IPortSlot slot = portTimesRecord.getFirstSlot();
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return slot.getPort();
		}
		if (slot.getPortType() == PortType.Start) {
			// We may be starting "anywhere", in which case we need to look at the next slot
			// to get the start port.
			if ("ANYWHERE".equalsIgnoreCase(slot.getPort().getName())) {
				if (portTimesRecord.getSlots().size() > 1) {
					slot = portTimesRecord.getSlots().get(1);
				} else {
					slot = portTimesRecord.getReturnSlot();
				}
			}

			if (slot != null) {
				return slot.getPort();
			}
		}
		return null;
	}

	@Override
	public long calculateCost(final IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter) {
		return lumpSumCurve.getValueAtPoint(portTimesRecord.getFirstSlotTime());
	}

	@Override
	public ICharterContractTermAnnotation annotate(final IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter) {
		final LumpSumRepositioningFeeTermAnnotation annotation = new LumpSumRepositioningFeeTermAnnotation();
		annotation.lumpSum = calculateCost(portTimesRecord, vesselCharter);
		annotation.matchingPort = getFirstPort(vesselCharter, portTimesRecord);

		return annotation;
	}

}
