/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.multipleEvents;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
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
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?259">Case 259: Scenario with several cargoes</a>
 * 
 * @author Adam Semenenko
 * 
 */
@ExtendWith(ShiroRunner.class)
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

		final PortModelBuilder portModelBuilder = csc.getScenarioModelBuilder().getPortModelBuilder();

		final Port portA = portModelBuilder.createPort("portA", "portA", "UTC", 0, 0);
		final Port portB = portModelBuilder.createPort("portB", "portB", "UTC", 0, 0);

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

		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		Assertions.assertNotNull(result);

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
			Assertions.assertNotNull(cargoAllocation);
			final SimpleCargoAllocation ca = new SimpleCargoAllocation(cargoAllocation);

			// expect only NBO to be used always
			// check the laden journey
			for (final FuelQuantity fq : ca.getLadenLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden leg never uses FBO");

						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden leg never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedLegNBO, amount.getQuantity(), "Laden leg uses 90M3 NBO\"");
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getLadenIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden idle never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden idle never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedIdleNBO, amount.getQuantity(), "Laden idle uses 10M3 NBO");
						}
					}
				}
			}

			// check the ballast journey
			for (final FuelQuantity fq : ca.getBallastLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Ballast leg never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Ballast leg never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedLegNBO, amount.getQuantity(), "Ballast leg uses 90M3 NBO");
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getBallastIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Ballast idle never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Ballast idle never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedIdleNBO, amount.getQuantity(), "Ballast idle uses 10M3 NBO");
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

		final PortModelBuilder portModelBuilder = csc.getScenarioModelBuilder().getPortModelBuilder();

		final Port portA = portModelBuilder.createPort("portA", "portA", "UTC", 0, 0);
		final Port portB = portModelBuilder.createPort("portB", "portB", "UTC", 0, 0);

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

		IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();
		CustomScenarioCreator.createCanalAndCost(scenarioDataProvider, canalName, portA, portB, canalDistanceBetweenPorts, canalDistanceBetweenPorts, canalDistanceBetweenPorts,
				canalDistanceBetweenPorts, 0, 0, 0);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

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
			Assertions.assertEquals(canalName, ca.getLadenLeg().getRouteOption(), "Vessel travels on canal");
			Assertions.assertEquals(canalName, ca.getBallastLeg().getRouteOption(), "Vessel travels on canal");

			// expect only NBO to be used always

			// check the laden journey
			for (final FuelQuantity fq : ca.getLadenLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden leg never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden leg never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedLegNBO, amount.getQuantity(), "Laden leg uses 90M3 NBO");
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getLadenIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden idle never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertEquals(0, amount.getQuantity(), "Laden idle never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedIdleNBO, amount.getQuantity(), "Laden idle uses 10M3 NBO");
						}
					}
				}
			}

			// check the ballast journey
			for (final FuelQuantity fq : ca.getBallastLeg().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(0, amount.getQuantity(), "Ballast leg never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {

							Assertions.assertEquals(0, amount.getQuantity(), "Ballast leg never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertEquals(expectedLegNBO, amount.getQuantity(), "Ballast leg uses 90M3 NBO");
						}
					}
				}
			}
			for (final FuelQuantity fq : ca.getBallastIdle().getFuels()) {
				if (fq.getFuel() == Fuel.FBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertTrue(amount.getQuantity() == 0, "Ballast idle never uses FBO");
						}
					}
				}
				if (fq.getFuel() == Fuel.BASE_FUEL) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.MT) {
							Assertions.assertTrue(amount.getQuantity() == 0, "Ballast idle never uses base fuel");
						}
					}
				}
				if (fq.getFuel() == Fuel.NBO) {
					for (final FuelAmount amount : fq.getAmounts()) {
						if (amount.getUnit() == FuelUnit.M3) {
							Assertions.assertTrue(amount.getQuantity() == expectedIdleNBO, "Ballast idle uses 10M3 NBO");
						}
					}
				}
			}
		}
	}
}
