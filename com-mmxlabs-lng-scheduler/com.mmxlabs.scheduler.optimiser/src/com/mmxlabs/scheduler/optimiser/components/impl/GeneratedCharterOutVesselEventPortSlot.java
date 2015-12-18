/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEvent;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class GeneratedCharterOutVesselEventPortSlot implements IGeneratedCharterOutVesselEventPortSlot {

	@Inject
	@NonNull
	private Provider<GeneratedVesselEventFactory> eventFactory;

	@NonNull
	private String id;

	@NonNull
	private IPort fromPort;

	public GeneratedCharterOutVesselEventPortSlot(@NonNull final String id, @NonNull final IPort fromPort) {
		this.id = id;
		this.fromPort = fromPort;
	}

	@Override
	public IGeneratedCharterOutVesselEvent getVesselEvent() {
		return eventFactory.get().getEvent(this, fromPort);
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
		getVesselEvent().setEndPort(port);
	}

	@Override
	public void setTimeWindow(ITimeWindow timeWindow) {
		getVesselEvent().setTimeWindow(timeWindow);
	}

	@Override
	public void setId(String id) {
		// TODO: Is changing this thread safe?
		this.id = id;
	}
}
