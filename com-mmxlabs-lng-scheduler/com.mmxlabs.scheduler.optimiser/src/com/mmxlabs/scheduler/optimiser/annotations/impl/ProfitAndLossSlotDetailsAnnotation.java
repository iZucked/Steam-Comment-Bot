/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossSlotDetailsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public final class ProfitAndLossSlotDetailsAnnotation implements IProfitAndLossSlotDetailsAnnotation {
	private final IDetailTree details;
	private final IPortSlot portSlot;

	public ProfitAndLossSlotDetailsAnnotation(final IPortSlot portSlot, final IDetailTree details) {
		this.portSlot = portSlot;
		this.details = details;
	}

	@Override
	public IDetailTree getDetails() {
		return details;
	}

	@Override
	public IPortSlot getPortSlot() {
		return portSlot;
	}
}
