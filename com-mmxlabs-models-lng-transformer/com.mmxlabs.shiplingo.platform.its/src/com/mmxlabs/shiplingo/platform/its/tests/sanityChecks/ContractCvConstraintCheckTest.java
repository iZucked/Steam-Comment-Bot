/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.sanityChecks;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.its.tests.CustomScenarioCreator;
import com.mmxlabs.shiplingo.platform.its.tests.ScenarioRunner;
import com.mmxlabs.shiplingo.platform.its.tests.calculation.ScenarioTools;

public class ContractCvConstraintCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);
	
	protected Port smallLoadPort;
	protected Port bigLoadPort;
	protected Port smallDischargePort;
	protected Port bigDischargePort;
	
	protected Cargo smallToLargeCargo;
	protected Cargo largeToSmallCargo;

	/**
	 * Creates a scenario object which has a strongly suboptimal wiring in PnL terms, ready to be rewired by the
	 * optimiser if constraints permit.
	 * 
	 * The scenario involves two load slots with large and small maximum cargo quantities respectively, and two discharge slots
	 * with matching large and small maximum cargo quantities. The optimum wiring (constraints permitting) is to wire the
	 * large load slot (at "portA") to the large discharge slot (at "portD") and the small load slot (at "portB") to the small 
	 * discharge slot (at "portC"), to allow the maximum amount of cargo to be shipped in total. The returned scenario actually 
	 * wires "portA"->"portB" and "portC"->"portD".
	 * 
	 * @author Simon McGregor
	 * @return The scenario object. 
	 */
	public MMXRootObject createSuboptimalScenario() {

		final int loadPrice = 1;
		final int dischargePrice = 90;
		final int fuelPrice = 1;
		final int smallQty = 50000;
		final int bigQty = 500000;

		smallLoadPort = ScenarioTools.createPort("smallLoadPort");
		bigLoadPort = ScenarioTools.createPort("bigLoadPort");
		smallDischargePort = ScenarioTools.createPort("smallDischargePort");
		bigDischargePort = ScenarioTools.createPort("bigDischargePort");
		
		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { smallLoadPort, bigLoadPort, smallDischargePort, bigDischargePort };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 2;

		// createVessels creates and adds the vessels to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, fuelPrice, 25, 1000000, 10, 10, 0, 500, false);

		final Date cargoStart = new Date(System.currentTimeMillis());
		
		// create some cargoes with different CV values
		smallToLargeCargo = csc.addCargo("CV-22-cargo", smallLoadPort, bigDischargePort, loadPrice, dischargePrice, 22, cargoStart, 50);
		largeToSmallCargo = csc.addCargo("CV-23-Cargo", bigLoadPort, smallDischargePort, loadPrice, dischargePrice, 22, cargoStart, 50);
		
		// make sure they can be rewired
		smallToLargeCargo.setAllowRewiring(true);
		largeToSmallCargo.setAllowRewiring(true);

		// set up one cargo with a small load allowance and a large discharge allowance 
		smallToLargeCargo.getLoadSlot().setMaxQuantity(smallQty);
		smallToLargeCargo.getDischargeSlot().setMaxQuantity(bigQty);
		
		// set up one cargo with a large load allowance and a small discharge allowance 
		largeToSmallCargo.getLoadSlot().setMaxQuantity(bigQty);
		largeToSmallCargo.getDischargeSlot().setMaxQuantity(smallQty);
		
		// build and run the scenario.
		final MMXRootObject scenario = csc.buildScenario();
		
		return scenario;
	}

	protected void testExpectedWiring(Schedule schedule, Port [] loadPorts, Port [] dischargePorts) {
		Assert.assertEquals("Load port and discharge port lists should have same length", loadPorts.length, dischargePorts.length);
		
		final int n = loadPorts.length;
		
		// set up an array storing whether load ports are assigned at all
		final boolean [] found = new boolean [n];
		for (int i = 0; i < n ; i++) {
			found[i] = false;
		}
		
		// check that the cargo allocations wire load ports up with the expected discharge ports
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			for (int i = 0; i < n; i++) {
				APort loadPort = ca.getLoadAllocation().getPort(); 
				APort dischargePort = ca.getDischargeAllocation().getPort(); 
				if (loadPort.equals(loadPorts[i])) {
					found[i] = true;
					Assert.assertEquals(String.format("Expected solution wires %s to %s", loadPort.getName(), dischargePorts[i].getName()), dischargePorts[i], dischargePort);
				}
			}
		}
		
		// check that all expected load ports were assigned
		for (int i = 0; i < n; i++) {
			Assert.assertTrue(String.format("Load port %s not assigned in solution", loadPorts[i]), found[i]);
		}
		
	}
	
	/*
	 * Tests the case where no CV constraints are attached to the scenario.
	 * The PnL-optimal wiring should be generated.
	 */
			
	@Test
	public void testNoConstraints() throws IncompleteScenarioException {
		final MMXRootObject scenario = createSuboptimalScenario();
		
		ScenarioRunner runner = new ScenarioRunner(scenario);
		runner.init();
		runner.run();
		
		final Schedule endSchedule = runner.getFinalSchedule();
		
		Port [] expectedLoadPorts = { smallLoadPort, bigLoadPort };
		Port [] expectedDischargePorts = { smallDischargePort, bigDischargePort };
		testExpectedWiring(endSchedule, expectedLoadPorts, expectedDischargePorts);
		
	}

	/*
	 * 
	 * Tests the case where a maximum CV constraint is attached to one of the sales contracts in the scenario.
	 * The PnL-suboptimal wiring should be generated.
	 */
	@Test
	public void testMaxCvConstraint() throws IncompleteScenarioException {
		final MMXRootObject scenario = createSuboptimalScenario();

		SalesContract contract = (SalesContract) smallToLargeCargo.getDischargeSlot().getContract();
		contract.setMaxCvValue(22.5);
		
		ScenarioRunner runner = new ScenarioRunner(scenario);
		runner.init();
		runner.run();
		
		final Schedule endSchedule = runner.getFinalSchedule();
		
		Port [] expectedLoadPorts = { smallLoadPort, bigLoadPort };
		Port [] expectedDischargePorts = { smallDischargePort, bigDischargePort };
		testExpectedWiring(endSchedule, expectedLoadPorts, expectedDischargePorts);
	}

}
