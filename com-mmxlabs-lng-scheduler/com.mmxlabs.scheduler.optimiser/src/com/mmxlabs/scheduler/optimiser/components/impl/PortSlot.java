/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public abstract class PortSlot implements IPortSlot {
	private @Nullable String key;

	private @NonNull String id;

	private @NonNull IPort port;

	private @Nullable ITimeWindow timeWindow;

	private @NonNull PortType portType;

	public PortSlot(@NonNull final String id, @NonNull final PortType portType, @NonNull final IPort port, @Nullable final ITimeWindow timeWindow) {
		this.id = id;
		this.port = port;
		this.timeWindow = timeWindow;
		this.portType = portType;
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
	@NonNull
	public String getKey() {
		return (key != null) ? key : id;
	}

	public void setKey(@NonNull final String key) {
		this.key = key;
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

	public boolean doEquals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof final PortSlot slot) {

			if (!Objects.equals(id, slot.id)) {
				return false;
			}
			if (!Objects.equals(portType, slot.portType)) {
				return false;
			}
			if (!Objects.equals(port, slot.port)) {
				return false;
			}
			if (!Objects.equals(timeWindow, slot.timeWindow)) {
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
