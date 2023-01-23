package com.mmxlabs.scheduler.optimiser.builder.impl;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionConsumer;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionSupplier;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ClampedSpeedVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.EndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.StartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.ThreadLocalStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.Vessel;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class MarketabilitySchedulerBuilder extends SchedulerBuilder {
	
	@Override
	public @NonNull IStartRequirement createStartRequirement() {
		return createStartRequirement(ANYWHERE, false, null, null);
	}
	
	@Override
	@NonNull
	public IStartRequirement createStartRequirement(@Nullable final IPort fixedPort, final boolean hasTimeRequirement, final @Nullable ITimeWindow timeWindow,
			@Nullable final IHeelOptionSupplier heelOptions) {
		StartRequirement reference = new StartRequirement(fixedPort == null ? ANYWHERE : fixedPort, fixedPort != null, hasTimeRequirement, timeWindow, heelOptions);
		return new ThreadLocalStartRequirement(reference);
	}
	
	@Override
	@NonNull
	public IEndRequirement createEndRequirement() {
		return createEndRequirement(Collections.singletonList(ANYWHERE), false, new MutableTimeWindow(0, Integer.MAX_VALUE),
				createHeelConsumer(0, 0, VesselTankState.MUST_BE_WARM, new ConstantHeelPriceCalculator(0), false));
	}

	@Override
	public @NonNull IEndRequirement createEndRequirement(@Nullable final Collection<IPort> portSet, final boolean hasTimeRequirement, @NonNull final ITimeWindow timeWindow,
			final IHeelOptionConsumer heelConsumer) {
		IEndRequirement endRequirement = null;
		if (portSet == null || portSet.isEmpty()) {
			endRequirement = new EndRequirement(Collections.singleton(ANYWHERE), false, hasTimeRequirement, timeWindow, heelConsumer);
		} else {
			endRequirement= new EndRequirement(portSet, true, hasTimeRequirement, timeWindow, heelConsumer);
		}
		return new ThreadLocalEndRequirement(endRequirement);
	}
	
//	@Override
//	@NonNull
//	public IVessel createVessel(final String name, final int minSpeed, final int maxSpeed, final long capacityInM3, final long safetyHeelInM3, final IBaseFuel baseFuel, final IBaseFuel idleBaseFuel,
//			final IBaseFuel inPortBaseFuel, final IBaseFuel pilotLightBaseFuel, final int pilotLightRate, final int warmupTimeHours, final int purgeTimeHours, final long cooldownVolumeM3,
//			final int minBaseFuelConsumptionPerDay, final boolean hasReliqCapability) {
//
//		final Vessel vessel = new Vessel(name, capacityInM3);
//
//		vessel.setMinSpeed(minSpeed);
//		vessel.setMaxSpeed(maxSpeed);
//
//		vessel.setSafetyHeel(safetyHeelInM3);
//
//		vessel.setWarmupTime(warmupTimeHours);
//		vessel.setPurgeTime(purgeTimeHours);
//		vessel.setCooldownVolume(cooldownVolumeM3);
//
//		vessel.setPilotLightRate(pilotLightRate);
//		vessel.setMinBaseFuelConsumptionInMTPerDay(minBaseFuelConsumptionPerDay);
//
//		vessel.setTravelBaseFuel(baseFuel);
//		vessel.setIdleBaseFuel(idleBaseFuel);
//		vessel.setPilotLightBaseFuel(pilotLightBaseFuel);
//		vessel.setInPortBaseFuel(inPortBaseFuel);
//
//		vessel.setHasReliqCapability(hasReliqCapability);
//		
//		final ClampedSpeedVessel clampedSpeedVessel = new ClampedSpeedVessel(vessel, maxSpeed);
//		vessels.add(clampedSpeedVessel);
//
//		return clampedSpeedVessel;
//	}
//	
//	@Override
//	public void setVesselStateParameters(@NonNull final IVessel vessel, final VesselState state, final int nboRateInM3PerDay, final int idleNBORateInM3PerDay, final int idleConsumptionRateInMTPerDay,
//			final IConsumptionRateCalculator consumptionRateCalculatorInMTPerDay, final int serviceSpeed, final int inPortNBORateInM3PerDay) {
//
//		if (!vessels.contains(vessel)) {
//			throw new IllegalArgumentException("IVessel was not created using this builder");
//		}
//
//		// Check instance is the same as that used in createVessel(..)
//		if (!(vessel instanceof Vessel)) {
//			throw new IllegalArgumentException("Expected instance of " + Vessel.class.getCanonicalName());
//		}
//
//		final Vessel veeselEditor = (Vessel) vessel;
//
//		veeselEditor.setNBORate(state, nboRateInM3PerDay);
//		veeselEditor.setIdleNBORate(state, idleNBORateInM3PerDay);
//		veeselEditor.setIdleConsumptionRate(state, idleConsumptionRateInMTPerDay);
//		veeselEditor.setConsumptionRate(state, consumptionRateCalculatorInMTPerDay);
//		veeselEditor.setServiceSpeed(state, serviceSpeed);
//		veeselEditor.setInPortNBORate(state, inPortNBORateInM3PerDay);
//	}
	
	
}
