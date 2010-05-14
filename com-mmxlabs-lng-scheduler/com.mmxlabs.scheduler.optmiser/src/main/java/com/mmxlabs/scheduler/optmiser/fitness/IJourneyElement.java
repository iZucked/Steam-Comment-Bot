package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IJourneyElement<T> extends IScheduledElement<T> {

	IPort getFromPort();

	IPort getToPort();

	int getDistance();
}
