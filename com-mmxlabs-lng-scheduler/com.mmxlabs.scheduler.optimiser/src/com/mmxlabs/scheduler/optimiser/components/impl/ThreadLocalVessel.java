package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;

public class ThreadLocalVessel implements IVessel {

	private final ThreadLocal<Vessel> reference = new ThreadLocal<>();
	private final ThreadLocal<Integer> minSpeed = new ThreadLocal<>();
	private final ThreadLocal<Integer> maxSpeed = new ThreadLocal<>();
	@NonNull
	private final Vessel globalReference;

	public ThreadLocalVessel(@NonNull Vessel vessel) {
		globalReference = vessel;
	}

	public void setVessel(Vessel vessel) {
		if (vessel == null) {
			reference.remove();
		} else {
			reference.set(vessel);
		}

	}

	public void setMinSpeed(final int minSpeed) {
		this.minSpeed.set(minSpeed);
	}

	public void setMaxSpeed(final int maxSpeed) {
		this.maxSpeed.set(maxSpeed);
	}

	@Override
	public int getMaxSpeed() {
		Integer ms = maxSpeed.get();
		if (ms != null) {
			return ms;
		} else {
			return globalReference.getMaxSpeed();
		}
	}

	@Override
	public int getMinSpeed() {
		Integer ms = minSpeed.get();
		if (ms != null) {
			return ms;
		} else {
			return globalReference.getMinSpeed();
		}
	}

	public void setMinBaseFuelConsumptionInMTPerDay(final int minBaseFuelConsumptionInMTPerDay) {
		getUnderlyingVessel().setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionInMTPerDay);
	}

	public void setServiceSpeed(final VesselState vesselState, final int serviceSpeed) {
		getUnderlyingVessel().setServiceSpeed(vesselState, serviceSpeed);
	}

	public void setSafetyHeel(final long safetyHeel) {
		getUnderlyingVessel().setSafetyHeel(safetyHeel);
	}

	public void setNBORate(final VesselState state, final long nboRate) {
		getUnderlyingVessel().setNBORate(state, nboRate);
	}

	public void setIdleNBORate(final VesselState state, final long nboRate) {
		getUnderlyingVessel().setIdleNBORate(state, nboRate);
	}

	public void setIdleConsumptionRate(final VesselState state, final long rate) {
		getUnderlyingVessel().setIdleConsumptionRate(state, rate);
	}

	public void setInPortConsumptionRateInMTPerDay(final PortType portType, final long rate) {
		getUnderlyingVessel().setInPortConsumptionRateInMTPerDay(portType, rate);
	}

	public void setConsumptionRate(final VesselState vesselState, final IConsumptionRateCalculator calc) {
		getUnderlyingVessel().setConsumptionRate(vesselState, calc);
	}

	public final void setPilotLightRate(final long pilotLightRate) {
		getUnderlyingVessel().setPilotLightRate(pilotLightRate);
	}

	public void setCooldownVolume(final long cooldownVolume) {
		getUnderlyingVessel().setCooldownVolume(cooldownVolume);
	}

	public void setWarmupTime(final int warmupTime) {
		getUnderlyingVessel().setWarmupTime(warmupTime);
	}

	public void setPurgeTime(final int purgeTime) {
		getUnderlyingVessel().setPurgeTime(purgeTime);
	}

	public void setTravelBaseFuel(final IBaseFuel bf) {
		getUnderlyingVessel().setTravelBaseFuel(bf);
	}

	public void setHasReliqCapability(final boolean hasReliqCapability) {
		getUnderlyingVessel().hasReliqCapability();
	}

	public void setInPortNBORate(final VesselState state, final long nboRate) {
		getUnderlyingVessel().setInPortNBORate(state, nboRate);
	}

	@Override
	public @NonNull String getName() {
		return getUnderlyingVessel().getName();
	}

	@Override
	public long getCargoCapacity() {
		return getUnderlyingVessel().getCargoCapacity();
	}

	@Override
	public @NonNull IConsumptionRateCalculator getConsumptionRate(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getConsumptionRate(vesselState);
	}

	@Override
	public long getIdleConsumptionRate(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getIdleConsumptionRate(vesselState);
	}

	@Override
	public long getInPortConsumptionRateInMTPerDay(@NonNull PortType portType) {
		return getUnderlyingVessel().getInPortConsumptionRateInMTPerDay(portType);
	}

	@Override
	public long getIdleNBORate(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getIdleNBORate(vesselState);
	}

	@Override
	public long getPilotLightRate() {
		return getUnderlyingVessel().getPilotLightRate();
	}

	@Override
	public long getSafetyHeel() {
		return getUnderlyingVessel().getSafetyHeel();
	}

	@Override
	public int getServiceSpeed(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getServiceSpeed(vesselState);
	}

	@Override
	public long getNBORate(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getNBORate(vesselState);
	}

	@Override
	public int getWarmupTime() {
		return getUnderlyingVessel().getWarmupTime();
	}

	@Override
	public int getPurgeTime() {
		return getUnderlyingVessel().getPurgeTime();
	}

	@Override
	public long getCooldownVolume() {
		return getUnderlyingVessel().getCooldownVolume();
	}

	@Override
	public int getMinBaseFuelConsumptionInMTPerDay() {
		return getUnderlyingVessel().getMinBaseFuelConsumptionInMTPerDay();
	}

	@Override
	public @NonNull IBaseFuel getTravelBaseFuel() {
		return getUnderlyingVessel().getTravelBaseFuel();
	}

	@Override
	public @NonNull IBaseFuel getInPortBaseFuel() {
		return getUnderlyingVessel().getInPortBaseFuel();
	}

	@Override
	public void setInPortBaseFuel(@NonNull IBaseFuel baseFuel) {
		getUnderlyingVessel().setInPortBaseFuel(baseFuel);

	}

	@Override
	public @NonNull IBaseFuel getPilotLightBaseFuel() {
		return getUnderlyingVessel().getPilotLightBaseFuel();
	}

	@Override
	public void setPilotLightBaseFuel(@NonNull IBaseFuel baseFuel) {
		getUnderlyingVessel().setPilotLightBaseFuel(baseFuel);
	}

	@Override
	public @NonNull IBaseFuel getIdleBaseFuel() {
		return getUnderlyingVessel().getIdleBaseFuel();
	}

	@Override
	public void setIdleBaseFuel(@NonNull IBaseFuel baseFuel) {
		getUnderlyingVessel().setIdleBaseFuel(baseFuel);
	}

	@Override
	public long getInPortNBORate(@NonNull VesselState vesselState) {
		return getUnderlyingVessel().getInPortNBORate(vesselState);
	}

	@Override
	public boolean hasReliqCapability() {
		return getUnderlyingVessel().hasReliqCapability();
	}

	@Override
	public @NonNull FuelKey getTravelBaseFuelInMT() {
		return getUnderlyingVessel().getTravelBaseFuelInMT();
	}

	@Override
	public @NonNull FuelKey getSupplementalTravelBaseFuelInMT() {
		return getUnderlyingVessel().getSupplementalTravelBaseFuelInMT();
	}

	@Override
	public @NonNull FuelKey getIdleBaseFuelInMT() {
		return getUnderlyingVessel().getIdleBaseFuelInMT();
	}

	@Override
	public @NonNull FuelKey getPilotLightFuelInMT() {
		return getUnderlyingVessel().getPilotLightFuelInMT();
	}

	@Override
	public @NonNull FuelKey getIdlePilotLightFuelInMT() {
		return getUnderlyingVessel().getIdlePilotLightFuelInMT();
	}

	@Override
	public @NonNull FuelKey getInPortBaseFuelInMT() {
		return getUnderlyingVessel().getInPortBaseFuelInMT();
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getPortFuelKeys() {
		return getUnderlyingVessel().getPortFuelKeys();
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getTravelFuelKeys() {
		return getUnderlyingVessel().getTravelFuelKeys();
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getIdleFuelKeys() {
		return getUnderlyingVessel().getIdleFuelKeys();
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getVoyageFuelKeys() {
		return getUnderlyingVessel().getVoyageFuelKeys();
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getAllFuelKeys() {
		return getUnderlyingVessel().getAllFuelKeys();
	}

	@SuppressWarnings("null")
	private @NonNull Vessel getUnderlyingVessel() {
		if (reference.get() != null) {
			return reference.get();
		}
		return globalReference;
	}

}
