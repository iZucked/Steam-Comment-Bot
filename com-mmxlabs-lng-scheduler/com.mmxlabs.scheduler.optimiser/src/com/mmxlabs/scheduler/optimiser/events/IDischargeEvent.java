/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

public interface IDischargeEvent extends IPortVisitEvent {

	long getDischargeVolume();

	long getSalesPrice();

}
