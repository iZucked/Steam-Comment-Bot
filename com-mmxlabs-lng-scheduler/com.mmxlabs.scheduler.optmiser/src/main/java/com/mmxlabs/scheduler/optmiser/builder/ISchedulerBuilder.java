package com.mmxlabs.scheduler.optmiser.builder;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.components.IVessel;

/**
 * A builder to create {@link IOptimisationData} instances for Scheduler
 * problems.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISchedulerBuilder {

	IOptimisationData<ISequenceElement> getOptimisationData();

	IVessel createVessel(String name);

	IPort createPort(String name);

	ICargo createCargo(IPort loadPort, ITimeWindow loadWindow,
			IPort dischargePort, ITimeWindow dischargeWindow);

	ITimeWindow createTimeWindow(int start, int end);

	void setPortToPortDistance(IPort from, IPort to, int distance);

	void setElementDurations(ISequenceElement element, IResource resource,
			int duration);

	void dispose();
}
