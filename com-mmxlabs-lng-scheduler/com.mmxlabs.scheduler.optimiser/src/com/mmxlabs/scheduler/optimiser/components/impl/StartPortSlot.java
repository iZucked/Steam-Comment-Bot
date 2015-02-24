/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class StartPortSlot extends PortSlot implements IHeelOptionsPortSlot {
	private final IHeelOptions heelOptions;

	public StartPortSlot(final String id, final IPort port, final ITimeWindow timeWindow, final IHeelOptions heelOptions) {
		super(id, port, timeWindow);
		setPortType(PortType.Start);
		this.heelOptions = heelOptions == null? new HeelOptions() : heelOptions;
	}

	public StartPortSlot(final IHeelOptions heelOptions) {
		setPortType(PortType.Start);
		this.heelOptions = heelOptions == null? new HeelOptions() : heelOptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot#getHeelOptions()
	 */
	@Override
	public IHeelOptions getHeelOptions() {
		return heelOptions;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof StartPortSlot) {
			final StartPortSlot other = (StartPortSlot) obj;

			if (super.equals(other)) {
				if (!other.heelOptions.equals(heelOptions)) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}
