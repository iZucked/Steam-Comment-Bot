/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class StartPortSlot extends PortSlot implements IHeelOptionsPortSlot {
	private final int heelUnitPrice, heelCVValue;
	private final long heelLimit;
	private final IHeelOptions heelOptions = new IHeelOptions() {
		@Override
		public int getHeelUnitPrice() {
			return heelUnitPrice;
		}

		@Override
		public long getHeelLimit() {
			return heelLimit;
		}

		@Override
		public int getHeelCVValue() {
			return heelCVValue;
		}
	};

	public StartPortSlot(final String id, final IPort port, final ITimeWindow timeWindow, final long heelLimit, final int heelUnitPrice, final int heelCVValue) {
		super(id, port, timeWindow);
		setPortType(PortType.Start);
		this.heelLimit = heelLimit;
		this.heelUnitPrice = heelUnitPrice;
		this.heelCVValue = heelCVValue;
	}

	public StartPortSlot(final long heelLimit, final int heelUnitPrice, final int heelCVValue) {
		setPortType(PortType.Start);
		this.heelLimit = heelLimit;
		this.heelUnitPrice = heelUnitPrice;
		this.heelCVValue = heelCVValue;
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
				if (other.heelCVValue != heelCVValue) {
					return false;
				}
				if (other.heelLimit != heelLimit) {
					return false;
				}
				if (other.heelUnitPrice != heelUnitPrice) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}
