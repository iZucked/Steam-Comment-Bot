/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

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
	 * equivalent to no minimum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMinLoadVolume();

	/**
	 * Returns the maximum quantity that can be loaded. A value of
	 * {@link Long#MAX_VALUE} is equivalent to no maximum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMaxLoadVolume();

	/**
	 * Returns the purchase price per MMBTu of LNG.
	 * 
	 * @return
	 */
	int getPurchasePrice();

	/**
	 * Returns the CV of the cargo loaded from this slot. This will be used to
	 * convert between M3 and MMBTu of LNG.
	 * 
	 * @return
	 */
	int getCargoCVValue();
}
