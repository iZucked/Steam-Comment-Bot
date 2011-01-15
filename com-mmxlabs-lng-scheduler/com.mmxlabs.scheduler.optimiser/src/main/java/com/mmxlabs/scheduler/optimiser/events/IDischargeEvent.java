/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events;

public interface IDischargeEvent<T> extends IPortVisitEvent<T> {

	long getDischargeVolume();

	long getSalesPrice();

}
