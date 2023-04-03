/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.IPort;

@NonNullByDefault
public class CharterLengthEvent implements ICharterLengthEvent {
	private @Nullable ITimeWindow timeWindow;
	private IPort port;
	private int durationHours;

	public CharterLengthEvent(@Nullable ITimeWindow timeWindow, IPort port) {
		this.timeWindow = timeWindow;
		this.port = port;
	}

	@Override
	public void setTimeWindow(final ITimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return timeWindow;
	}
	public void setDurationHours(final int durationHours) {
		this.durationHours = durationHours;
	}

	@Override
	public IPort getStartPort() {
		return port;
	}

	@Override
	public IPort getEndPort() {
		return port;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				// this.startPort,
				// this.endPort,
				this.timeWindow);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		// DO NOT DO IDENTITY CHECK AS CONTENTS ARE MUTABLE WITH REGARDS TO HASHMAP

		if (obj instanceof CharterLengthEvent other) {
			return  true //
					// && Objects.equals(this.startPort, other.startPort) //
					// && Objects.equals(this.endPort, other.endPort) //
					&& Objects.equals(this.timeWindow, other.timeWindow); //

		}

		return false;
	}
	@Override
	public int getDurationHours() {
		return durationHours;
	}

}
