/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;


/**
 * Interface representing a particular discharge slot defined by a port, time
 * window and discharge volumes.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDischargeSlot extends IPortSlot {
	/**
	 * Returns the minimum quantity that can be discharged. A value of zero is
	 * equivalent to no minimum bound.
	 * 
	 * @return
	 */
	long getMinDischargeVolume();

	/**
	 * Returns the maximum quantity that can be discharged. A value of
	 * {@link Long#MAX_VALUE} is equivalent to no maximum bound.
	 * 
	 * @return
	 */
	long getMaxDischargeVolume();

	/**
	 * Returns sales price per unit.
	 * 
	 * @return
	 */
	int getSalesPriceAtTime(int time);
}
