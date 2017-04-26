/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * A {@link IDataComponentProvider} providing default port CV values.
 * 
 * @author Simon Goodall
 */
public interface IPortCVProvider extends IDataComponentProvider {

	/**
	 * Returns the CV value for the port instance. This will be zero if not set
	 * 
	 * @param port
	 * @return
	 */
	int getPortCV(@NonNull IPort port);
}
