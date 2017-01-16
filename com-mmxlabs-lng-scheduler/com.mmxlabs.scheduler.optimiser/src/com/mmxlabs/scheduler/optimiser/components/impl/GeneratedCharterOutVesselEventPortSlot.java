/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class GeneratedCharterOutVesselEventPortSlot implements IGeneratedCharterOutVesselEventPortSlot {

	@NonNull
	private final String id;

	@NonNull
	private GeneratedCharterOutVesselEvent event;

	// public GeneratedCharterOutVesselEventPortSlot(@NonNull final String id, @NonNull final IPort fromPort) {
	// this.id = id;
	// this.event = new GeneratedCharterOutVesselEvent();
	// this.event.setStartPort(fromPort);
	// }

	public GeneratedCharterOutVesselEventPortSlot(@NonNull String id, IPort port, long maxCharteringRevenue, long repositioningFee, int durationInHours) {
		this.id = id;
		this.event = new GeneratedCharterOutVesselEvent();
		this.event.setStartPort(port);
		this.event.setEndPort(port);
		this.event.setHireOutRevenue(maxCharteringRevenue);
		this.event.setRepositioning(repositioningFee);
		this.event.setDurationHours(durationInHours);
	}

	@Override
	public IGeneratedCharterOutVesselEvent getVesselEvent() {
		return event;
	}

	@Override
	public IHeelOptions getHeelOptions() {
		return getVesselEvent();
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
	public ITimeWindow getTimeWindow() {
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
//		getVesselEvent().setTimeWindow(timeWindow);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GeneratedCharterOutVesselEventPortSlot) {
			return Objects.equals(event, ((GeneratedCharterOutVesselEventPortSlot) obj).getVesselEvent());
		}
		return obj == this;
	}

	@Override
	public @NonNull List<@NonNull ISequenceElement> getEventSequenceElements() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public @NonNull List<@NonNull IPortSlot> getEventPortSlots() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
