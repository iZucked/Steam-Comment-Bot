package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class MarketabilitySchedulerBuilder extends SchedulerBuilder {

	@Override
	@NonNull
	public IStartRequirement createStartRequirement(@Nullable final IPort fixedPort, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow,
			@Nullable final IHeelOptionSupplier heelOptions) {
		IStartRequirement startRequirement = super.createStartRequirement(fixedPort, hasTimeRequirement, timeWindow, heelOptions);
		return new ThreadLocalStartRequirement(startRequirement);
	}

	@Override
	public @NonNull IEndRequirement createEndRequirement(@Nullable final Collection<IPort> portSet, final boolean hasTimeRequirement, @NonNull final ITimeWindow timeWindow,
			final IHeelOptionConsumer heelConsumer) {
		IEndRequirement endRequirement = super.createEndRequirement(portSet, hasTimeRequirement, timeWindow, heelConsumer);
		return new ThreadLocalEndRequirement(endRequirement);
	}

	@Override
	@NonNull
	public IVessel createVessel(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long safetyHeelInM3, final IBaseFuel baseFuel, final IBaseFuel idleBaseFuel,
			final IBaseFuel inPortBaseFuel, final IBaseFuel pilotLightBaseFuel, final int pilotLightRate, final int warmupTimeHours, final int purgeTimeHours, final long cooldownVolumeM3,
			final int minBaseFuelConsumptionPerDay, final boolean hasReliqCapability) {

		final Vessel vessel = new Vessel(name, capacityInM3);

		vessel.setMinSpeed(minSpeed);
		vessel.setMaxSpeed(maxSpeed);

		vessel.setSafetyHeel(safetyHeelInM3);

		vessel.setWarmupTime(warmupTimeHours);
		vessel.setPurgeTime(purgeTimeHours);
		vessel.setCooldownVolume(cooldownVolumeM3);

		vessel.setPilotLightRate(pilotLightRate);
		vessel.setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionPerDay);

		vessel.setTravelBaseFuel(baseFuel);
		vessel.setIdleBaseFuel(idleBaseFuel);
		vessel.setPilotLightBaseFuel(pilotLightBaseFuel);
		vessel.setInPortBaseFuel(inPortBaseFuel);

		vessel.setHasReliqCapability(hasReliqCapability);

		final ThreadLocalVessel threadLocalVessel = new ThreadLocalVessel(vessel);
		vessels.add(threadLocalVessel);

		return threadLocalVessel;
	}

	@Override
	public void setVesselStateParameters(@NonNull final IVessel vessel, final VesselState state, final int nboRateInM3PerDay, final int idleNBORateInM3PerDay, final int idleConsumptionRateInMTPerDay,
			final IConsumptionRateCalculator consumptionRateCalculatorInMTPerDay, final int serviceSpeed, final int inPortNBORateInM3PerDay) {

		if (!vessels.contains(vessel)) {
			throw new IllegalArgumentException("IVessel was not created using this builder");
		}

		// Check instance is the same as that used in createVessel(..)
		if (vessel instanceof ThreadLocalVessel vesselEditor) {
			vesselEditor.setNBORate(state, nboRateInM3PerDay);
			vesselEditor.setIdleNBORate(state, idleNBORateInM3PerDay);
			vesselEditor.setIdleConsumptionRate(state, idleConsumptionRateInMTPerDay);
			vesselEditor.setConsumptionRate(state, consumptionRateCalculatorInMTPerDay);
			vesselEditor.setServiceSpeed(state, serviceSpeed);
			vesselEditor.setInPortNBORate(state, inPortNBORateInM3PerDay);
		} else {
			throw new IllegalArgumentException("Expected instance of " + ThreadLocalVessel.class.getCanonicalName());
		}
	}

	@Override
	public void setVesselPortTypeParameters(@NonNull final IVessel vc, final PortType portType, final int inPortConsumptionRateInMTPerDay) {
		if (vc instanceof ThreadLocalVessel tlv) {
			tlv.setInPortConsumptionRateInMTPerDay(portType, inPortConsumptionRateInMTPerDay);
		} else {
			throw new IllegalArgumentException("Expected instance of " + ThreadLocalVessel.class.getCanonicalName());
		}
	}

}
