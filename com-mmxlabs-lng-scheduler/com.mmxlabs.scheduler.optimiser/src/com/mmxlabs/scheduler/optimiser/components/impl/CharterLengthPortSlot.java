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
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class CharterLengthPortSlot extends VesselEventPortSlot implements ICharterLengthEventPortSlot {
	final IPort port;
	public CharterLengthPortSlot(String id, @Nullable ITimeWindow timeWindow, IPort port) {
		this(id, timeWindow, port, new CharterLengthEvent(timeWindow, port));
	}
	public CharterLengthPortSlot(String id, @Nullable ITimeWindow timeWindow, IPort port, final CharterLengthEvent event) {
		super(id, PortType.CharterLength, port, timeWindow, event);
		this.port = port;
	}

	@Override
	public ICharterLengthEvent getVesselEvent() {
		return (ICharterLengthEvent)vesselEvent;
	}
	@Override
	public IPort getPort() {
		return port;
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return getVesselEvent().getTimeWindow();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof CharterLengthPortSlot) {
			return Objects.equals(vesselEvent, ((CharterLengthPortSlot) obj).getVesselEvent());
		}
		return false;
	}

}
