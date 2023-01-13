/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

@NonNullByDefault
public interface IGeneratedCharterLengthEvent extends IVesselEvent {
	/**
	 * Set time window in which the vessel must arrive at the start port
	 * 
	 * @return
	 */
	public void setTimeWindow(ITimeWindow timeWindow);

	/**
	 * Set the port for the event
	 * 
	 * @return
	 */
	public void setPort(IPort port);

	void setHeelConsumer(IHeelOptionConsumer heelConsumer);

	void setHeelSupplier(IHeelOptionSupplier heelSupplier);

	IHeelOptionSupplier getHeelOptionsSupplier();

	IHeelOptionConsumer getHeelOptionsConsumer();
}
