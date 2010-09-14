package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IReturnElementProviderEditor<T> extends
		IReturnElementProvider<T> {
	public void setReturnElement(IPort port, T element);
}
