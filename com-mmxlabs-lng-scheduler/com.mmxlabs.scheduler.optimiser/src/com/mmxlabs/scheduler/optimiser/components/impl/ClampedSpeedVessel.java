/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;

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
	public int getPurgeTime() {
		return vessel.getPurgeTime();
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
	public int hashCode() {
		return vessel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return vessel.equals(obj);
	}

	@Override
	public IBaseFuel getTravelBaseFuel() {
		return vessel.getTravelBaseFuel();
	}

	@Override
	public void setTravelBaseFuel(IBaseFuel baseFuel) {
		vessel.setTravelBaseFuel(baseFuel);
	}

	@Override
	public boolean hasReliqCapability() {
		return vessel.hasReliqCapability();
	}

	@Override
	public IBaseFuel getInPortBaseFuel() {
		return vessel.getInPortBaseFuel();
	}

	@Override
	public void setInPortBaseFuel(final IBaseFuel bf) {
		vessel.setInPortBaseFuel(bf);
	}

	@Override
	public IBaseFuel getPilotLightBaseFuel() {
		return vessel.getPilotLightBaseFuel();
	}

	@Override
	public void setPilotLightBaseFuel(final IBaseFuel bf) {
		vessel.setPilotLightBaseFuel(bf);
	}

	@Override
	public IBaseFuel getIdleBaseFuel() {
		return vessel.getIdleBaseFuel();
	}

	@Override
	public void setIdleBaseFuel(final IBaseFuel bf) {
		vessel.setIdleBaseFuel(bf);
	}

	@Override
	public long getInPortNBORate(VesselState vesselState) {
		return vessel.getInPortNBORate(vesselState);
	}

	@Override
	public FuelKey getPilotLightFuelInMT() {
		return vessel.getPilotLightFuelInMT();
	}

	@Override
	public FuelKey getIdlePilotLightFuelInMT() {
		return vessel.getIdlePilotLightFuelInMT();
	}

	@Override
	public FuelKey getTravelBaseFuelInMT() {
		return vessel.getTravelBaseFuelInMT();
	}

	@Override
	public FuelKey getIdleBaseFuelInMT() {
		return vessel.getIdleBaseFuelInMT();
	}

	@Override
	public FuelKey getSupplementalTravelBaseFuelInMT() {
		return vessel.getSupplementalTravelBaseFuelInMT();
	}

	@Override
	public FuelKey getInPortBaseFuelInMT() {
		return vessel.getInPortBaseFuelInMT();
	}

	@Override
	public Collection<FuelKey> getPortFuelKeys() {
		return vessel.getPortFuelKeys();
	}

	@Override
	public Collection<FuelKey> getTravelFuelKeys() {
		return vessel.getTravelFuelKeys();
	}

	@Override
	public Collection<FuelKey> getIdleFuelKeys() {
		return vessel.getIdleFuelKeys();
	}

	@Override
	public Collection<FuelKey> getVoyageFuelKeys() {
		return vessel.getVoyageFuelKeys();
	}

	@Override
	public Collection<FuelKey> getAllFuelKeys() {
		return vessel.getAllFuelKeys();
	}
}
