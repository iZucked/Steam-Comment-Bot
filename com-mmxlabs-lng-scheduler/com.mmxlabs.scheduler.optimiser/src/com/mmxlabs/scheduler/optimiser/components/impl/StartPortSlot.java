/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class StartPortSlot extends PortSlot implements IHeelOptionsPortSlot {

	@NonNull
	private final IHeelOptions heelOptions;

	public StartPortSlot(@NonNull final String id, @NonNull final IPort port, @Nullable final ITimeWindow timeWindow, @Nullable final IHeelOptions heelOptions) {
		super(id, PortType.Start, port, timeWindow);
		this.heelOptions = heelOptions == null ? new HeelOptions() : heelOptions;
	}

	@Override
	@NonNull
	public IHeelOptions getHeelOptions() {
		return heelOptions;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
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
