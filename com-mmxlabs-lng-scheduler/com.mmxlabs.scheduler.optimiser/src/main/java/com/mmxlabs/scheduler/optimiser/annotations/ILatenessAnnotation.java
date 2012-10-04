package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.optimiser.common.components.ITimeWindow;

/**
 * An annotation describing the amount of lateness for an element.
 * 
 * @since 2.0
 */
public interface ILatenessAnnotation {

	int getAmount();

	ITimeWindow getTimeWindow();

	int getActualTime();

}
