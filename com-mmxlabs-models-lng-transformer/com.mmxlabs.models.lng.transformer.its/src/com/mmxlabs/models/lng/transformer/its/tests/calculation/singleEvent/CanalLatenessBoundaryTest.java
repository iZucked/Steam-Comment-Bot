/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.singleEvent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * Tests for <a href="https://mmxlabs.fogbugz.com/default.asp?184">FogBugz: Case 184</a>
 * 
 * Test that a canal is used if a vessel will be late if it takes the ocean route.
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class CanalLatenessBoundaryTest {

	// private static final RouteOption canalOption = RouteOption.SUEZ;

	/**
	 * If the vessel is going to be late if it takes the longer ocean route check it takes the shorter canal route, even if it costs more.
	 */
	@Test
	public void canalUsedToAvoidLateness() {

		final String testName = "Expensive canal used to avoid lateness";

		final int canalCost = 1000;
		final int travelTime = 100;

		// Canal: 980 miles @ 10mph + 1 hour transit time = 99 hours
		// Ocean 1010 miles @ 10mph = 101 hours
		// The canal will take 98 + 1 = 99 hours, the ocean will take 101 hours.
		final int canalTransitTimeHours = 1;
		final int canalDistance = 980;
		final int oceanRouteDistance = 1010;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testRouteWhenLate(testName, canalCost, canalDistance, oceanRouteDistance, travelTime, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on canal", RouteOption.SUEZ.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels on canal", RouteOption.SUEZ.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * If the vessel is going to be late in either the ocean or canal route, test that the canal route is used if it is slightly shorter but is more costly.
	 */
	@Test
	public void expensiveShorterCanalUsedWhenLate() {

		final String testName = "Expensive shorter canal used when late";

		final int canalCost = 1000;
		final int travelTime = 100;

		// The canal will take 104 + 1 = 105 hours, the ocean route will take 106 hours
		final int canalTransitTimeHours = 1;
		final int canalDistance = 1040;
		final int oceanRouteDistance = 1060;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testRouteWhenLate(testName, canalCost, canalDistance, oceanRouteDistance, travelTime, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on canal", RouteOption.SUEZ.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels on canal", RouteOption.SUEZ.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * If the vessel is going to be late in either the ocean or canal route, test that the canal route is used if it is the same length but is more costly.
	 */
	@Test
	public void expensiveCanalNotUsedWhenLate() {

		final String testName = "Expensive canal not used when late if same length";

		final int canalCost = 1000;
		final int travelTime = 100;

		// The canal will take 104 + 1 = 105 hours, the ocean route will take 105 hours
		final int canalTransitTimeHours = 1;
		final int canalDistance = 1040;
		final int oceanRouteDistance = 1050;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testRouteWhenLate(testName, canalCost, canalDistance, oceanRouteDistance, travelTime, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on ocean", RouteOption.DIRECT.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels on ocean", RouteOption.DIRECT.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * Test that an expensive canal is not used if it is quicker than the ocean route, but the ocean route will get there on time anyway.
	 */
	@Test
	public void expensiveCanalNotUsed() {

		final String testName = "Expensive canal not used";

		final int canalCost = 1000;
		final int travelTime = 100;

		// The canal will take 98 + 1 = 99 hours, the ocean route will take 100 hours
		final int canalTransitTimeHours = 1;
		final int canalDistance = 980;
		final int oceanRouteDistance = 1000;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testRouteWhenLate(testName, canalCost, canalDistance, oceanRouteDistance, travelTime, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels on ocean", RouteOption.DIRECT.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels on ocean", RouteOption.DIRECT.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * Assuming identically costed direct and Suez voyages, pick direct.
	 */
	@Test
	public void preferDirectWhenCostsIdentical() {

		final String testName = "Prefer direct voyages when costs are identical";

		// The canal is free
		final int canalCost = 0;
		final int travelTime = 150;

		// The canal will take 98 + 1 = 99 hours, the ocean route will take 100 hours
		final int canalTransitTimeHours = 1;
		final int canalDistance = 980;
		final int oceanRouteDistance = 1000;

		final SimpleCargoAllocation a = new SimpleCargoAllocation(testRouteWhenLate(testName, canalCost, canalDistance, oceanRouteDistance, travelTime, canalTransitTimeHours));

		Assert.assertTrue("Laden leg travels direct", RouteOption.DIRECT.equals(a.getLadenLeg().getRoute().getRouteOption()));
		Assert.assertTrue("Ballast leg travels direct", RouteOption.DIRECT.equals(a.getBallastLeg().getRoute().getRouteOption()));
	}

	/**
	 * For testing scenarios between two ports with an ocean route and a canal route. Fuel consumption and NBO rates are identical for laden/ballast/idle vessels). The speed of the vessel is always
	 * 10mph.
	 * 
	 * @param testName
	 *            Name of the test for identifying console output
	 * @param canalFee
	 *            The one off fee of the canal
	 * @param canalDistance
	 *            The distance between the two ports via canal
	 * @param oceanRouteDistance
	 *            The distance between the two ports via ocean
	 * @param travelTime
	 *            The time in hours of the voyage
	 * @param canalTransitTimeHours
	 *            Extra time in hours added onto the time of the canal (due to delays, e.g. queues).
	 * @return
	 */
	private CargoAllocation testRouteWhenLate(final String testName, final int canalFee, final int canalDistance, final int oceanRouteDistance, final int travelTime, final int canalTransitTimeHours) {

		// use the same fuel consumption for every travel/idle/canal
		final int fuelConsumptionPerHour = 10;
		final int NBORatePerHour = 10;
		// have same fuel consumption/nbo rate for travel and idle
		final int fuelTravelConsumptionPerHour = fuelConsumptionPerHour;
		final int fuelIdleConsumptionPerHour = fuelConsumptionPerHour;
		final int canalTransitFuelPerHour = fuelConsumptionPerHour;
		final int NBOTravelRatePerHour = NBORatePerHour;
		final int NBOIdleRatePerHour = NBORatePerHour;

		// same fee for laden and ballast
		final int canalLadenCost = canalFee;
		final int canalUnladenCost = canalFee;

		final float baseFuelUnitPrice = 1;
		final float equivalenceFactor = 1;
		final float dischargePrice = 1;
		final float cvValue = 1;

		final int speed = 10;
		final int capacity = 1000000;

		final int fuelTravelConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelTravelConsumptionPerHour);
		final int NBOTravelRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBOTravelRatePerHour);
		final int canalTransitFuelPerDay = TimeUnitConvert.convertPerHourToPerDay(canalTransitFuelPerHour);

		final int fuelIdleConsumptionPerDay = TimeUnitConvert.convertPerHourToPerDay(fuelIdleConsumptionPerHour);
		final int NBOIdleRatePerDay = TimeUnitConvert.convertPerHourToPerDay(NBOIdleRatePerHour);

		final boolean useDryDock = true;
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;

		final LNGScenarioModel canalScenario = ScenarioTools.createScenarioWithCanal(oceanRouteDistance, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, speed, speed,
				capacity, speed, fuelTravelConsumptionPerDay, speed, fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBOIdleRatePerDay, NBOTravelRatePerDay, speed, fuelTravelConsumptionPerDay,
				speed, fuelTravelConsumptionPerDay, fuelIdleConsumptionPerDay, NBOIdleRatePerDay, NBOTravelRatePerDay, useDryDock, pilotLightRate, minHeelVolume);

		CustomScenarioCreator.createCanalAndCost(canalScenario, RouteOption.SUEZ, ScenarioTools.A, ScenarioTools.B, canalDistance, canalDistance, canalLadenCost, canalUnladenCost,
				canalTransitFuelPerDay, NBOTravelRatePerDay, canalTransitTimeHours);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(canalScenario);
		// there will be a single cargo allocation for this cargo
		final CargoAllocation a = result.getCargoAllocations().get(0);
		ScenarioTools.printCargoAllocation(testName, a);

		return a;
	}
}
