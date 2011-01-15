/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IReturnElementProviderEditor<T> extends
		IReturnElementProvider<T> {
	public void setReturnElement(IResource resource, IPort port, T element);
}
