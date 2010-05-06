package com.mmxlabs.scheduler.optmiser.fitness;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IIdleElement extends IScheduledElement {

	IPort getPort();
}
