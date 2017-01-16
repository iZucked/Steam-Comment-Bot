/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.multipleEvents;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?259">Case 259: Scenario with several cargoes</a>
 * 
 * @author Adam Semenenko
 * 
 */
@RunWith(value = ShiroRunner.class)
public class MultipleCargoFuelConsumptionTest {

	private static final int dischargePrice = 1;
	private static final int loadPrice = 1;

	/**
	 * One vessel must take two cargoes from port A to port B. NBO should be cheaper always.
	 */
	@Test
	public void twoCargoTest() {

		final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		// We want a leg between ports A and B to take 10 hours (9 hours travel, 1 hour idle).
		final int legDuration = 10;

		final Port portA = PortFactory.eINSTANCE.createPort();
		final Port portB = PortFactory.eINSTANCE.createPort();
		final int distanceBetweenPorts = 90;
		csc.addPorts(portA, portB, distanceBetweenPorts);

		final String vesselClassName = "vc";
		final int numOfVesselsToCreate = 1;
		final float baseFuelUnitPrice = 10.01f;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORate = TimeUnitConvert.convertPerHourToPerDay(10);
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume, false);

		// Set up start dates and durations.
		final LocalDateTime cargoAStart = LocalDateTime.now();
		final int cargoADuration = 10;
		// cargo B starts one hour after the vessel makes it back to port A (the vessel takes 10 hours to get to portB, then 10 hours to travel back to portA).
		final LocalDateTime cargoBStart = cargoAStart.plusHours(2 * legDuration);
		final int cargoBDuration = 10;

		final int dryDockDurationDays = 0;

		csc.addCargo("alpha", portA, portB, loadPrice, dischargePrice, 10, cargoAStart, cargoADuration);
		csc.addCargo("beta", portA, portB, loadPrice, dischargePrice, 10, cargoBStart, cargoBDuration);

		// add a dry dock so ballast idle doesn't take ages, but leave enough time for a 1 hour idle.
		final LocalDateTime dryDockStart = cargoBStart.plusHours(2 * legDuration);
		csc.addDryDock(portA, dryDockStart, dryDockDurationDays);

		final LNGScenarioModel scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (final CargoAllocation ca : result.getCargoAllocations()) {
			ScenarioTools.printCargoAllocation(ca.getName(), ca);
		}

		// expected fuel consumptions
		// NBO rate is 10, so expect 90M3 of NBO for the 9 hour legs and 10M3 for the 1 hour idle.s
		final int expectedLegNBO = 90;
		final int expectedIdleNBO = 10;

		// add assertions on results
		for (final CargoAllocation cargoAllocation : result.getCargoAllocations()) {
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);
			// expect only NBO to be used always

			// check the laden journey
			for (final FuelQuantity fq : ca.getLadenLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden leg never uses FBO", 0, amount.getQuantity());

						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Laden leg never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden leg uses 90M3 NBO", expectedLegNBO, amount.getQuantity());
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getLadenIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden idle never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Laden idle never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden idle uses 10M3 NBO", expectedIdleNBO, amount.getQuantity());
						}
					}
				}
			}

			// check the ballast journey
			for (final FuelQuantity fq : ca.getBallastLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast leg never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Ballast leg never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast leg uses 90M3 NBO", expectedLegNBO, amount.getQuantity());
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getBallastIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast idle never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Ballast idle never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast idle uses 10M3 NBO", expectedIdleNBO, amount.getQuantity());
						}
					}
				}
			}
		}
	}

	@Test
	public void twoCargoTestWithCanal() {

		final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		// We want a leg between ports A and B to take 10 hours (9 hours travel, 1 hour idle).
		final int legDuration = 10;

		final Port portA = PortFactory.eINSTANCE.createPort();
		final Port portB = PortFactory.eINSTANCE.createPort();

		// add the ports
		final int distanceBetweenPorts = 110;
		csc.addPorts(portA, portB, distanceBetweenPorts);
		// and add a canal
		final RouteOption canalName = RouteOption.SUEZ;
		final int canalDistanceBetweenPorts = 90;

		final String vesselClassName = "vc";
		final int numOfVesselsToCreate = 1;
		final float baseFuelUnitPrice = 10.01f;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORate = TimeUnitConvert.convertPerHourToPerDay(10);
		final int pilotLightRate = 0;
		final int minHeelVolume = 0;
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume, false);

		// Set up start dates and durations.
		final LocalDateTime cargoAStart = LocalDateTime.now();
		final int cargoADuration = 10;
		// cargo B starts one hour after the vessel makes it back to port A (the vessel takes 10 hours to get to portB, then 10 hours to travel back to portA).
		final LocalDateTime cargoBStart = cargoAStart.plusHours(2 * legDuration);
		final int cargoBDuration = 10;

		final int dryDockDurationDays = 0;

		csc.addCargo("alpha", portA, portB, loadPrice, dischargePrice, 10, cargoAStart, cargoADuration);
		csc.addCargo("beta", portA, portB, loadPrice, dischargePrice, 10, cargoBStart, cargoBDuration);

		// add a dry dock so ballast idle doesn't take ages, but leave enough time for a 1 hour idle.
		final LocalDateTime dryDockStart = cargoBStart.plusHours(2 * legDuration);
		csc.addDryDock(portA, dryDockStart, dryDockDurationDays);

		final LNGScenarioModel scenario = csc.buildScenario();
		CustomScenarioCreator.createCanalAndCost(scenario, canalName, portA, portB, canalDistanceBetweenPorts, canalDistanceBetweenPorts, canalDistanceBetweenPorts, canalDistanceBetweenPorts, 0, 0,
				0);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (final CargoAllocation ca : result.getCargoAllocations()) {
			ScenarioTools.printCargoAllocation(ca.getName(), ca);
		}

		// expected fuel consumptions
		// NBO rate is 10, so expect 90M3 of NBO for the 9 hour legs and 10M3 for the 1 hour idle.s
		final int expectedLegNBO = 90;
		final int expectedIdleNBO = 10;

		// add assertions on results
		for (final CargoAllocation cargoAllocation : result.getCargoAllocations()) {
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);
			Assert.assertEquals("Vessel travels on canal", canalName, ca.getLadenLeg().getRoute().getRouteOption());
			Assert.assertEquals("Vessel travels on canal", canalName, ca.getBallastLeg().getRoute().getRouteOption());

			// expect only NBO to be used always

			// check the laden journey
			for (final FuelQuantity fq : ca.getLadenLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden leg never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Laden leg never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden leg uses 90M3 NBO", expectedLegNBO, amount.getQuantity());
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getLadenIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden idle never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertEquals("Laden idle never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Laden idle uses 10M3 NBO", expectedIdleNBO, amount.getQuantity());
						}
					}
				}
			}

			// check the ballast journey
			for (final FuelQuantity fq : ca.getBallastLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast leg never uses FBO", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {

							Assert.assertEquals("Ballast leg never uses base fuel", 0, amount.getQuantity());
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertEquals("Ballast leg uses 90M3 NBO", expectedLegNBO, amount.getQuantity());
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getBallastIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertTrue("Ballast idle never uses FBO", amount.getQuantity() == 0);
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assert.assertTrue("Ballast idle never uses base fuel", amount.getQuantity() == 0);
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assert.assertTrue("Ballast idle uses 10M3 NBO", amount.getQuantity() == expectedIdleNBO);
						}
					}
				}
			}
		}
	}
}
