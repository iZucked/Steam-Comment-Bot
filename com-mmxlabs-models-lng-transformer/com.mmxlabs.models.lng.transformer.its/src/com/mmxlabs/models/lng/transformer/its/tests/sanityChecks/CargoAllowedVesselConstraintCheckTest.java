/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

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
@ExtendWith(ShiroRunner.class)
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

		// createVessels creates and adds the vessels to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false);
		csc.addVesselSimple("classThree", numOfClassThree, 27, 25, 10000, 17, 14, 10, 1000, false);
		final List<VesselAvailability> vesselsClassFour = Lists.newArrayList(csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true));
		// create some cargoes.
		final List<Cargo> cargoes = new ArrayList<Cargo>(Arrays.asList(SanityCheckTools.addCargoes(csc, ports, loadPrice, dischargePrice, cvValue)));

		// add a constraint to one cargo - it can only be carried by the one vessel that is class four.
		final Cargo constrainedCargo = cargoes.get(0);
		csc.addAllowedVesselsOnCargo(constrainedCargo, vesselsClassFour.stream().map(VesselAvailability::getVessel).collect(Collectors.toList()));

		// build and run the scenario.
		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		// get the cargo that was constrained out of the results.
		for (final CargoAllocation ca : result.getCargoAllocations()) {

			if (ScheduleModelUtils.matchingSlots(constrainedCargo, ca)) {
				// found the constrained cargo
				// now get the name of the vessel and see it it matches the one class four vessel.
				final Vessel av = ca.getSequence().getVesselAvailability().getVessel();

				final boolean namesMatch = av.getName().equals(vesselsClassFour.get(0).getVessel().getName());

				if (!namesMatch) {
					int ii = 0;
				}
				Assertions.assertTrue(namesMatch, "Only vessel class four used");
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
		final List<VesselAvailability> allowedVessels = new ArrayList<>();

		// createVessels creates and adds the vessels to the scenario.
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classOne", numOfClassOne, 10, 25, 1000000, 10, 10, 0, 500, false)));
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classTwo", numOfClassTwo, 9, 30, 700000, 11, 9, 7, 0, false)));
		allowedVessels.addAll(Arrays.asList(csc.addVesselSimple("classThree", numOfClassThree, 1, 35, 10000000, 5, 5, 5, 0, false)));
		// class four vessels will not be allowed to carry any cargoes.
		csc.addVesselSimple("classFour", numOfClassFour, 15, 20, 150000, 20, 10, 5, 2000, true);

		// create some cargoes.
		final List<Cargo> cargoes = Lists.newArrayList(SanityCheckTools.addCargoes(csc, ports, loadPrice, dischargePrice, cvValue));

		// constrain all cargoes so none of class four can carry any.
		for (final Cargo c : cargoes) {
			csc.addAllowedVesselsOnCargo(c, allowedVessels.stream().map(VesselAvailability::getVessel).collect(Collectors.toList()));
		}

		// build and run the scenario
		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		// check that the vessel that carries every cargo matches the name of one in the allowed vessels list.
		for (final CargoAllocation ca : result.getCargoAllocations()) {

			final Vessel av = ca.getSequence().getVesselAvailability().getVessel();

			boolean inAllowedVessels = false;
			for (final VesselAvailability v : allowedVessels) {

				if (v.getVessel().getName().equals(av.getName())) {
					inAllowedVessels = true;
					break;
				}
			}
			Assertions.assertTrue(inAllowedVessels, "Only vessel class four used");
		}
	}

}
