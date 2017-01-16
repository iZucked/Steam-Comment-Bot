/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.time.ZonedDateTime;
import java.util.List;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;

/**
 */
public class StsScenarioCreator extends DefaultScenarioCreator {
	/**
	 * 
	 */
	public final VesselClass vc;
	public final VesselAvailability[] vesselAvailabilities;

	public final Port originPort;
	public final Port loadPort;
	public final Port transferPort;
	public final Port dischargePort;

	public final Cargo loadCargo;

	// public final Cargo dischargeCargo;

	/**
	 * Initialises a minimal complete scenario, creating: - contract and shipping legal entities - one vessel class and one vessel - one (default) route - one fixed-price sales contract and one
	 * fixed-price purchase contract - three ports (one origin port, one load port and one discharge port) - one cargo The vessel starts at the origin port, must travel to the load port, pick up the
	 * cargo, travel to the discharge port and discharge it. There is enough time at every stage to create some idling at the discharge port.
	 */
	public StsScenarioCreator() {
		// scenario = ManifestJointModel.createEmptyInstance(null);
		//
		// // need to create a legal entity for contracts
		// contractEntity = addEntity("Third-parties");
		// // need to create a legal entity for shipping
		// shippingEntity = addEntity("Shipping");

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract", purchasePrice);

		// create a vessel class with default name
		vc = fleetCreator.createDefaultVesselClass(null);
		// create two vessels in that class
		vesselAvailabilities = fleetCreator.createMultipleDefaultVessels(vc, 2, shippingEntity);
		// final VesselAvailability[] vesselAvailabilities = new VesselAvailability[vessels.length];
		// for (int i = 0; i < vessels.length; ++i) {
		// for (VesselAvailability vesselAvailability : scenario.getCargoModel().getVesselAvailabilities()) {
		// if (vesselAvailability.getVessel() == vessels[i]) {
		// vesselAvailabilities[i] = vesselAvailability;
		// }
		// }
		// }

		// need to create a default route
		addRoute(RouteOption.DIRECT);

		final double maxSpeed = vc.getMaxSpeed();

		// initialise the port creator with a convenient base distance multiplier
		if (vc.getMinSpeed() == maxSpeed) {
			portCreator.baseDistanceMultiplier = maxSpeed;
		}

		// create three ports (and their distances) all with default settings
		final Port[] ports = portCreator.createDefaultPorts(4);

		// these are start, load and discharge ports respectively
		originPort = ports[0];
		loadPort = ports[1];
		transferPort = ports[2];
		dischargePort = ports[3];

		/*
		 * determine the time it will take the vessel to travel between load and discharge ports, and create a cargo with enough time to spare
		 */

		final int loadTransferDuration = 2 * getTravelTime(loadPort, transferPort, null, (int) maxSpeed);
		final int transferDischargeDuration = 2 * getTravelTime(transferPort, dischargePort, null, (int) maxSpeed);

		loadCargo = cargoCreator.createDefaultCargo(null, loadPort, transferPort, null, loadTransferDuration);

		DischargeSlot loadTransferSlot = (DischargeSlot) loadCargo.getSortedSlots().get(1);

		// dischargeCargo = cargoCreator.createDefaultCargo(null, transferPort, dischargePort, loadTransferSlot.getWindowStartWithSlotOrPortTime(), transferDischargeDuration);

		// LoadSlot dischargeTransferSlot = (LoadSlot) loadCargo.getSortedSlots().get(0);

		// set fixed discharge quantities on the transfer slot
		loadTransferSlot.setMinQuantity(10000);
		loadTransferSlot.setMaxQuantity(10000);
		// dischargeTransferSlot.setMinQuantity(1000);
		// dischargeTransferSlot.setMaxQuantity(1000);

		// setup transfer slots as sts
		// loadTransferSlot.setTransferTo(dischargeTransferSlot);

		final ZonedDateTime loadDate = loadCargo.getSlots().get(0).getWindowStartWithSlotOrPortTime();
		final ZonedDateTime transferDate = loadCargo.getSlots().get(1).getWindowStartWithSlotOrPortTime();
		// final Date dischargeDate = dischargeCargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();

		// vessel one will start before the load date and end before the discharge date
		final ZonedDateTime preLoadDate = loadDate.minusHours(2 * getTravelTime(originPort, loadPort, null, (int) maxSpeed));
		// final Date preDischargeDate = addHours(dischargeDate, -2);
		// fleetCreator.setAvailability(portfolioModel.getScenarioFleetModel(), vessels[0], originPort, preLoadDate, transferPort, preDischargeDate);

		// vessel two will start after the load date and end after the discharge date
		final ZonedDateTime postLoadDate = loadDate.plusHours(2);
		// final Date postDischargeDate = addHours(dischargeDate, 2 * getTravelTime(dischargePort, originPort, null, (int) maxSpeed));
		// fleetCreator.setAvailability(portfolioModel.getScenarioFleetModel(), vessels[1], transferPort, postLoadDate, originPort, postDischargeDate);

		loadCargo.setVesselAssignmentType(vesselAvailabilities[0]);

		/*
		 * assignment = AssignmentFactory.eINSTANCE.createElementAssignment(); assignment.setAssignedObject(dischargeCargo); assignment.setAssignment(vessels[1]);
		 * assignmentModel.getElementAssignments().add(assignment);
		 */

	}

}