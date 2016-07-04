/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
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

		int getCharterPrice(int date);

		@NonNull
		Set<@NonNull IPort> getAllowedPorts();
	};

	@NonNull
	Collection<CharterMarketOptions> getCharterInOptions(@NonNull IVesselClass vesselClass, int time);

	@NonNull
	Collection<CharterMarketOptions> getCharterOutOptions(@NonNull IVesselClass vesselClass, int time);

	/**
	 * Return the earliest time we can charter out from.
	 * 
	 * @return
	 */
	int getCharterOutStartTime();

	@NonNull
	Set<@NonNull IPort> getCharteringPortsForVesselClass(@NonNull IVesselClass vesselClass);

}
