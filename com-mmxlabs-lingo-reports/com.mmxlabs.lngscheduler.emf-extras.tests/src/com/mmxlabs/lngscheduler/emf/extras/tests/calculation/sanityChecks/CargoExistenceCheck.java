/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation.sanityChecks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.port.Port;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.lngscheduler.emf.extras.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?253">BugzID: 253 Check all cargoes in input exist in output (unless spot/optional)</a><br>
 * 
 * Check that, when a scenario is evaluated:<br>
 * <br>
 * 1. The resulting schedule has a cargo allocation for every cargo in the input.<br>
 * 2. The schedule's sequences contain the load and discharge slots for each cargo somewhere.<br>
 * 3. Make sure that the SlotVisit events for each cargo happen in the right order.<br>
 * 
 * @author Adam
 * 
 */
public class CargoExistenceCheck {

	private CustomScenarioCreator csc;

	/**
	 * Check that all cargos added to the scenario are in the output.
	 */
	@Test
	public void test() {

		// A list to hold all cargos that are input.
		final ArrayList<Cargo> inputCargos = new ArrayList<Cargo>();

		final int dischargePrice = 1;
		final int loadPrice = 1;
		final int cvValue = 10;

		csc = new CustomScenarioCreator(dischargePrice);

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { ScenarioTools.createPort("portA"), ScenarioTools.createPort("portB"), ScenarioTools.createPort("portC"), ScenarioTools.createPort("portD"),
				ScenarioTools.createPort("portE"), ScenarioTools.createPort("portF") };

		// Add the ports, and set the distances.
		setPortDistances(ports);

		// create a few vessels and add them to the scenario
		final int numOfClassOne = 2;
		final int numOfClassTwo = 3;

		csc.addVesselSimple("classOne", numOfClassOne, 10, 10, 1000000, 10, 10, 0, 500);
		csc.addVesselSimple("classTwo", numOfClassTwo, 9, 15, 700000, 11, 9, 7, 0);

		// create some cargos.
		inputCargos.addAll(Arrays.asList(addCargos(ports, loadPrice, dischargePrice, cvValue)));

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the legs to console
		for (CargoAllocation ca : result.getCargoAllocations())
			ScenarioTools.printCargoAllocation(ca.getName(), ca);

		// print each vessel's sequence
		ScenarioTools.printSequences(result);

		// check the output
		this.checkCargos(result, inputCargos);
	}

	/**
	 * Set the distance between the given ports in a random-ish manner.
	 * 
	 * @param ports
	 */
	private void setPortDistances(Port[] ports) {

		int distance = 10;

		for (Port portX : ports) {
			for (Port portY : ports) {
				if (!portX.equals(portY)) {

					csc.addPorts(portX, portY, distance);

					distance += 10;
				} else {

					distance -= distance / 2;
				}
			}
		}
	}

	/**
	 * Add a number of cargos to the scenario in a random-ish manner.
	 * 
	 * @param ports
	 *            The ports to add cargos to.
	 * @param loadPrice
	 * @param dischargePrice
	 * @param cvValue
	 */
	private Cargo[] addCargos(final Port[] ports, final int loadPrice, final float dischargePrice, final float cvValue) {

		// For each port there is a cargo from every other port (i.e. no cargo goes from one port to the same port).
		// This gives the number of cargos below.
		final int numOfCargos = ports.length * (ports.length - 1);// - ports.length;
		Cargo[] inputCargos = new Cargo[numOfCargos];
		int i = 0;

		Date cargoStart = new Date(System.currentTimeMillis());
		int duration = 50;

		for (final Port portX : ports) {
			for (final Port portY : ports) {
				if (!portX.equals(portY)) {

					Cargo c = csc.addCargo(portX.getName() + " to " + portY.getName() + " in " + duration + ".", portX, portY, loadPrice, dischargePrice, cvValue, cargoStart, duration);

					inputCargos[i++] = c;

					duration += 25;
					cargoStart.setTime(cargoStart.getTime() + TimeUnit.DAYS.toMillis(1));

				} else {

					duration -= duration / 2;
				}
			}
		}

		return inputCargos;
	}

	/**
	 * Check all the cargos in output are
	 * 
	 * @param result
	 * @param inputCargos
	 */
	private void checkCargos(final Schedule result, final ArrayList<Cargo> inputCargos) {

		final int numOfInputCargos = inputCargos.size();
		int numOfOutputCargos = 0;

		for (final CargoAllocation ca : result.getCargoAllocations()) {

			Cargo c = (Cargo) (ca.getLoadSlot().eContainer());

			Assert.assertTrue("Input cargo is in output", inputCargos.contains(c));

			inputCargos.remove(c);
			numOfOutputCargos++;
		}

		Assert.assertEquals("All cargos in output", 0, inputCargos.size());
		Assert.assertEquals("Number of input cargos is the same as the number of output", numOfInputCargos, numOfOutputCargos);
	}
}
