/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.sanityChecks;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import scenario.cargo.Cargo;
import scenario.port.Port;

import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;

/**
 * Tools that are used in many of the sanity checks.
 * 
 * @author Adam
 * 
 */
public class SanityCheckTools {

	/**
	 * Set the distance between the given ports in a random-ish manner.
	 * 
	 * @param ports
	 */
	public static void setPortDistances(final CustomScenarioCreator csc, final Port[] ports) {

		int distance = 10;

		for (final Port portX : ports) {

			for (final Port portY : ports) {

				if (!portX.equals(portY)) {

					csc.addPorts(portX, portY, distance);

					distance += 10;

				} else
					distance -= distance / 2;
			}
		}
	}

	/**
	 * Add a number of cargos to the scenario in a random-ish manner. For every port a cargo is added to go to every other port.
	 * 
	 * @param ports
	 *            The ports to add cargos to.
	 * @param loadPrice
	 * @param dischargePrice
	 * @param cvValue
	 */
	public static Cargo[] addCargos(final CustomScenarioCreator csc, final Port[] ports, final int loadPrice, final float dischargePrice, final float cvValue) {

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
}
