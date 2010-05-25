package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProviderEditor extends IPortProvider {

	void setPortForElement(IPort port, Object element);
}
