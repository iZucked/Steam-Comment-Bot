/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;

/**
 * A cargo is a collection of loads and discharges. Note This is currently (20th March 2013) only required by the {@link SchedulerBuilder} to determine the end date of the optimisation. All other uses
 * can/have been replaced with the slot list.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICargo {

	/**
	 * Returns the {@link IPortSlot}s initially bound to this cargo.
	 * 
	 * @return
	 */
	List<IPortSlot> getPortSlots();

}