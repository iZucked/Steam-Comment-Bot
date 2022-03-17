/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;

/**
 */
public class LddScenarioCreator extends DefaultScenarioCreator {
	/**
	 * 
	 */
	public final Vessel vessel;

	public final Port originPort;
	public final Port loadPort;
	public final Port dischargePort1;
	public final Port dischargePort2;

	public final Cargo cargo;
	private final VesselAvailability vesselAvailability;

	/**
	 * Initialises a minimal complete scenario, creating: - contract and shipping legal entities - one vessel class and one vessel - one (default) route - one fixed-price sales contract and one
	 * fixed-price purchase contract - three ports (one origin port, one load port and one discharge port) - one cargo The vessel starts at the origin port, must travel to the load port, pick up the
	 * cargo, travel to the discharge port and discharge it. There is enough time at every stage to create some idling at the discharge port.
	 */
	public LddScenarioCreator() {
		super();
		// scenario = ManifestJointModel.createEmptyInstance(null);
		// commercialModelBuilder = new CommercialModelBuilder(ScenarioModelUtil.getCommercialModel(scenario));

		// need to create a legal entity for contracts
		// contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		// shippingEntity = addEntity("Shipping");

		final CommercialModel commercialModel = scenario.getReferenceModel().getCommercialModel();
		final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract", purchasePrice);

		// create a vessel class with default name
		vessel = fleetCreator.createDefaultVessel(null);
		// create a vessel in that class
		fleetCreator.createMultipleDefaultVessels(vessel, 1, shippingEntity);

		// need to create a default route
		addRoute(RouteOption.DIRECT);

		final double maxSpeed = vessel.getVesselOrDelegateMaxSpeed();

		// initialise the port creator with a convenient base distance multiplier
		if (vessel.getVesselOrDelegateMinSpeed() == maxSpeed) {
			portCreator.baseDistanceMultiplier = maxSpeed;
		}

		// create three ports (and their distances) all with default settings
		final Port[] ports = portCreator.createDefaultPorts(4);

		// these are start, load and discharge ports respectively
		originPort = ports[0];
		loadPort = ports[1];
		dischargePort1 = ports[2];
		dischargePort2 = ports[3];

		/*
		 * determine the time it will take the vessel to travel between load and discharge ports, and create a cargo with enough time to spare
		 */

		final int duration1 = 2 * getTravelTime(loadPort, dischargePort1, RouteOption.DIRECT, (int) maxSpeed);
		final int duration2 = 2 * getTravelTime(loadPort, dischargePort2, RouteOption.DIRECT, (int) maxSpeed);

		cargo = cargoCreator.createDefaultLddCargo(null, ports[1], ports[2], ports[3], null, duration1, duration2);

		// set fixed discharge quantities on the discharge slots
		for (int i = 1; i < cargo.getSlots().size(); i++) {
			final Slot slot = cargo.getSortedSlots().get(i);
			slot.setMinQuantity(1000);
			slot.setMaxQuantity(1000);
		}

		final ZonedDateTime loadDate = cargo.getSlots().get(0).getSchedulingTimeWindow().getStart();
		final ZonedDateTime lastDischargeDate = cargo.getSlots().get(1).getSchedulingTimeWindow().getEnd();

		final ZonedDateTime startDate = loadDate.minusHours(2 * getTravelTime(originPort, loadPort, RouteOption.DIRECT, (int) maxSpeed));
		final ZonedDateTime endDate = lastDischargeDate.plusHours(2 * getTravelTime(dischargePort2, originPort, RouteOption.DIRECT, (int) maxSpeed));

		this.vesselAvailability = fleetCreator.setAvailability(scenario.getCargoModel(), vessel, originPort, startDate.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime(), originPort,
				endDate.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

		cargo.setVesselAssignmentType(vesselAvailability);
	}

}