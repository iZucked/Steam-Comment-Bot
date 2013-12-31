package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface INextLoadDateProvider extends IDataComponentProvider {

	/**
	 * Returns the date of the next feasible load based on the original loading, the port and time of departure (i.e. from the discharge slot) and the vessel
	 * 
	 * @param fromPort
	 * @param time
	 * @param vessel
	 * @return
	 */
	int getNextLoadDate(ILoadOption origin, IPort fromPort, int time, IVessel vessel);

}
