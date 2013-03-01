package com.mmxlabs.models.lng.transformer.its.tests.evaluation;
import org.junit.Test;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.DefaultScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.its.tests.sanityChecks.SanityCheckTools;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;


public class ShippingCostCalculationTest {
	/*
	 * We need to create a barebones scenario with a single vessel schedule.
	 * Then the scenario needs to be evaluated to test correct calculation of:
	 *  - Fuel costs
	 *  - Port costs
	 *  - Route costs
	 *  - NBO rates
	 */

	@Test
	public void testStartEndCase() {
		CustomScenarioCreator csc = new CustomScenarioCreator(1.0f);

		final int loadPrice = 1;
		final int cvValue = 10;

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB") };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the scenario
		final int numOfClassOne = 2;

		csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500, false);


		// create some cargoes.
		SanityCheckTools.addCargoes(csc, ports, loadPrice, 1.0f, cvValue);

		DefaultScenarioCreator dsc = new DefaultScenarioCreator();
		//MMXRootObject scenario = csc.buildScenario();
		MMXRootObject scenario = dsc.buildScenario();

		// evaluate and get a schedule
		final Schedule schedule = ScenarioTools.evaluate(scenario);
		
		/*
		MMXRootObject scenario = dsc.buildScenario();
		Schedule schedule = ScenarioTools.evaluate(scenario);
		*/
		System.out.println(schedule);
	}

}
