/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.EnumMap;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

public final class VesselClass implements IVesselClass {

	private String name;

	private long cargoCapacity;

	private int minSpeed;

	private int maxSpeed;

	private int baseFuelUnitPrice;

	private int baseFuelConversionFactor;

	private long pilotLightRate;

	private long idlePilotLightRate;

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

	private final EnumMap<VesselState, Integer> nboSpeeds = new EnumMap<VesselState, Integer>(
			VesselState.class);

	/**
	 * Stores the price per hour to charter vessels of this class.
	 */
	private int hourlyCharterInPrice;

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
		return CollectionsUtil.getValue(idleConsumptionRate, vesselState, 0l);
	}

	@Override
	public long getIdleNBORate(final VesselState vesselState) {
		return CollectionsUtil.getValue(idleNBORate, vesselState, 0l);
	}

	@Override
	public long getMinHeel() {
		return minHeel;
	}

	@Override
	public int getMinNBOSpeed(final VesselState vesselState) {
		return CollectionsUtil.getValue(minNBOSpeed, vesselState, 0);
	}

	public void setMinNBOSpeed(final VesselState vesselState,
			final int minNBOSpeed) {
		this.minNBOSpeed.put(vesselState, minNBOSpeed);
	}

	public void setMinHeel(final long minHeel) {
		this.minHeel = minHeel;
	}

	public void setNBORate(final VesselState state, final long nboRate) {
		this.nboRate.put(state, nboRate);
	}

	@Override
	public long getNBORate(final VesselState state) {
		return CollectionsUtil.getValue(nboRate, state, 0l);
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

		return CollectionsUtil.getValue(consumptionRate, vesselState, null);
	}

	public void setConsumptionRate(final VesselState vesselState,
			final IConsumptionRateCalculator calc) {
		consumptionRate.put(vesselState, calc);
	}

	@Override
	public int getNBOSpeed(final VesselState state) {

		return CollectionsUtil.getValue(nboSpeeds, state, 0);
	}

	@Override
	public void setNBOSpeed(final VesselState vesselState, final int nboSpeed) {
		nboSpeeds.put(vesselState, nboSpeed);
	}

	@Override
	public int getBaseFuelUnitPrice() {
		return baseFuelUnitPrice;
	}

	public void setBaseFuelUnitPrice(final int baseFuelUnitPrice) {
		this.baseFuelUnitPrice = baseFuelUnitPrice;
	}

	@Override
	public int getBaseFuelConversionFactor() {
		return baseFuelConversionFactor;
	}

	public void setBaseFuelConversionFactor(int baseFuelConversionFactor) {
		this.baseFuelConversionFactor = baseFuelConversionFactor;
	}

	public void setHourlyCharterInPrice(int hourlyCharterInPrice) {
		this.hourlyCharterInPrice = hourlyCharterInPrice;
	}

	@Override
	public int getHourlyCharterInPrice() {
		return hourlyCharterInPrice;
	}

	@Override
	public final long getPilotLightRate() {
		return pilotLightRate;
	}

	public final void setPilotLightRate(long pilotLightRate) {
		this.pilotLightRate = pilotLightRate;
	}

	@Override
	public final long getIdlePilotLightRate() {
		return idlePilotLightRate;
	}

	public final void setIdlePilotLightRate(long idlePilotLightRate) {
		this.idlePilotLightRate = idlePilotLightRate;
	}
}
