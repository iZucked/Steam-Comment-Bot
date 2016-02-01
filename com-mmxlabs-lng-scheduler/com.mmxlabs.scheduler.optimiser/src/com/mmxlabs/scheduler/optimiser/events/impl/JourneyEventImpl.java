/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;

/**
 * Implementation of {@link IJourneyEvent}.
 * 
 * @author Simon Goodall
 * 
 */
public final class JourneyEventImpl extends AbstractFuelUsingEventImpl implements IJourneyEvent {

	private int distance;

	private IPort fromPort;

	private IPort toPort;

	private VesselState vesselState;

	private int speed;

	private String route;

	private long routeCost;

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public IPort getFromPort() {
		return fromPort;
	}

	@Override
	public IPort getToPort() {
		return toPort;
	}

	public void setDistance(final int distance) {
		this.distance = distance;
	}

	public void setFromPort(final IPort fromPort) {
		this.fromPort = fromPort;
	}

	public void setToPort(final IPort toPort) {
		this.toPort = toPort;
	}

	@Override
	public VesselState getVesselState() {
		return vesselState;
	}

	public void setVesselState(final VesselState vesselState) {
		this.vesselState = vesselState;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(final int speed) {
		this.speed = speed;
	}

	@Override
	public String getRoute() {
		return route;
	}

	public void setRoute(final String route) {
		this.route = route;
	}

	@Override
	public long getRouteCost() {
		return routeCost;
	}

	public void setRouteCost(final long routeCost) {
		this.routeCost = routeCost;
	}
}
