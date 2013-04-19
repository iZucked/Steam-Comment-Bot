package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.Date;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * @since 3.0
 */
public class LddScenarioCreator extends DefaultScenarioCreator {
	/**
	 * 
	 */
	public final VesselClass vc;
	public final Vessel vessel;
	
	public final Port originPort;
	public final Port loadPort;
	public final Port dischargePort1;
	public final Port dischargePort2;
	
	public final Cargo cargo;
	private VesselAvailability vesselAvailability;
	
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
	public LddScenarioCreator() {
		scenario = ManifestJointModel.createEmptyInstance(null);
		
		final CommercialModel commercialModel = scenario.getCommercialModel();
		final FleetModel fleetModel = scenario.getFleetModel();
		final LNGPortfolioModel portfolioModel = scenario.getPortfolioModel();
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
		final Port [] ports = portCreator.createDefaultPorts(4);
		
		// these are start, load and discharge ports respectively
		originPort = ports[0];
		loadPort = ports[1];
		dischargePort1 = ports[2];
		dischargePort2 = ports[3];
		
		/*
		 *  determine the time it will take the vessel to travel between load
		 *  and discharge ports, and create a cargo with enough time to spare
		 */
		
		int duration1 = 2 * getTravelTime(loadPort, dischargePort1, null, (int) maxSpeed);
		int duration2 = 2 * getTravelTime(loadPort, dischargePort2, null, (int) maxSpeed);
		
		cargo = cargoCreator.createDefaultLddCargo(null, ports[1], ports[2], ports[3], null, duration1, duration2);
		
		// set fixed discharge quantities on the discharge slots
		for (int i = 1; i < cargo.getSlots().size(); i++) {
			Slot slot = cargo.getSortedSlots().get(i);
			slot.setMinQuantity(1000);
			slot.setMaxQuantity(1000);			
		}
		
		Date loadDate = cargo.getSlots().get(0).getWindowStart();
		Date lastDischargeDate = cargo.getSlots().get(1).getWindowEndWithSlotOrPortTime();
		
		Date startDate = addHours(loadDate, -2 * getTravelTime(originPort, loadPort, null, (int) maxSpeed));
		Date endDate = addHours(lastDischargeDate, 2 * getTravelTime(dischargePort2, originPort, null, (int) maxSpeed));
		
		this.vesselAvailability = fleetCreator.setAvailability(portfolioModel.getScenarioFleetModel(), vessel, originPort, startDate, originPort, endDate);			

		// complete hack: forcibly assign the cargo to the vessel
		final ElementAssignment assignment = AssignmentFactory.eINSTANCE.createElementAssignment();
		assignment.setAssignedObject(cargo);
		assignment.setAssignment(vessel);
		final AssignmentModel assignmentModel = portfolioModel.getAssignmentModel();
		assignmentModel.getElementAssignments().add(assignment);	
	
	}
	


}