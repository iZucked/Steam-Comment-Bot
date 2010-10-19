/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.events;

public interface IDischargeEvent<T> extends IPortVisitEvent<T> {

	long getDischargeVolume();

	long getSalesPrice();

}
