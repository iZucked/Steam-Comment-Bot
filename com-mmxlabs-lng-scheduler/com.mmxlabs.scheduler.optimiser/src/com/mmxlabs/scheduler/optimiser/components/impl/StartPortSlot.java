/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplierPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class StartPortSlot extends PortSlot implements IHeelOptionSupplierPortSlot {

	@NonNull
	private final IHeelOptionSupplier heelOptions;

	public StartPortSlot(@NonNull final String id, @NonNull final IPort port, @Nullable final ITimeWindow timeWindow, @Nullable final IHeelOptionSupplier heelOptions) {
		super(id, PortType.Start, port, timeWindow);
		this.heelOptions = heelOptions == null ? new HeelOptionSupplier(0, Integer.MAX_VALUE, 0, new ConstantHeelPriceCalculator(0)) : heelOptions;
	}

	@Override
	@NonNull
	public IHeelOptionSupplier getHeelOptionsSupplier() {
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
