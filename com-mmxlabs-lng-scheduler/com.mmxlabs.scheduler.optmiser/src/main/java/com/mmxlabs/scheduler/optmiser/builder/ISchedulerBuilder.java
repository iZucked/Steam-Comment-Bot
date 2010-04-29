package com.mmxlabs.scheduler.optmiser.builder;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.components.IVessel;

public interface ISchedulerBuilder {

	IOptimisationData<ISequenceElement> getOptimisationData();

	IVessel createVessel(String name);

	IPort createPort(String name);

	ICargo createCargo(IPort loadPort, ITimeWindow loadWindow,
			IPort dischargePort, ITimeWindow dischargeWindow);

	ITimeWindow createTimeWindow(int start, int end);

	void setPortToPortDistance(IPort from, IPort to, int distance);
}
