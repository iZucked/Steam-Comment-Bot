/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public interface IPortProviderEditor extends IPortProvider {

	void setAnywherePort(IPort anywhere);

	void addPort(IPort port, String name, String mmxID);

}
