/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Route;

public class FleetModelBuilder {
	private @NonNull final FleetModel fleetModel;

	public FleetModelBuilder(@NonNull final FleetModel fleetModel) {
		this.fleetModel = fleetModel;
	}

	public @NonNull FleetModel getFleetModel() {
		return fleetModel;
	}

	@NonNull
	public Vessel createVessel(@NonNull final String name, @NonNull final VesselClass vesselClass) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		vessel.setVesselClass(vesselClass);

		fleetModel.getVessels().add(vessel);

		return vessel;
	}

	@NonNull
	public Vessel createVessel(@NonNull final String name, @NonNull final VesselClass vesselClass, final int capacityInM3, final double fillPercent) {
		final Vessel vessel = createVessel(name, vesselClass);
		vessel.setCapacity(capacityInM3);
		vessel.setFillCapacity(fillPercent);
		return vessel;
	}

	@NonNull
	public BaseFuel createBaseFuel(@NonNull final String name, final double equivalenceFactor) {
		final BaseFuel baseFuel = FleetFactory.eINSTANCE.createBaseFuel();
		baseFuel.setName(name);
		baseFuel.setEquivalenceFactor(equivalenceFactor);

		fleetModel.getBaseFuels().add(baseFuel);
		return baseFuel;
	}

	public void addVessel(final Vessel vessel) {
		fleetModel.getVessels().add(vessel);
	}

	public void setRouteParameters(final VesselClass vesselClass, @NonNull final Route route, final int ladenConsumptionRatePerDay, final int ballastConsumptionRatePerDay,
			final int ladenNBORatePerDay, final int ballastNBORatePerDay, final int canalTransitHours) {

		final VesselClassRouteParameters params = FleetFactory.eINSTANCE.createVesselClassRouteParameters();

		params.setRoute(route);
		params.setLadenConsumptionRate(ladenConsumptionRatePerDay);
		params.setBallastConsumptionRate(ballastConsumptionRatePerDay);
		params.setLadenNBORate(ladenNBORatePerDay);
		params.setBallastNBORate(ballastNBORatePerDay);
		params.setExtraTransitTime(canalTransitHours);

		vesselClass.getRouteParameters().add(params);
	}

	public @NonNull VesselClass createVesselClass(@NonNull final String vesselClassName, final int capacityInM3, final double fillCapacity, final double minSpeed, final double maxSpeed,
			@NonNull final BaseFuel baseFuel, final double pilotLightRate, final int minHeelVolume, final int cooldownVolume, final int warmupTime) {

		if (minSpeed > maxSpeed) {
			throw new IllegalArgumentException();
		}

		final VesselClass vesselClass = FleetFactory.eINSTANCE.createVesselClass();

		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vesselClass.setLadenAttributes(laden);
		vesselClass.setBallastAttributes(ballast);

		vesselClass.setName(vesselClassName);
		vesselClass.setBaseFuel(baseFuel);
		vesselClass.setMinSpeed(minSpeed);
		vesselClass.setMaxSpeed(maxSpeed);
		vesselClass.setCapacity(capacityInM3);
		vesselClass.setPilotLightRate(pilotLightRate);
		vesselClass.setWarmingTime(warmupTime);
		vesselClass.setCoolingVolume(cooldownVolume);
		vesselClass.setMinHeel(minHeelVolume);
		vesselClass.setFillCapacity(fillCapacity);

		fleetModel.getVesselClasses().add(vesselClass);

		return vesselClass;

	}

	public void setVesselStateAttributes(final @NonNull VesselClass vesselClass, final boolean isLaden, final int nboRate, final int idleNBORate, final int idleBaseRate,
			final int portConsumptionRate) {
		final VesselStateAttributes attributes = isLaden ? vesselClass.getLadenAttributes() : vesselClass.getBallastAttributes();

		attributes.setNboRate(nboRate);
		attributes.setIdleNBORate(idleNBORate);
		attributes.setIdleBaseRate(idleBaseRate);
		attributes.setInPortBaseRate(portConsumptionRate);
	}

	public void setVesselStateAttributesCurve(@NonNull final VesselClass vesselClass, final boolean isLaden, final double speed, final double consumption) {
		final VesselStateAttributes attributes = isLaden ? vesselClass.getLadenAttributes() : vesselClass.getBallastAttributes();

		for (final FuelConsumption fuelConsumption : attributes.getFuelConsumption()) {
			if (fuelConsumption.getSpeed() == speed) {
				fuelConsumption.setConsumption(consumption);
				return;
			}
		}

		final FuelConsumption fuelConsumption = FleetFactory.eINSTANCE.createFuelConsumption();
		fuelConsumption.setSpeed(speed);
		fuelConsumption.setConsumption(consumption);
		attributes.getFuelConsumption().add(fuelConsumption);

	}

}
