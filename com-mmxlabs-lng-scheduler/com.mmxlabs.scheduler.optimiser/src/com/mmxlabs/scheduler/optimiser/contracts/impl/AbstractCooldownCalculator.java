/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 */
public abstract class AbstractCooldownCalculator implements ICooldownCalculator {

	protected final ICurve expressionCurve;

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	public AbstractCooldownCalculator(final ICurve expressionCurve) {
		this.expressionCurve = expressionCurve;
	}

	@Override
	public void prepareEvaluation(final ISequences sequences) {

	}

	protected int calculateUnitPriceAtUTCTime(final int localTime, final IPort port) {
		return expressionCurve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(localTime, port));
	}

	@Override
	public abstract long calculateCooldownCost(IVesselClass vesselClass, IPort port, int cv, int localTime);
}
