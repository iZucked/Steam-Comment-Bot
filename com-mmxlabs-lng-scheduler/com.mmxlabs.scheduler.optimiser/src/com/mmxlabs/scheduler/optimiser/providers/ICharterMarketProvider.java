/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterOutMarket;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

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
		
		int getMaxDuration();

		int getCharterPrice();

		int getCharterPrice(int date);
		
		ISpotCharterOutMarket getMarket();

		@NonNull
		Set<@NonNull IPort> getAllowedPorts();
	};

	@NonNull
	Collection<CharterMarketOptions> getCharterInOptions(@NonNull IVessel vessel, int time);

	@NonNull
	Collection<CharterMarketOptions> getCharterOutOptions(@NonNull IVessel vessel, int time);

	/**
	 * Return the earliest time we can charter out from.
	 * 
	 * @return
	 */
	int getCharterOutStartTime();

	/**
	 * Return the latest time we can charter out from.
	 * 
	 * @return
	 */
	int getCharterOutEndTime();

	@NonNull
	Set<@NonNull IPort> getCharteringPortsForVessel(@NonNull IVessel vessel);

}
