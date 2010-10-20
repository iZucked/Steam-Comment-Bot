/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IPortProvider<T> extends IDataComponentProvider {

	IPort getPortForElement(T element);
}
