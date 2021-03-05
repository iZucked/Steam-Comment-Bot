/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts.terms;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTermAnnotation;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.RepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.termannotations.LumpSumRepositioningFeeTermAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

public class DefaultLumpSumRepositioningFeeContractTerm extends RepositioningFeeContractTerm{
	private final @NonNull ILongCurve lumpSumCurve;

	public DefaultLumpSumRepositioningFeeContractTerm(final @NonNull IPort originPort, final @NonNull ILongCurve lumpSumCurve) {
		super(originPort);
		this.lumpSumCurve = lumpSumCurve;
	}
	
	@Override
	public long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return lumpSumCurve.getValueAtPoint(vesselStartTime);
	}

	@Override
	public ICharterContractTermAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		final LumpSumRepositioningFeeTermAnnotation annotation = new LumpSumRepositioningFeeTermAnnotation();
		annotation.lumpSum = calculateCost(firstLoad, lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
		annotation.originPort = getOriginPort();
		return annotation;
	}

	@Override
	public boolean match(final IPort loadPort, IPortSlot slot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return getOriginPort() == loadPort;
	}
}
