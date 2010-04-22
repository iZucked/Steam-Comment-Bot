package com.mmxlabs.scheduler.optmiser.providers;

import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IPortProvider extends IDataComponentProvider {

	IPort getPortForElement(Object element);
}
