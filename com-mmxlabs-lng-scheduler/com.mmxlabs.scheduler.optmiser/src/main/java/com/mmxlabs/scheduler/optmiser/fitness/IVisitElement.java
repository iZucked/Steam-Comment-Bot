package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IVisitElement<T> extends IScheduledElement<T> {

	IPort getPort();
}
