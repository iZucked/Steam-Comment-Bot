package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.Date;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * @since 3.0
 */
public class MinimalScenarioCreator extends DefaultScenarioCreator {
	/**
	 * 
	 */
	public final VesselClass vc;
	public final Vessel vessel;
	public final VesselAvailability vesselAvailability; 
	
	public final Port originPort;
	public final Port loadPort;
	public final Port dischargePort;
	
	public final Cargo cargo;
	
	/**
	 * Initialises a minimal complete scenario, creating:
	 * - contract and shipping legal entities
	 * - one vessel class and one vessel
	 * - one (default) route
	 * - one fixed-price sales contract and one fixed-price purchase contract
	 * - three ports (one origin port, one load port and one discharge port)
	 * - one cargo
	 * The vessel starts at the origin port, must travel to the load port, pick up the cargo,
	 * travel to the discharge port and discharge it. There is enough time at every stage
	 * to create some idling at the discharge port. 
	 */
	public MinimalScenarioCreator() {
		scenario = ManifestJointModel.createEmptyInstance(null);
		
		final CommercialModel commercialModel = scenario.getCommercialModel();
		final FleetModel fleetModel = scenario.getFleetModel();
		
		// need to create a legal entity for contracts
		contractEntity = addEntity("Third-parties");
		// need to create a legal entity for shipping
		shippingEntity = addEntity("Shipping");
		commercialModel.setShippingEntity(shippingEntity);

		// need to create sales and purchase contracts
		salesContract = addSalesContract("Sales Contract", dischargePrice);
		purchaseContract = addPurchaseContract("Purchase Contract");
		
		// create a vessel class with default name
		vc = fleetCreator.createDefaultVesselClass(null);
		// create a vessel in that class
		vessel = fleetCreator.createMultipleDefaultVessels(vc, 1)[0];
		
		// need to create a default route
		addRoute(ScenarioTools.defaultRouteName);		

		final double maxSpeed = vc.getMaxSpeed();
		
		// initialise the port creator with a convenient base distance multiplier
		if (vc.getMinSpeed() == maxSpeed) {
			portCreator.baseDistanceMultiplier = maxSpeed;
		}
		
		// create three ports (and their distances) all with default settings
		final Port [] ports = portCreator.createDefaultPorts(3);
		
		// these are start, load and discharge ports respectively
		originPort = ports[0];
		loadPort = ports[1];
		dischargePort = ports[2];
		
		/*
		 *  determine the time it will take the vessel to travel between load
		 *  and discharge ports, and create a cargo with enough time to spare
		 */
		
		int duration = 2 * getTravelTime(loadPort, dischargePort, null, (int) maxSpeed);
		
		cargo = cargoCreator.createDefaultCargo(null, ports[1], ports[2], null, duration);
		
		Date loadDate = cargo.getSortedSlots().get(0).getWindowStart();
		Date dischargeDate = cargo.getSortedSlots().get(1).getWindowEndWithSlotOrPortTime();
		
		Date startDate = addHours(loadDate, -2 * getTravelTime(originPort, loadPort, null, (int) maxSpeed));
		Date endDate = addHours(dischargeDate, 2 * getTravelTime(dischargePort, originPort, null, (int) maxSpeed));
		
		
		this.vesselAvailability = fleetCreator.setAvailability(scenario.getPortfolioModel().getScenarioFleetModel(), vessel, originPort, startDate, originPort, endDate);			
	}
}