/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

class ClampedSpeedVesselClass implements IVesselClass {

	private final IVesselClass vesselClass;
	private final int maxSpeed;
	private final int minSpeed;
	
	public ClampedSpeedVesselClass(@NonNull final IVesselClass vesselClass, final int clampedSpeed) {
		this.vesselClass = vesselClass;
		this.maxSpeed = clampedSpeed;
		this.minSpeed = vesselClass.getMinSpeed() <= clampedSpeed ? vesselClass.getMinSpeed() : clampedSpeed ;
		
	}

	@Override
	public String getName() {
		return vesselClass.getName();
	}

	@Override
	public long getCargoCapacity() {
		return vesselClass.getCargoCapacity();
	}

	@Override
	public IConsumptionRateCalculator getConsumptionRate(final VesselState vesselState) {
		return vesselClass.getConsumptionRate(vesselState);
	}

	@Override
	public long getIdleConsumptionRate(final VesselState vesselState) {
		return vesselClass.getIdleConsumptionRate(vesselState);
	}

	@Override
	public long getInPortConsumptionRateInMTPerDay(final PortType portType) {
		return vesselClass.getInPortConsumptionRateInMTPerDay(portType);
	}

	@Override
	public long getIdleNBORate(final VesselState vesselState) {
		return vesselClass.getIdleNBORate(vesselState);
	}

	@Override
	public long getPilotLightRate() {
		return vesselClass.getPilotLightRate();
	}

	@Override
	public long getIdlePilotLightRate() {
		return vesselClass.getIdlePilotLightRate();
	}

	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}

	@Override
	public long getSafetyHeel() {
		return vesselClass.getSafetyHeel();
	}

	@Override
	public int getMinSpeed() {
		return minSpeed;
	}

	@Override
	public int getServiceSpeed(final VesselState vesselState) {
		return vesselClass.getServiceSpeed(vesselState);
	}

	@Override
	public long getNBORate(final VesselState vesselState) {
		return vesselClass.getNBORate(vesselState);
	}

//	@Override
//	public int getBaseFuelConversionFactor() {
//		return vesselClass.getBaseFuelConversionFactor();
//	}

	@Override
	public int getWarmupTime() {
		return vesselClass.getWarmupTime();
	}

	@Override
	public long getCooldownVolume() {
		return vesselClass.getCooldownVolume();
	}

	@Override
	public int getMinBaseFuelConsumptionInMTPerDay() {
		return vesselClass.getMinBaseFuelConsumptionInMTPerDay();
	}

	@Override
	public int hashCode() {
		return vesselClass.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return vesselClass.equals(obj);
	}
	
	@Override
	public IBaseFuel getBaseFuel() {
		return vesselClass.getBaseFuel();
	}
	
	@Override
	public void setBaseFuel(IBaseFuel baseFuel) {
		vesselClass.setBaseFuel(baseFuel);
	}

	@Override
	public boolean hasReliqCapability() {
		return vesselClass.hasReliqCapability();
	}

	@Override
	public long getInPortNBORate(VesselState vesselState) {
		return vesselClass.getInPortNBORate(vesselState);
	}

}
