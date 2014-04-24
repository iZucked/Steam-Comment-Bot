/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Allow custom code to tinker with arrival times.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICustomNonShippedScheduler {

	void modifyArrivalTimes(IResource resource, int startTime, VoyagePlan currentPlan, int[] times);

}
