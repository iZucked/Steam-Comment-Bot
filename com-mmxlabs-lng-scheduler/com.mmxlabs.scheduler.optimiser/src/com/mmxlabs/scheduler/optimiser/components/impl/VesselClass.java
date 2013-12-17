/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.EnumMap;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class VesselClass implements IVesselClass {

	private String name;

	private long cargoCapacity;

	private int minSpeed;

	private int maxSpeed;

	private final EnumMap<VesselState, Integer> serviceSpeed = new EnumMap<VesselState, Integer>(VesselState.class);

	private int baseFuelUnitPrice;

	private int baseFuelConversionFactor;

	private long pilotLightRate;

	private long idlePilotLightRate;

	private final EnumMap<VesselState, Integer> minNBOSpeed = new EnumMap<VesselState, Integer>(VesselState.class);

	private long minHeel;

	private final EnumMap<VesselState, Long> nboRate = new EnumMap<VesselState, Long>(VesselState.class);

	private final EnumMap<VesselState, Long> idleConsumptionRate = new EnumMap<VesselState, Long>(VesselState.class);

	private final EnumMap<PortType, Long> inPortConsumptionRate = new EnumMap<PortType, Long>(PortType.class);

	private final EnumMap<VesselState, Long> idleNBORate = new EnumMap<VesselState, Long>(VesselState.class);

	private final EnumMap<VesselState, IConsumptionRateCalculator> consumptionRate = new EnumMap<VesselState, IConsumptionRateCalculator>(VesselState.class);

	/**
	 * The volume of LNG required to cool the tanks on vessels of this class.
	 */
	private long cooldownVolume;

	/**
	 * The time in hours required for empty tanks to warm up on vessels of this class.
	 */
	private int warmupTime;

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

	public void setServiceSpeed(final VesselState vesselState, final int serviceSpeed) {
		this.serviceSpeed.put(vesselState, serviceSpeed);
	}

	@Override
	public int getServiceSpeed(final VesselState vesselState) {
		return CollectionsUtil.getValue(serviceSpeed, vesselState, 0);

	}

	@Override
	public long getIdleConsumptionRate(final VesselState vesselState) {
		return CollectionsUtil.getValue(idleConsumptionRate, vesselState, 0l);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public long getInPortConsumptionRate(final PortType portType) {
		return CollectionsUtil.getValue(inPortConsumptionRate, portType, 0l);
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

	public void setMinNBOSpeed(final VesselState vesselState, final int minNBOSpeed) {
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

	/**
	 * @since 2.0
	 */
	public void setInPortConsumptionRate(final PortType portType, final long rate) {
		this.inPortConsumptionRate.put(portType, rate);
	}

	@Override
	public IConsumptionRateCalculator getConsumptionRate(final VesselState vesselState) {

		return CollectionsUtil.getValue(consumptionRate, vesselState, null);
	}

	public void setConsumptionRate(final VesselState vesselState, final IConsumptionRateCalculator calc) {
		consumptionRate.put(vesselState, calc);
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

	public void setBaseFuelConversionFactor(final int baseFuelConversionFactor) {
		this.baseFuelConversionFactor = baseFuelConversionFactor;
	}

	@Override
	public final long getPilotLightRate() {
		return pilotLightRate;
	}

	public final void setPilotLightRate(final long pilotLightRate) {
		this.pilotLightRate = pilotLightRate;
	}

	@Override
	public final long getIdlePilotLightRate() {
		return idlePilotLightRate;
	}

	public final void setIdlePilotLightRate(final long idlePilotLightRate) {
		this.idlePilotLightRate = idlePilotLightRate;
	}

	@Override
	public int getWarmupTime() {
		return warmupTime;
	}

	@Override
	public long getCooldownVolume() {
		return cooldownVolume;
	}

	public void setCooldownVolume(final long cooldownVolume) {
		this.cooldownVolume = cooldownVolume;
	}

	public void setWarmupTime(final int warmupTime) {
		this.warmupTime = warmupTime;
	}
}
