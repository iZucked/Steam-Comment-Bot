/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;

/**
 * This interface defines a Port, a physical location that can be used as a source or destination point for travel.
 * 
 * @author Simon Goodall
 * 
 */
public interface IPort extends IIndexedObject {

	/**
	 * The name of the port.
	 * 
	 * @return the port's name
	 */
	String getName();

	/**
	 * Some ports do not typically provide cooldown facilities to inbound vessels. These ports will return true here, indicating that vessels should arrive cold. If a vessel <em>cannot</em> arrive
	 * cold, because its earlier journey does not allow any LNG to load, cooldown may still be performed.
	 * 
	 * @return true if cooldown is not routinely provided at this port.
	 */
	boolean shouldVesselsArriveCold();

	/**
	 * A {@link ICooldownPriceCalculator} used for pricing cooldown gas.
	 * 
	 * @return
	 */
	ICooldownPriceCalculator getCooldownPriceCalculator();
}
