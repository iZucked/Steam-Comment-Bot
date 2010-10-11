package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProviderEditor<T> extends IPortProvider<T> {

	void setPortForElement(IPort port, T element);
}
