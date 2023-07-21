/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

@NonNullByDefault
public interface ICharterLengthEventPortSlot extends IVesselEventPortSlot{
	@Override
	ICharterLengthEvent getVesselEvent();

	void setPort(IPort port);

	void setTimeWindow(ITimeWindow port);
}
