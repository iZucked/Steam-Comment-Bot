/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * An annotation describing the amount of lateness for an element.
 * 
 */
public interface ILatenessAnnotation {

	int getAmount();

	ITimeWindow getTimeWindow();

	int getActualTime();

}
