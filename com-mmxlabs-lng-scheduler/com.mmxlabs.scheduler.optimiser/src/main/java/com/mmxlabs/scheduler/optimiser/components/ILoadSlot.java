/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;

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
	// int getPurchasePriceAtTime(int time);

	/**
	 * Returns the CV of the cargo loaded from this slot. This will be used to
	 * convert between M3 and MMBTu of LNG.
	 * 
	 * @return
	 */
	int getCargoCVValue();

	/**
	 * Returns the
	 * {@link com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator}
	 * which should be used to determine the unit cost of LNG at this slot.
	 * 
	 * @return
	 */
	ILoadPriceCalculator2 getLoadPriceCalculator();

	/**
	 * If true, {@link #isCooldownRequired()} returns true if a cooldown is to
	 * be performed at this slot if the vessel warms up, and false if a cooldown
	 * is not to be performed at this slot if it can be avoided.
	 * 
	 * Otherwise, both cooldown options will be considered
	 * 
	 * @return
	 */
	boolean isCooldownSet();

	/**
	 * If {@link #isCooldownSet()} is true, this flag constrains the cooldown
	 * decision on arrival at this port (true => cooldown will be used if
	 * necessary, false => cooldown will be avoided if at all possible)
	 * 
	 * @return
	 */
	boolean isCooldownForbidden();
}
