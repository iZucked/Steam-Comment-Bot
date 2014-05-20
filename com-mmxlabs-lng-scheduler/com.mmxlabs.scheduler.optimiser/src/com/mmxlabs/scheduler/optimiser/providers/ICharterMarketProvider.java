/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;

/**
 */
public interface ICharterMarketProvider extends IDataComponentProvider {

	public interface CharterMarketOptions {

		int getDateKey();

		// int getAvailability();

		/**
		 * When chartering out, this is the min duration a charter needs to be.
		 * 
		 * @return
		 */
		int getMinDuration();

		int getCharterPrice();
	};

	Collection<CharterMarketOptions> getCharterInOptions(IVesselClass vesselClass, int time);

	Collection<CharterMarketOptions> getCharterOutOptions(IVesselClass vesselClass, int time);

	/**
	 * Return the earliest time we can charter out from.
	 * 
	 * @return
	 */
	int getCharterOutStartTime();
}
