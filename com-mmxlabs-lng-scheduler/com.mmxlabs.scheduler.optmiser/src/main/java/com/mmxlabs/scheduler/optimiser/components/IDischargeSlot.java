package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;

/**
 * Interface representing a particular discharge slot defined by a port, time
 * window and discharge volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDischargeSlot {

	/**
	 * The {@link IPort} this cargo is destined for.
	 * 
	 * @return
	 */
	IPort getDischargePort();

	/**
	 * The {@link ITimeWindow} in which this cargo needs to be discharged.
	 * 
	 * @return
	 */
	ITimeWindow getDischargeWindow();

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
	 * Returns an id for this discharge slot.
	 * 
	 * @return
	 */
	String getDischargeID();

	/**
	 * Returns sales price per unit.
	 * 
	 * @return
	 */
	long getSalesPrice();
}
