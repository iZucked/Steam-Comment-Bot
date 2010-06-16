package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;

/**
 * Interface representing a particular discharge slot defined by a port, time
 * window and discharge volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDischargeSlot extends IPortSlot {
	/**
	 * Returns the minimum quantity that can be discharged.
	 * 
	 * @return
	 */
	long getMinDischargeVolume();

	/**
	 * Returns the maximum quantity that can be discharged.
	 * 
	 * @return
	 */
	long getMaxDischargeVolume();

	/**
	 * Returns sales price per unit.
	 * 
	 * @return
	 */
	long getSalesPrice();
}
