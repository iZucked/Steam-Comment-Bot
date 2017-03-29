/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class NotionalEndPortSlot extends PortSlot implements IEndPortSlot {

	private @NonNull IHeelOptionConsumer heelOptions;

	public NotionalEndPortSlot(final String id, final IPort port, final ITimeWindow timeWindow, final @NonNull IHeelOptionConsumer heelOptions) {
		super(id, PortType.End, port, timeWindow);
		this.heelOptions = heelOptions;
	}

	@Override
	public IHeelOptionConsumer getHeelOptionsConsumer() {
		return heelOptions;
	}

}
