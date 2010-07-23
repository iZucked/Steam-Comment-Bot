package com.mmxlabs.scheduler.optimiser.components;

/**
 * Interface representing a particular load slot defined by a port, time window
 * and load volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface ILoadSlot extends IPortSlot {

	/**
	 * Returns the minimum quantity that can be loaded. A value of zero is
	 * equivalent to no minimum bound.
	 * 
	 * @return
	 */
	long getMinLoadVolume();

	/**
	 * Returns the maximum quantity that can be loaded. A value of
	 * {@link Long#MAX_VALUE} is equivalent to no maximum bound.
	 * 
	 * @return
	 */
	long getMaxLoadVolume();

	/**
	 * Returns the purchase price per unit.
	 * 
	 * @return
	 */
	int getPurchasePrice();
}
