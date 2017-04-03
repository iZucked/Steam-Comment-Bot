/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * Represents a generated vessel event such as a charter out or dry-dock visit
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
public interface IGeneratedCharterOutVesselEvent extends ICharterOutVesselEvent {
	/**
	 * Set time window in which the vessel must arrive at the start port
	 * 
	 * @return
	 */
	public void setTimeWindow(ITimeWindow timeWindow);

	/**
	 * Set he duration in hours of the event
	 * 
	 * @return
	 */
	public void setDurationHours(int time);

	/**
	 * Set the port at which this vessel event starts
	 * 
	 * @return
	 */
	public void setStartPort(IPort port);

	/**
	 * Set the port at which the vessel will be after the event
	 * 
	 * @return
	 */
	public void setEndPort(IPort port);

	/**
	 * Set the total hire cost for charter out events.
	 * 
	 */
	@Override
	void setHireOutRevenue(long hireCost);

	void setHeelConsumer(IHeelOptionConsumer heelConsumer);

	void setHeelSupplier(IHeelOptionSupplier heelSupplier);
}
