/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import com.google.common.collect.Lists;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Default implementation of {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel implements IVessel {
	@NonNull
	private final String name;

	private final long cargoCapacity;

	private int minSpeed;

	private int maxSpeed;

	private final @NonNull EnumMap<VesselState, Integer> serviceSpeed = new EnumMap<>(VesselState.class);

	private long pilotLightRate;

	private long safetyHeel;

	private int minBaseFuelConsumptionInMTPerDay;

	private final @NonNull EnumMap<VesselState, Long> nboRate = new EnumMap<>(VesselState.class);

	private final @NonNull EnumMap<VesselState, Long> idleConsumptionRate = new EnumMap<>(VesselState.class);

	private final @NonNull EnumMap<PortType, Long> inPortConsumptionRateInMTPerDay = new EnumMap<>(PortType.class);

	private final @NonNull EnumMap<VesselState, Long> idleNBORate = new EnumMap<>(VesselState.class);

	private final @NonNull EnumMap<VesselState, Long> inPortNBORate = new EnumMap<>(VesselState.class);

	private final @NonNull EnumMap<VesselState, IConsumptionRateCalculator> consumptionRate = new EnumMap<>(VesselState.class);

	/**
	 * The volume of LNG required to cool the tanks on vessels of this class.
	 */
	private long cooldownVolume;

	/**
	 * The time in hours required for empty tanks to warm up on vessels of this class.
	 */
	private int warmupTime;
	
	private int purgeTime;

	private IBaseFuel baseFuel = null;
	private IBaseFuel inPortBaseFuel = null;
	private IBaseFuel pilotLightBaseFuel = null;
	private IBaseFuel idleBaseFuel = null;

	private FuelKey pilotLightFuelInMT = null;
	private FuelKey idlePilotLightFuelInMT = null;
	private FuelKey travelBaseFuelInMT = null;
	private FuelKey travelSupplementalBaseFuelInMT = null;
	private FuelKey idleBaseFuelInMT = null;
	private FuelKey inPortBaseFuelInMT = null;

	private boolean hasReliqCapability;

	public Vessel(@NonNull final String name, final long cargoCapacity) {
		this.name = name;
		this.cargoCapacity = cargoCapacity;
	}

	@Override
	@NonNull
	public String toString() {
		return getName();
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public long getCargoCapacity() {
		return cargoCapacity;
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

	/**
	 */
	public void setServiceSpeed(final VesselState vesselState, final int serviceSpeed) {
		this.serviceSpeed.put(vesselState, serviceSpeed);
	}

	/**
	 */
	@Override
	public int getServiceSpeed(final VesselState vesselState) {
		return CollectionsUtil.getValue(serviceSpeed, vesselState, 0);

	}

	@Override
	public long getIdleConsumptionRate(final VesselState vesselState) {
		return CollectionsUtil.getValue(idleConsumptionRate, vesselState, 0L);
	}

	/**
	 */
	@Override
	public long getInPortConsumptionRateInMTPerDay(final PortType portType) {
		return CollectionsUtil.getValue(inPortConsumptionRateInMTPerDay, portType, 0L);
	}

	@Override
	public long getIdleNBORate(final VesselState vesselState) {
		return CollectionsUtil.getValue(idleNBORate, vesselState, 0L);
	}

	@Override
	public long getSafetyHeel() {
		return safetyHeel;
	}

	public void setSafetyHeel(final long safetyHeel) {
		this.safetyHeel = safetyHeel;
	}

	public void setNBORate(final VesselState state, final long nboRate) {
		this.nboRate.put(state, nboRate);
	}

	@Override
	public long getNBORate(final VesselState state) {
		return CollectionsUtil.getValue(nboRate, state, 0L);
	}

	public void setIdleNBORate(final VesselState state, final long nboRate) {
		this.idleNBORate.put(state, nboRate);
	}

	public void setIdleConsumptionRate(final VesselState state, final long rate) {
		this.idleConsumptionRate.put(state, rate);
	}

	/**
	 */
	public void setInPortConsumptionRateInMTPerDay(final PortType portType, final long rate) {
		this.inPortConsumptionRateInMTPerDay.put(portType, rate);
	}

	@Override
	public IConsumptionRateCalculator getConsumptionRate(final VesselState vesselState) {

		IConsumptionRateCalculator value = CollectionsUtil.getValue(consumptionRate, vesselState, null);
		if (value == null) {
			throw new IllegalStateException();
		}
		return value;
	}

	public void setConsumptionRate(final VesselState vesselState, final IConsumptionRateCalculator calc) {
		consumptionRate.put(vesselState, calc);
	}

	@Override
	public final long getPilotLightRate() {
		return pilotLightRate;
	}

	public final void setPilotLightRate(final long pilotLightRate) {
		this.pilotLightRate = pilotLightRate;
	}

	@Override
	public int getWarmupTime() {
		return warmupTime;
	}
	
	@Override
	public int getPurgeTime() {
		return purgeTime;
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
	
	public void setPurgeTime(final int purgeTime) {
		this.purgeTime = purgeTime;
	}

	@Override
	public int getMinBaseFuelConsumptionInMTPerDay() {
		return this.minBaseFuelConsumptionInMTPerDay;
	}

	public void setMinBaseFuelConsumptionInMTPerDay(final int minBaseFuelConsumptionInMTPerDay) {
		this.minBaseFuelConsumptionInMTPerDay = minBaseFuelConsumptionInMTPerDay;
	}

	@Override
	public IBaseFuel getTravelBaseFuel() {
		return baseFuel;
	}

	@Override
	public void setTravelBaseFuel(final IBaseFuel bf) {
		baseFuel = bf;
		this.travelBaseFuelInMT = new FuelKey(FuelComponent.Base, FuelUnit.MT, bf);
		this.travelSupplementalBaseFuelInMT = new FuelKey(FuelComponent.Base_Supplemental, FuelUnit.MT, bf);
		rebuild();
	}

	@Override
	public IBaseFuel getInPortBaseFuel() {
		return inPortBaseFuel;
	}

	@Override
	public void setInPortBaseFuel(final IBaseFuel bf) {
		inPortBaseFuel = bf;
		this.inPortBaseFuelInMT = new FuelKey(FuelComponent.Base, FuelUnit.MT, bf);
		rebuild();
	}

	@Override
	public IBaseFuel getPilotLightBaseFuel() {
		return pilotLightBaseFuel;
	}

	@Override
	public void setPilotLightBaseFuel(final IBaseFuel bf) {
		pilotLightBaseFuel = bf;
		this.pilotLightFuelInMT = new FuelKey(FuelComponent.PilotLight, FuelUnit.MT, bf);
		this.idlePilotLightFuelInMT = new FuelKey(FuelComponent.IdlePilotLight, FuelUnit.MT, bf);
		rebuild();
	}

	@Override
	public IBaseFuel getIdleBaseFuel() {
		return idleBaseFuel;
	}

	@Override
	public void setIdleBaseFuel(final IBaseFuel bf) {
		idleBaseFuel = bf;
		this.idleBaseFuelInMT = new FuelKey(FuelComponent.IdleBase, FuelUnit.MT, bf);
		rebuild();
	}

	@Override
	public boolean hasReliqCapability() {
		return hasReliqCapability;
	}

	public void setHasReliqCapability(final boolean hasReliqCapability) {
		this.hasReliqCapability = hasReliqCapability;
	}

	@Override
	public long getInPortNBORate(final VesselState vesselState) {
		return CollectionsUtil.getValue(inPortNBORate, vesselState, 0L);
	}

	public void setInPortNBORate(final VesselState state, final long nboRate) {
		this.inPortNBORate.put(state, nboRate);
	}

	@Override
	public FuelKey getPilotLightFuelInMT() {
		return pilotLightFuelInMT;
	}

	@Override
	public FuelKey getIdlePilotLightFuelInMT() {
		return idlePilotLightFuelInMT;
	}

	@Override
	public FuelKey getTravelBaseFuelInMT() {
		return travelBaseFuelInMT;
	}

	@Override
	public FuelKey getIdleBaseFuelInMT() {
		return idleBaseFuelInMT;
	}

	@Override
	public FuelKey getSupplementalTravelBaseFuelInMT() {
		return travelSupplementalBaseFuelInMT;
	}

	@Override
	public FuelKey getInPortBaseFuelInMT() {
		return inPortBaseFuelInMT;
	}

	private List<FuelKey> portFuelKeys = new LinkedList<>();
	private List<FuelKey> travelFuelKeys = new LinkedList<>();
	private List<FuelKey> idleFuelKeys = new LinkedList<>();
	private List<FuelKey> voyageFuelKeys = new LinkedList<>();
	private List<FuelKey> allFuelKeys = new LinkedList<>();

	private void rebuild() {
		portFuelKeys = Lists.newArrayList(getInPortBaseFuelInMT());
		travelFuelKeys = Lists.newArrayList(getTravelBaseFuelInMT(), getSupplementalTravelBaseFuelInMT(), getPilotLightFuelInMT());
		idleFuelKeys = Lists.newArrayList(getIdleBaseFuelInMT(), getIdlePilotLightFuelInMT());
		voyageFuelKeys = Lists.newArrayList(getTravelBaseFuelInMT(), getSupplementalTravelBaseFuelInMT(), getPilotLightFuelInMT(), getIdleBaseFuelInMT(), getIdlePilotLightFuelInMT());
		allFuelKeys = Lists.newArrayList(getTravelBaseFuelInMT(), getSupplementalTravelBaseFuelInMT(), getPilotLightFuelInMT(), getIdleBaseFuelInMT(), getIdlePilotLightFuelInMT(),
				getInPortBaseFuelInMT());
	}

	@Override
	public Collection<FuelKey> getPortFuelKeys() {
		return portFuelKeys;
	}

	@Override
	public Collection<FuelKey> getTravelFuelKeys() {
		return travelFuelKeys;
	}

	@Override
	public Collection<FuelKey> getIdleFuelKeys() {
		return idleFuelKeys;
	}

	@Override
	public Collection<FuelKey> getVoyageFuelKeys() {
		return voyageFuelKeys;
	}

	@Override
	public Collection<FuelKey> getAllFuelKeys() {
		return allFuelKeys;
	}
}
