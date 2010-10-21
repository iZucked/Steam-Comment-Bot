/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProviderEditor<T> extends IPortProvider<T> {

	void setPortForElement(IPort port, T element);
}
