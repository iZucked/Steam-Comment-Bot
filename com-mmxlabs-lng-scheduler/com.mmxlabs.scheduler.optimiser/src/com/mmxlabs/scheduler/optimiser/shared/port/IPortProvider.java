/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IPortProvider extends IDataComponentProvider {

	int getNumberOfPorts();

	List<IPort> getAllPorts();

	IPort getAnywherePort();

	@Nullable
	IPort getPortForName(String name);

	@Nullable
	IPort getPortForMMXID(String mmxID);

}
