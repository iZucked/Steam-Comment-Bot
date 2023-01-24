/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

public interface IPortExclusionProviderEditor extends IPortExclusionProvider {

	public void setExcludedPorts(IVessel vessel, Set<IPort> excludedPorts);

}
