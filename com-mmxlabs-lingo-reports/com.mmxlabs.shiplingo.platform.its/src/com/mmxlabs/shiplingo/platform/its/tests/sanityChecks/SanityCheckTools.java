/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.sanityChecks;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import scenario.cargo.Cargo;
import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.port.Port;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.shiplingo.platform.its.tests.CustomScenarioCreator;

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

				} else {
					distance -= distance / 2;
				}
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
		final Cargo[] inputCargos = new Cargo[numOfCargos];
		int i = 0;

		final Date cargoStart = new Date(System.currentTimeMillis());
		int duration = 50;

		for (final Port portX : ports) {
			for (final Port portY : ports) {
				if (!portX.equals(portY)) {

					final Cargo c = csc.addCargo(portX.getName() + " to " + portY.getName() + " in " + duration + ".", portX, portY, loadPrice, dischargePrice, cvValue, cargoStart, duration);

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
	 * Get a list of ports that a fleet vessel has visited in a schedule.
	 * 
	 * @param schedule
	 *            The schedule that must contain the given FleetVessel
	 * @param fleetVessel
	 * @return A list of visited ports (with no duplicates)
	 */
	public static ArrayList<Port> getVesselsVisitedPorts(final Schedule schedule, final FleetVessel fleetVessel) {

		final ArrayList<Port> visitedPorts = new ArrayList<Port>();

		for (final Sequence sq : schedule.getSequences()) {
			if (sq.getVessel().equals(fleetVessel)) {
				for (final ScheduledEvent se : sq.getEvents()) {
					if (se instanceof PortVisit) {
						final PortVisit pv = (PortVisit) se;

						if (!visitedPorts.contains(pv.getPort())) {
							visitedPorts.add(pv.getPort());
						}
					}
				}
			}
		}

		return visitedPorts;
	}

	/**
	 * Add a load of drydocks from every port to every port. Also add constraints to each dry dock.
	 * 
	 * @param ports
	 *            The ports to add dry docks to/from
	 * @param allowedDrydockVessel
	 *            A vessel to add to all dry dock's allowed-vessel list. If null it will not be added to any dry dock.
	 * @param allowedDrydockVesselClass
	 *            A vessel class to add to all dry dock's allowed-vesselclass list. If null it will not be added to any dry dock.
	 */
	public static void addDrydocks(final CustomScenarioCreator csc, final Port[] ports, final Vessel allowedDrydockVessel, final VesselClass allowedDrydockVesselClass) {

		final Date start = new Date(System.currentTimeMillis());
		for (final Port portA : ports) {
			for (final Port portB : ports) {
				if (!portA.equals(portB)) {

					final Drydock dry = csc.addDryDock(portB, start, 1);

					if (allowedDrydockVessel != null) {
						dry.getVessels().add(allowedDrydockVessel);
					}
					if (allowedDrydockVesselClass != null) {
						dry.getVesselClasses().add(allowedDrydockVesselClass);
					}

					start.setTime(start.getTime() + TimeUnit.HOURS.toMillis(2));
				}
			}
		}
	}

	/**
	 * Adds a charter out from every port to every port. Also adds constraints to each charter out.
	 * 
	 * @param ports
	 *            The ports to add charter outs to/from
	 * @param allowedCharterOutVessel
	 *            A vessel to add to all charter outs allowed-vessel list. If null it will not be added to any charter out.
	 * @param allowedCharterOutVesselClass
	 *            A class of vessel to add to every charter out's allowed vessel class list. If null it will not be added to any charter out.
	 */
	public static void addCharterOuts(final CustomScenarioCreator csc, final Port[] ports, final Vessel allowedCharterOutVessel, final VesselClass allowedCharterOutVesselClass, final float cvValue,
			final float dischargePrice) {

		final Date start = new Date(System.currentTimeMillis());
		int charterOutDurationDays = 1;
		for (final Port portA : ports) {
			for (final Port portB : ports) {

				final String id = "CharterOut " + portA.getName() + " to " + portB.getName();

				final CharterOut charterOut = csc.addCharterOut(id, portA, portB, start, 1000, charterOutDurationDays, cvValue, dischargePrice, 100, 0);

				if (allowedCharterOutVessel != null) {
					charterOut.getVessels().add(allowedCharterOutVessel);
				}
				if (allowedCharterOutVesselClass != null) {
					charterOut.getVesselClasses().add(allowedCharterOutVesselClass);
				}

				if (portA.equals(portB)) {
					charterOutDurationDays /= 2;
				}

			}
		}
	}
}
