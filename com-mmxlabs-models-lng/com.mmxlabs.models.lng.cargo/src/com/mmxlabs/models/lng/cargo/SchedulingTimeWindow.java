/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.types.TimePeriod;

public interface SchedulingTimeWindow {
	/**
	 * Inclusive start.
	 * @return
	 */
	ZonedDateTime getStart();
	
	/**
	 * Inclusive end.
	 * ZonedDateTime getEnd();
	 * @return
	 */
	ZonedDateTime getEnd();
	
	/**
	 * Inclusive start.
	 * @return
	 */
	ZonedDateTime getStartWithFlex();
	
	/**
	 * Inclusive end.
	 * @return
	 */
	ZonedDateTime getEndWithFlex();
	
	int getSize();
	TimePeriod getSizeUnits();
	
	/**
	 * Get the duration in hours.
	 */
	int getDuration();
	int getSizeInHours();
}
