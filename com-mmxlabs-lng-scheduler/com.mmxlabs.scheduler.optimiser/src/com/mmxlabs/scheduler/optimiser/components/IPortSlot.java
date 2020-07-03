/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

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
	 * Constant to indicate pricing date has not been set. See #getPricingDate in
	 * the {@link ILoadSlot} and {@link IDischargeSlot} interfaces.
	 */
	public static final int NO_PRICING_DATE = Integer.MIN_VALUE;

	@NonNull
	String getId();

	/** Return an equivalence key used in certain types of cache */ 
	default @NonNull String getKey() {
		return getId();
	}

	void setKey(@NonNull String key);

	@NonNull
	IPort getPort();

	// @Nullable
	ITimeWindow getTimeWindow();

	@NonNull
	PortType getPortType();
}
