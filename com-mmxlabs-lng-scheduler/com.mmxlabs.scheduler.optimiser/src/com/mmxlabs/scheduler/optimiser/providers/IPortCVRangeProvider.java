/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * A {@link IDataComponentProvider} providing default port CV values.
 * 
 * @author Simon Goodall
 */
public interface IPortCVRangeProvider extends IDataComponentProvider {

	/**
	 * Returns the CV value for the port instance. This will be zero if not set
	 * 
	 * @param port
	 * @return
	 */
	long getPortMinCV(IPort port);
	long getPortMaxCV(IPort port);
}
