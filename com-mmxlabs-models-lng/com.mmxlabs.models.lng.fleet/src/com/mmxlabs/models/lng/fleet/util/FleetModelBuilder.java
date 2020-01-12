/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.RouteOption;

public class FleetModelBuilder {
	private @NonNull final FleetModel fleetModel;

	public FleetModelBuilder(@NonNull final FleetModel fleetModel) {
		this.fleetModel = fleetModel;
	}

	public @NonNull FleetModel getFleetModel() {
		return fleetModel;
	}

	@NonNull
	public VesselGroup makeVesselGroup(@NonNull final String name, @NonNull final Vessel... vessels) {
		final VesselGroup vesselGroup = FleetFactory.eINSTANCE.createVesselGroup();
		vesselGroup.setName(name);
		for (final Vessel vessel : vessels) {
			vesselGroup.getVessels().add(vessel);
		}
		fleetModel.getVesselGroups().add(vesselGroup);
		return vesselGroup;
	}

	@NonNull
	public VesselGroup makeVesselGroup(@NonNull final String name, @NonNull final Collection<Vessel> vessels) {
		final VesselGroup vesselGroup = FleetFactory.eINSTANCE.createVesselGroup();
		vesselGroup.setName(name);
		vesselGroup.getVessels().addAll(vessels);
		fleetModel.getVesselGroups().add(vesselGroup);
		return vesselGroup;
	}

	@NonNull
	public Vessel createVesselFrom(@NonNull final String name, final Vessel source, BiConsumer<Vessel, Vessel> costCloner) {
		final Vessel copy = EcoreUtil.copy(source);
		copy.setName(name);

		if (costCloner != null) {
			costCloner.accept(source, copy);
		}
		fleetModel.getVessels().add(copy);

		return copy;
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

	public void setRouteParameters(final Vessel vessel, @NonNull final RouteOption routeOption, final int ladenConsumptionRatePerDay, final int ballastConsumptionRatePerDay,
			final int ladenNBORatePerDay, final int ballastNBORatePerDay, final int canalTransitHours) {

		VesselClassRouteParameters params = null;
		for (final VesselClassRouteParameters p : vessel.getRouteParameters()) {
			if (Objects.equals(routeOption, p.getRouteOption())) {
				params = p;
				break;
			}
		}
		if (params == null) {
			params = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
			params.setRouteOption(routeOption);
			vessel.getVesselOrDelegateRouteParameters().add(params);
		}
		
		params.setLadenConsumptionRate(ladenConsumptionRatePerDay);
		params.setBallastConsumptionRate(ballastConsumptionRatePerDay);
		params.setLadenNBORate(ladenNBORatePerDay);
		params.setBallastNBORate(ballastNBORatePerDay);
		params.setExtraTransitTime(canalTransitHours);

	}

	public @NonNull Vessel createVessel(@NonNull final String vesselName, final int capacityInM3, final double fillCapacity, final double minSpeed, final double maxSpeed,
			@NonNull final BaseFuel baseFuel, final double pilotLightRate, final int minHeelVolume, final int cooldownVolume, final int warmupTime) {

		if (minSpeed > maxSpeed) {
			throw new IllegalArgumentException();
		}

		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();

		final VesselStateAttributes laden = FleetFactory.eINSTANCE.createVesselStateAttributes();
		final VesselStateAttributes ballast = FleetFactory.eINSTANCE.createVesselStateAttributes();

		vessel.setLadenAttributes(laden);
		vessel.setBallastAttributes(ballast);

		vessel.setName(vesselName);
		vessel.setBaseFuel(baseFuel);
		vessel.setMinSpeed(minSpeed);
		vessel.setMaxSpeed(maxSpeed);
		vessel.setCapacity(capacityInM3);
		vessel.setPilotLightRate(pilotLightRate);
		vessel.setWarmingTime(warmupTime);
		vessel.setCoolingVolume(cooldownVolume);
		vessel.setSafetyHeel(minHeelVolume);
		vessel.setFillCapacity(fillCapacity);

		fleetModel.getVessels().add(vessel);

		return vessel;

	}

	public void setVesselStateAttributes(final @NonNull Vessel vessel, final boolean isLaden, final int nboRate, final int idleNBORate, final int idleBaseRate, final int portConsumptionRate) {
		final VesselStateAttributes attributes = isLaden ? vessel.getLadenAttributes() : vessel.getBallastAttributes();

		attributes.setNboRate(nboRate);
		attributes.setIdleNBORate(idleNBORate);
		attributes.setIdleBaseRate(idleBaseRate);
		attributes.setInPortBaseRate(portConsumptionRate);
	}

	public void setVesselStateAttributesCurve(@NonNull final Vessel vessel, final boolean isLaden, final double speed, final double consumption) {
		final VesselStateAttributes attributes = isLaden ? vessel.getLadenAttributes() : vessel.getBallastAttributes();

		for (final FuelConsumption fuelConsumption : attributes.getFuelConsumption()) {
			if (fuelConsumption.getSpeed() == speed) {
				fuelConsumption.setConsumption(consumption);
				return;
			}
		}

		attributes.setFuelConsumptionOverride(true);

		final FuelConsumption fuelConsumption = FleetFactory.eINSTANCE.createFuelConsumption();
		fuelConsumption.setSpeed(speed);
		fuelConsumption.setConsumption(consumption);
		attributes.getFuelConsumption().add(fuelConsumption);
	}

}
