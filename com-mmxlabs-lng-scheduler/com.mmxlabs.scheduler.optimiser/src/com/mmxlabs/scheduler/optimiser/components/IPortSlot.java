/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Represents a single time slot at a given port.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPortSlot {
	/**
	 * FIXME! Bad constant, *could* clash with real time unit.
	 */
	public static final int NO_PRICING_DATE = -1000;

	@NonNull
	String getId();

	@NonNull
	IPort getPort();

//	@Nullable
	ITimeWindow getTimeWindow();

	@NonNull
	PortType getPortType();
}
