package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.EnumMap;

import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public final class VesselClass implements IVesselClass {

	private String name;

	private long cargoCapacity;

	private int minSpeed;

	private int maxSpeed;

	private final EnumMap<VesselState, Integer> minNBOSpeed = new EnumMap<VesselState, Integer>(
			VesselState.class);

	private long minHeel;

	private final EnumMap<VesselState, Long> nboRate = new EnumMap<VesselState, Long>(
			VesselState.class);

	private final EnumMap<VesselState, Long> idleConsumptionRate = new EnumMap<VesselState, Long>(
			VesselState.class);

	private final EnumMap<VesselState, Long> idleNBORate = new EnumMap<VesselState, Long>(
			VesselState.class);

	private final EnumMap<VesselState, IConsumptionRateCalculator> consumptionRate = new EnumMap<VesselState, IConsumptionRateCalculator>(
			VesselState.class);

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public long getCargoCapacity() {
		return cargoCapacity;
	}

	public void setCargoCapacity(final long cargoCapacity) {
		this.cargoCapacity = cargoCapacity;
	}

	@Override
	public int getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(final int minSpeed) {
		this.minSpeed = minSpeed;
	}

	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(final int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public long getIdleConsumptionRate(final VesselState vesselState) {
		return idleConsumptionRate.get(vesselState);
	}

	@Override
	public long getIdleNBORate(final VesselState vesselState) {
		return idleNBORate.get(vesselState);
	}

	@Override
	public long getMinHeel() {
		return minHeel;
	}

	@Override
	public int getMinNBOSpeed(VesselState vesselState) {
		return minNBOSpeed.get(vesselState);
	}

	public void setMinNBOSpeed(VesselState vesselState, int minNBOSpeed) {
		this.minNBOSpeed.put(vesselState, minNBOSpeed);
	}

	public void setMinHeel(long minHeel) {
		this.minHeel = minHeel;
	}

	public void setNBORate(final VesselState state, final long nboRate) {
		this.nboRate.put(state, nboRate);
	}

	@Override
	public long getNBORate(final VesselState state) {
		return nboRate.get(state);
	}

	public void setIdleNBORate(final VesselState state, final long nboRate) {
		this.idleNBORate.put(state, nboRate);
	}

	public void setIdleConsumptionRate(final VesselState state, final long rate) {
		this.idleConsumptionRate.put(state, rate);
	}

	@Override
	public IConsumptionRateCalculator getConsumptionRate(
			final VesselState vesselState) {
		return consumptionRate.get(vesselState);
	}

	public void setConsumptionRate(final VesselState vesselState,
			IConsumptionRateCalculator calc) {
		consumptionRate.put(vesselState, calc);
	}

}
