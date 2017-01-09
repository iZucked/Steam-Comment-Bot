/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;

/**
 * Supertype for things which can go where a load slot goes; these are actual physical load slots, or DES purchases. Anything that can bind to some discharge options is an {@link ILoadOption}.
 * 
 * @author hinton
 * 
 */
public interface ILoadOption extends IPortSlot {
	/**
	 * Returns the minimum quantity that can be loaded. A value of zero is equivalent to no minimum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMinLoadVolume();

	/**
	 * Returns the maximum quantity that can be loaded. A value of {@link Long#MAX_VALUE} is equivalent to no maximum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMaxLoadVolume();

	/**
	 * Returns the minimum quantity that can be loaded. A value of zero is equivalent to no minimum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMinLoadVolumeMMBTU();

	/**
	 * Returns the maximum quantity that can be loaded. A value of {@link Long#MAX_VALUE} is equivalent to no maximum bound. Units are M3.
	 * 
	 * @return
	 */
	long getMaxLoadVolumeMMBTU();

	/**
	 * Returns whether the input data volume was set in m3 or MMBTU
	 * 
	 * @return
	 */
	boolean isVolumeSetInM3();

	void setVolumeLimits(boolean volumeInM3, long minVolume, long maxVolume);

	/**
	 * Returns the CV of the cargo loaded from this slot. This will be used to convert between M3 and MMBTu of LNG.
	 * 
	 * @return
	 */
	int getCargoCVValue();

	/**
	 * Returns the date on which the slot pricing should be based.
	 * 
	 * @return
	 */
	int getPricingDate();

	@NonNull
	PricingEventType getPricingEvent();

	/**
	 * Returns the {@link com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator} which should be used to determine the unit cost of LNG at this slot.
	 * 
	 * @return
	 */
	ILoadPriceCalculator getLoadPriceCalculator();
}
