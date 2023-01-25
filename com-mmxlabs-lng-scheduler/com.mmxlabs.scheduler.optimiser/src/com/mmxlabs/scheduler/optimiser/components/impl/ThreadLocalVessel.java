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

	ThreadLocal<Vessel> reference = new ThreadLocal<>();
	ThreadLocal<Integer> minSpeed = new ThreadLocal<>();
	ThreadLocal<Integer> maxSpeed = new ThreadLocal<>();
	@NonNull
	Vessel globalReference;

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

	public void setMinBaseFuelConsumptionInMTPerDay(final int minBaseFuelConsumptionInMTPerDay) {
		if (reference.get() != null) {
			reference.get().setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionInMTPerDay);
		} else {
			globalReference.setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionInMTPerDay);
		}
	}

	public void setMinSpeed(final int minSpeed) {
		this.minSpeed.set(minSpeed);
	}

	public void setMaxSpeed(final int maxSpeed) {
		this.maxSpeed.set(maxSpeed);
	}

	/**
	 */
	public void setServiceSpeed(final VesselState vesselState, final int serviceSpeed) {
		if (reference.get() != null) {
			reference.get().setServiceSpeed(vesselState, serviceSpeed);
		} else {
			globalReference.setServiceSpeed(vesselState, serviceSpeed);
		}
	}

	public void setSafetyHeel(final long safetyHeel) {
		if (reference.get() != null) {
			reference.get().setSafetyHeel(safetyHeel);
		} else {
			globalReference.setSafetyHeel(safetyHeel);
		}
	}

	public void setNBORate(final VesselState state, final long nboRate) {
		if (reference.get() != null) {
			reference.get().setNBORate(state, nboRate);
		} else {
			globalReference.setNBORate(state, nboRate);
		}
	}

	public void setIdleNBORate(final VesselState state, final long nboRate) {
		if (reference.get() != null) {
			reference.get().setIdleNBORate(state, nboRate);
		} else {
			globalReference.setIdleNBORate(state, nboRate);
		}
	}

	public void setIdleConsumptionRate(final VesselState state, final long rate) {
		if (reference.get() != null) {
			reference.get().setIdleConsumptionRate(state, rate);
		} else {
			globalReference.setIdleConsumptionRate(state, rate);
		}
	}

	public void setInPortConsumptionRateInMTPerDay(final PortType portType, final long rate) {
		if (reference.get() != null) {
			reference.get().setInPortConsumptionRateInMTPerDay(portType, rate);
		} else {
			globalReference.setInPortConsumptionRateInMTPerDay(portType, rate);
		}
	}

	public void setConsumptionRate(final VesselState vesselState, final IConsumptionRateCalculator calc) {
		if (reference.get() != null) {
			reference.get().setConsumptionRate(vesselState, calc);
		} else {
			globalReference.setConsumptionRate(vesselState, calc);
		}
	}

	public final void setPilotLightRate(final long pilotLightRate) {
		if (reference.get() != null) {
			reference.get().setPilotLightRate(pilotLightRate);
		} else {
			globalReference.setPilotLightRate(pilotLightRate);
		}
	}

	public void setCooldownVolume(final long cooldownVolume) {
		if (reference.get() != null) {
			reference.get().setCooldownVolume(cooldownVolume);
		} else {
			globalReference.setCooldownVolume(cooldownVolume);
		}
	}

	public void setWarmupTime(final int warmupTime) {
		if (reference.get() != null) {
			reference.get().setWarmupTime(warmupTime);
		} else {
			globalReference.setWarmupTime(warmupTime);
		}
	}

	public void setPurgeTime(final int purgeTime) {
		if (reference.get() != null) {
			reference.get().setPurgeTime(purgeTime);
		} else {
			globalReference.setPurgeTime(purgeTime);
		}
	}

	public void setTravelBaseFuel(final IBaseFuel bf) {
		if (reference.get() != null) {
			reference.get().setTravelBaseFuel(bf);
		} else {
			globalReference.setTravelBaseFuel(bf);
		}
	}

	public void setHasReliqCapability(final boolean hasReliqCapability) {
		if (reference.get() != null) {
			reference.get().setHasReliqCapability(hasReliqCapability);
		} else {
			globalReference.setHasReliqCapability(hasReliqCapability);
		}
	}

	public void setInPortNBORate(final VesselState state, final long nboRate) {
		if (reference.get() != null) {
			reference.get().setInPortNBORate(state, nboRate);
		} else {
			globalReference.setInPortNBORate(state, nboRate);
		}
	}

	@Override
	public @NonNull String getName() {
		if (reference.get() != null) {
			return reference.get().getName();
		} else {
			return globalReference.getName();
		}
	}

	@Override
	public long getCargoCapacity() {
		if (reference.get() != null) {
			return reference.get().getCargoCapacity();
		} else {
			return globalReference.getCargoCapacity();
		}
	}

	@Override
	public @NonNull IConsumptionRateCalculator getConsumptionRate(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getConsumptionRate(vesselState);
		} else {
			return globalReference.getConsumptionRate(vesselState);
		}
	}

	@Override
	public long getIdleConsumptionRate(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getIdleConsumptionRate(vesselState);
		} else {
			return globalReference.getIdleConsumptionRate(vesselState);
		}
	}

	@Override
	public long getInPortConsumptionRateInMTPerDay(@NonNull PortType portType) {
		if (reference.get() != null) {
			return reference.get().getInPortConsumptionRateInMTPerDay(portType);
		} else {
			return globalReference.getInPortConsumptionRateInMTPerDay(portType);
		}
	}

	@Override
	public long getIdleNBORate(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getIdleNBORate(vesselState);
		} else {
			return globalReference.getIdleNBORate(vesselState);
		}
	}

	@Override
	public long getPilotLightRate() {
		if (reference.get() != null) {
			return reference.get().getPilotLightRate();
		} else {
			return globalReference.getPilotLightRate();
		}
	}

	@Override
	public int getMaxSpeed() {
		Integer ms = maxSpeed.get();
		if(ms != null) {
			return ms;
		} else {
			return globalReference.getMaxSpeed();
		}
	}

	@Override
	public long getSafetyHeel() {
		if (reference.get() != null) {
			return reference.get().getSafetyHeel();
		} else {
			return globalReference.getSafetyHeel();
		}
	}

	@Override
	public int getMinSpeed() {
		Integer ms = minSpeed.get();
		if(ms != null) {
			return ms;
		} else {
			return globalReference.getMinSpeed();
		}
	}

	@Override
	public int getServiceSpeed(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getServiceSpeed(vesselState);
		} else {
			return globalReference.getServiceSpeed(vesselState);
		}
	}

	@Override
	public long getNBORate(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getNBORate(vesselState);
		} else {
			return globalReference.getNBORate(vesselState);
		}
	}

	@Override
	public int getWarmupTime() {
		if (reference.get() != null) {
			return reference.get().getWarmupTime();
		} else {
			return globalReference.getWarmupTime();
		}
	}

	@Override
	public int getPurgeTime() {
		if (reference.get() != null) {
			return reference.get().getPurgeTime();
		} else {
			return globalReference.getPurgeTime();
		}
	}

	@Override
	public long getCooldownVolume() {
		if (reference.get() != null) {
			return reference.get().getCooldownVolume();
		} else {
			return globalReference.getCooldownVolume();
		}
	}

	@Override
	public int getMinBaseFuelConsumptionInMTPerDay() {
		if (reference.get() != null) {
			return reference.get().getMinBaseFuelConsumptionInMTPerDay();
		} else {
			return globalReference.getMinBaseFuelConsumptionInMTPerDay();
		}
	}

	@Override
	public @NonNull IBaseFuel getTravelBaseFuel() {
		if (reference.get() != null) {
			return reference.get().getTravelBaseFuel();
		} else {
			return globalReference.getTravelBaseFuel();
		}
	}

	@Override
	public @NonNull IBaseFuel getInPortBaseFuel() {
		if (reference.get() != null) {
			return reference.get().getInPortBaseFuel();
		} else {
			return globalReference.getInPortBaseFuel();
		}
	}

	@Override
	public void setInPortBaseFuel(@NonNull IBaseFuel baseFuel) {
		if (reference.get() != null) {
			reference.get().setInPortBaseFuel(baseFuel);
		} else {
			globalReference.setInPortBaseFuel(baseFuel);
		}

	}

	@Override
	public @NonNull IBaseFuel getPilotLightBaseFuel() {
		if (reference.get() != null) {
			return reference.get().getPilotLightBaseFuel();
		} else {
			return globalReference.getPilotLightBaseFuel();
		}
	}

	@Override
	public void setPilotLightBaseFuel(@NonNull IBaseFuel baseFuel) {
		if (reference.get() != null) {
			reference.get().setPilotLightBaseFuel(baseFuel);
		} else {
			globalReference.setPilotLightBaseFuel(baseFuel);
		}
	}

	@Override
	public @NonNull IBaseFuel getIdleBaseFuel() {
		if (reference.get() != null) {
			return reference.get().getIdleBaseFuel();
		} else {
			return globalReference.getIdleBaseFuel();
		}
	}

	@Override
	public void setIdleBaseFuel(@NonNull IBaseFuel baseFuel) {
		if (reference.get() != null) {
			reference.get().setIdleBaseFuel(baseFuel);
		} else {
			globalReference.setIdleBaseFuel(baseFuel);
		}

	}

	@Override
	public long getInPortNBORate(@NonNull VesselState vesselState) {
		if (reference.get() != null) {
			return reference.get().getInPortNBORate(vesselState);
		} else {
			return globalReference.getInPortNBORate(vesselState);
		}
	}

	@Override
	public boolean hasReliqCapability() {
		if (reference.get() != null) {
			return reference.get().hasReliqCapability();
		} else {
			return globalReference.hasReliqCapability();
		}
	}

	@Override
	public @NonNull FuelKey getTravelBaseFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getTravelBaseFuelInMT();
		} else {
			return globalReference.getTravelBaseFuelInMT();
		}
	}

	@Override
	public @NonNull FuelKey getSupplementalTravelBaseFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getSupplementalTravelBaseFuelInMT();
		} else {
			return globalReference.getSupplementalTravelBaseFuelInMT();
		}
	}

	@Override
	public @NonNull FuelKey getIdleBaseFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getIdleBaseFuelInMT();
		} else {
			return globalReference.getIdleBaseFuelInMT();
		}
	}

	@Override
	public @NonNull FuelKey getPilotLightFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getPilotLightFuelInMT();
		} else {
			return globalReference.getPilotLightFuelInMT();
		}
	}

	@Override
	public @NonNull FuelKey getIdlePilotLightFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getIdlePilotLightFuelInMT();
		} else {
			return globalReference.getIdlePilotLightFuelInMT();
		}
	}

	@Override
	public @NonNull FuelKey getInPortBaseFuelInMT() {
		if (reference.get() != null) {
			return reference.get().getInPortBaseFuelInMT();
		} else {
			return globalReference.getInPortBaseFuelInMT();
		}
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getPortFuelKeys() {
		if (reference.get() != null) {
			return reference.get().getPortFuelKeys();
		} else {
			return globalReference.getPortFuelKeys();
		}
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getTravelFuelKeys() {
		if (reference.get() != null) {
			return reference.get().getTravelFuelKeys();
		} else {
			return globalReference.getTravelFuelKeys();
		}
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getIdleFuelKeys() {
		if (reference.get() != null) {
			return reference.get().getIdleFuelKeys();
		} else {
			return globalReference.getIdleFuelKeys();
		}
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getVoyageFuelKeys() {
		if (reference.get() != null) {
			return reference.get().getVoyageFuelKeys();
		} else {
			return globalReference.getVoyageFuelKeys();
		}
	}

	@Override
	public @NonNull Collection<@NonNull FuelKey> getAllFuelKeys() {
		if (reference.get() != null) {
			return reference.get().getAllFuelKeys();
		} else {
			return globalReference.getAllFuelKeys();
		}
	}

}
