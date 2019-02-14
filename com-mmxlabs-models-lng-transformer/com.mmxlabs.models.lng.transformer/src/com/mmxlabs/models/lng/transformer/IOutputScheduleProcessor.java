/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * Interface to apply further changes to a {@link Schedule} instance once created, but prior to attaching to model (i.e. model can be directly manipulated).
 */
public interface IOutputScheduleProcessor {

	/**
	 */
	void process(Schedule scheduleModel);
}
