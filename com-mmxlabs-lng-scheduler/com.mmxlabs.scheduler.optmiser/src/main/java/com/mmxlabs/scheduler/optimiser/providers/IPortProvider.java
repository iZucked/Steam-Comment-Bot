package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProvider extends IDataComponentProvider {

	IPort getPortForElement(Object element);
}
