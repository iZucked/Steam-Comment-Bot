package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;


/**
 * Interface representing a particular load slot defined by a port, time
 * window and load volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILoadSlot extends IPortSlot {

	/**
	 * Returns the minimum quantity that can be loaded.
	 * @return
	 */
	long getMinLoadVolume();

	/**
	 * Returns the maximum quantity that can be loaded.
	 * @return
	 */
	long getMaxLoadVolume();

	/**
	 * Returns the purchase price per unit.
	 * @return
	 */
	long getPurchasePrice();
}
