/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.fleet.Vessel;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.fleetallocation.AllocatedVessel;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.scheduler.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?255">Case 255: Check constraints have not be violated</a><br>
 * 
 * This class tests the allowed vessel list on cargoes.<br>
 * 
 * Every Cargo has an 'allowedVessels' list; check that no cargo's slots are put on a vessel which is not in the allowedVessels list, unless it's empty.<br>
 * 
 * @author Adam
 * 
 */
public class CargoAllowedVesselConstraintCheckTest {

	private static final int dischargePrice = 1;
	private final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

	/**
	 * One cargo only has one vessels on it's allowedVessels list. Check that the constraint holds.
	 */
	@Test
	public void test() {

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 10;
		final int numOfClassTwo = 11;
		final int numOfClassThree = 12;
		final int numOfClassFour = 1;

		// createVessels creates and adds the vessesl to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false);
		csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false);
		final ArrayList<Vessel> vesselsClassFour = new ArrayList<Vessel>(Arrays.asList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true)));

		// create some cargos.
		final ArrayList<Cargo> cargoes = new ArrayList<Cargo>(Arrays.asList(SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue)));

		// add a constraint to one cargo - it can only be carried by the one vessel that is class four.
		final Cargo constrainedCargo = cargoes.get(0);
		Assert.assertTrue("Allowed vessels added to cargo successfully", csc.addAllowedVesselsOnCargo(constrainedCargo, vesselsClassFour));

		// build and run the scenario.
		final Scenario scenario = csc.buildScenario();
		final Schedule result = ScenarioTools.evaluate(scenario);

		// get the cargo that was constraned out of the results.
		for (final CargoAllocation ca : result.getCargoAllocations()) {

			final Cargo c = (Cargo) (ca.getLoadSlot().eContainer());

			if (c.equals(constrainedCargo)) {
				// found the constrained cargo
				// now get the name of the vessel and see it it matches the one class four vessel.
				final AllocatedVessel av = ca.getVessel();

				final boolean namesMatch = av.getName().equals(vesselsClassFour.get(0).getName());
				Assert.assertTrue("Only vessel class four used", namesMatch);
				// don't need to carry on
				break;
			}
		}
	}

	/**
	 * Test one class of vessel not being allowed to carry any cargo
	 * 
	 * (this was the test which found the bug highlighted in case 280 - now fixed)
	 */
	@Test
	public void test2() {

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 5;
		final int numOfClassTwo = 6;
		final int numOfClassThree = 7;
		final int numOfClassFour = 8;

		// this vessels that will be allowed to carry the cargo
		final ArrayList<Vessel> allowedVessels = new ArrayList<Vessel>();

		// createVessels creates and adds the vessesl to the scenario.
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 1, 35, 10000000, 5, 5, 5, 0, false)));
		// class four vessels will not be allowed to carry any cargoes.
		csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true);

		// create some cargos.
		final ArrayList<Cargo> cargoes = new ArrayList<Cargo>(Arrays.asList(SanityCheckTools.addCargos(csc, ports, loadPrice, dischargePrice, cvValue)));

		// constrain all cargoes so none of class four can carry any.
		for (final Cargo c : cargoes) {
			Assert.assertTrue("Allowed vessels added to cargo successfully", csc.addAllowedVesselsOnCargo(c, allowedVessels));
		}

		// build and run the scenario
		final Scenario scenario = csc.buildScenario();
		final Schedule result = ScenarioTools.evaluate(scenario);

		// check that the vessel that carries every cargo matches the name of one in the allowed vessels list.
		for (final CargoAllocation ca : result.getCargoAllocations()) {

			final AllocatedVessel av = ca.getVessel();

			boolean inAllowedVessels = false;
			for (final Vessel v : allowedVessels) {

				if (v.getName().equals(av.getName())) {
					inAllowedVessels = true;
					break;
				}
			}
			Assert.assertTrue("Only vessel class four used", inAllowedVessels);
		}
	}

}
