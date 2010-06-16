package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;


/**
 * Interface representing a particular load slot defined by a port, time
 * window and load volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILoadSlot {

	/**
	 * The {@link IPort} this cargo originates from.
	 * 
	 * @return
	 */
	IPort getLoadPort();

	/**
	 * The {@link ITimeWindow} in which this cargo needs to be loaded.
	 * 
	 * @return
	 */
	ITimeWindow getLoadWindow();

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
	 * Returns an id for the load slot
	 * @return
	 */
	String getLoadID();
	
	/**
	 * Returns the purchase price per unit.
	 * @return
	 */
	long getPurchasePrice();
}
