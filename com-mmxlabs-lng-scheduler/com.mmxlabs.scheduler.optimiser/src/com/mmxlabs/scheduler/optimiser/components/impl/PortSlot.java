/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Equality;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public abstract class PortSlot implements IPortSlot {

	private @NonNull String id;

	private @NonNull IPort port;

	private @Nullable ITimeWindow timeWindow;

	private @NonNull PortType portType;

	// public PortSlot() {
	//
	// }

	public PortSlot(@NonNull final String id, @NonNull final IPort port, @Nullable final ITimeWindow timeWindow) {
		this.id = id;
		this.port = port;
		this.timeWindow = timeWindow;
		this.portType = PortType.Unknown;
	}

	@Override
	@NonNull
	public String getId() {
		return id;
	}

	public void setId(@NonNull final String id) {
		this.id = id;
	}

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(@NonNull final IPort port) {
		this.port = port;
	}

	@Override
	@Nullable
	public ITimeWindow getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(@NonNull final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof PortSlot) {
			final PortSlot slot = (PortSlot) obj;

			if (!Equality.isEqual(id, slot.id)) {
				return false;
			}
			if (!Equality.isEqual(portType, slot.portType)) {
				return false;
			}
			if (!Equality.isEqual(port, slot.port)) {
				return false;
			}
			if (!Equality.isEqual(timeWindow, slot.timeWindow)) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public PortType getPortType() {
		return portType;
	}

	public void setPortType(@NonNull final PortType portType) {
		this.portType = portType;
	}

	@Override
	public String toString() {
		final ITimeWindow pTimeWindow = timeWindow;
		final String twStr = pTimeWindow == null ? "<any>" : String.format("<%d, %d>", pTimeWindow.getInclusiveStart(), pTimeWindow.getExclusiveEnd());
		return String.format("%s %s", id, twStr);
	}
}
