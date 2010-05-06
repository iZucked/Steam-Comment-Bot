package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IJourneyElement extends IScheduledElement {

	IPort getFromPort();

	IPort getToPort();

	int getDistance();
}
