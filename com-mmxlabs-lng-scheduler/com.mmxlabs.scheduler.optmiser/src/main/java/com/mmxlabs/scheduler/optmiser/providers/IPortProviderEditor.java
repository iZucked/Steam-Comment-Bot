package com.mmxlabs.scheduler.optmiser.providers;

import com.mmxlabs.scheduler.optmiser.components.IPort;

public interface IPortProviderEditor extends IPortProvider {

	void setPortForElement(IPort port, Object element);
}
