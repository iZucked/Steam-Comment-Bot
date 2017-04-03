/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

@NonNullByDefault
public class GeneratedCharterOutVesselEventPortSlot implements IGeneratedCharterOutVesselEventPortSlot {

	private final String id;

	private GeneratedCharterOutVesselEvent event;

	public GeneratedCharterOutVesselEventPortSlot(String id, @Nullable ITimeWindow timeWindow, IPort port, long maxCharteringRevenue, long repositioningFee, int durationInHours,
			IHeelOptionConsumer heelConsumer, IHeelOptionSupplier heelSupplier) {
		this.id = id;
		this.event = new GeneratedCharterOutVesselEvent(timeWindow, port, port, heelConsumer, heelSupplier);
		this.event.setHireOutRevenue(maxCharteringRevenue);
		this.event.setRepositioning(repositioningFee);
		this.event.setDurationHours(durationInHours);
	}

	@Override
	public IGeneratedCharterOutVesselEvent getVesselEvent() {
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
	public void setVesselEvent(IGeneratedCharterOutVesselEvent event) {
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
		return PortType.GeneratedCharterOut;
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
		if (obj instanceof GeneratedCharterOutVesselEventPortSlot) {
			return Objects.equals(event, ((GeneratedCharterOutVesselEventPortSlot) obj).getVesselEvent());
		}
		return false;
	}

	@Override
	public List<ISequenceElement> getEventSequenceElements() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public List<IPortSlot> getEventPortSlots() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
