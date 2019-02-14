/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class GeneratedCharterLengthPortSlot implements IGeneratedCharterLengthEventPortSlot {

	private final String id;

	private IPort port;

	private GeneratedCharterLengthEvent event;

	public GeneratedCharterLengthPortSlot(String id, @Nullable ITimeWindow timeWindow, IPort port, 
			IHeelOptionConsumer heelConsumer, IHeelOptionSupplier heelSupplier) {
		this.id = id;
		this.event = new GeneratedCharterLengthEvent(timeWindow, port, heelConsumer, heelSupplier);
		this.port = port;
	}

	@Override
	public IGeneratedCharterLengthEvent getVesselEvent() {
		return event;
	}

	@Override
	public @NonNull IHeelOptionConsumer getHeelOptionsConsumer() {
		return getVesselEvent().getHeelOptionsConsumer();
	}

	@Override
	public IHeelOptionSupplier getHeelOptionsSupplier() {
		return getVesselEvent().getHeelOptionsSupplier();
	}

	@Override
	public void setVesselEvent(IGeneratedCharterLengthEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public IPort getPort() {
		return getVesselEvent().getEndPort();
	}

	@Override
	public @Nullable ITimeWindow getTimeWindow() {
		return getVesselEvent().getTimeWindow();
	}

	@Override
	public PortType getPortType() {
		return PortType.GeneratedCharterLength;
	}

	@Override
	public void setPort(IPort port) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTimeWindow(ITimeWindow timeWindow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GeneratedCharterLengthPortSlot) {
			return Objects.equals(event, ((GeneratedCharterLengthPortSlot) obj).getVesselEvent());
		}
		return false;
	}

	@Override
	public List<ISequenceElement> getEventSequenceElements() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IPortSlot> getEventPortSlots() {
		throw new UnsupportedOperationException();
	}
}
