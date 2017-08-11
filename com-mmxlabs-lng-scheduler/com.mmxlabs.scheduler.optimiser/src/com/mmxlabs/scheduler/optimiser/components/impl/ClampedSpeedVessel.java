/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class ClampedSpeedVessel implements IVessel {

	private final IVessel vessel;
	private final int maxSpeed;
	private final int minSpeed;

	public ClampedSpeedVessel(@NonNull final IVessel vessel, final int clampedSpeed) {
		this.vessel = vessel;
		this.maxSpeed = clampedSpeed;
		this.minSpeed = vessel.getMinSpeed() <= clampedSpeed ? vessel.getMinSpeed() : clampedSpeed;
	}

	@Override
	public String getName() {
		return vessel.getName();
	}

	@Override
	public long getCargoCapacity() {
		return vessel.getCargoCapacity();
	}

	@Override
	public IConsumptionRateCalculator getConsumptionRate(final VesselState vesselState) {
		return vessel.getConsumptionRate(vesselState);
	}

	@Override
	public long getIdleConsumptionRate(final VesselState vesselState) {
		return vessel.getIdleConsumptionRate(vesselState);
	}

	@Override
	public long getInPortConsumptionRateInMTPerDay(final PortType portType) {
		return vessel.getInPortConsumptionRateInMTPerDay(portType);
	}

	@Override
	public long getIdleNBORate(final VesselState vesselState) {
		return vessel.getIdleNBORate(vesselState);
	}

	@Override
	public long getPilotLightRate() {
		return vessel.getPilotLightRate();
	}

	@Override
	public long getIdlePilotLightRate() {
		return vessel.getIdlePilotLightRate();
	}

	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}

	@Override
	public long getSafetyHeel() {
		return vessel.getSafetyHeel();
	}

	@Override
	public int getMinSpeed() {
		return minSpeed;
	}

	@Override
	public int getServiceSpeed(final VesselState vesselState) {
		return vessel.getServiceSpeed(vesselState);
	}

	@Override
	public long getNBORate(final VesselState vesselState) {
		return vessel.getNBORate(vesselState);
	}

	@Override
	public int getWarmupTime() {
		return vessel.getWarmupTime();
	}

	@Override
	public long getCooldownVolume() {
		return vessel.getCooldownVolume();
	}

	@Override
	public int getMinBaseFuelConsumptionInMTPerDay() {
		return vessel.getMinBaseFuelConsumptionInMTPerDay();
	}

	@Override
	public IBaseFuel getBaseFuel() {
		return vessel.getBaseFuel();
	}

	@Override
	public void setBaseFuel(IBaseFuel baseFuel) {
		vessel.setBaseFuel(baseFuel);
	}

	@Override
	public boolean hasReliqCapability() {
		return vessel.hasReliqCapability();
	}

	@Override
	public long getInPortNBORate(VesselState vesselState) {
		return vessel.getInPortNBORate(vesselState);
	}

	@Override
	public int hashCode() {
		return vessel.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return vessel.equals(obj);
	}
}
