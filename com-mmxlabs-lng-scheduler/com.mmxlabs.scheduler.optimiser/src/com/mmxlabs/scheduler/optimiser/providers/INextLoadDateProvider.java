/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface INextLoadDateProvider extends IDataComponentProvider {

	public interface INextLoadDate {

		/**
		 * Returns the next feasible load time.
		 * 
		 * @return
		 */
		int getTime();

		/**
		 * Returns the next feasible load slot, or null if not available
		 * 
		 * @return
		 */
		@Nullable
		ILoadOption getNextSlot();
	}

	/**
	 * Returns the date of the next feasible load based on the original loading, the port and time of departure (i.e. from the discharge slot) and the vessel
	 * 
	 * @param fromPort
	 * @param time
	 * @param vessel
	 * @return
	 */
	@NonNull
	INextLoadDate getNextLoadDate(@NonNull ILoadOption origin, @NonNull IPort fromPort, int time, @NonNull IVessel vessel);
}
