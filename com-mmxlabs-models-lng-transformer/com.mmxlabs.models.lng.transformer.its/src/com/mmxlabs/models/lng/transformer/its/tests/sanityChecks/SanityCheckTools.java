/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;

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
	 * Add a number of cargoes to the scenario in a random-ish manner. For every port a cargo is added to go to every other port.
	 * 
	 * @param ports
	 *            The ports to add cargoes to.
	 * @param loadPrice
	 * @param dischargePrice
	 * @param cvValue
	 */
	public static Cargo[] addCargoes(final CustomScenarioCreator csc, final Port[] ports, final int loadPrice, final float dischargePrice, final float cvValue) {

		// For each port there is a cargo from every other port (i.e. no cargo goes from one port to the same port).
		// This gives the number of cargoes below.
		final int numOfCargoes = ports.length * (ports.length - 1);// - ports.length;
		final Cargo[] inputCargoes = new Cargo[numOfCargoes];
		int i = 0;

		LocalDateTime cargoStart = LocalDateTime.now();
		int duration = 50;

		for (final Port portX : ports) {
			for (final Port portY : ports) {
				if (!portX.equals(portY)) {

					final Cargo c = csc.addCargo(portX.getName() + " to " + portY.getName() + " in " + duration + ".", portX, portY, loadPrice, dischargePrice, cvValue, cargoStart, duration);

					inputCargoes[i++] = c;

					duration += 25;
					cargoStart = cargoStart.plusDays(10);

				} else {

					duration -= duration / 2;
				}
			}
		}

		return inputCargoes;
	}

	/**
	 * Get a list of ports that has visited in a schedule.
	 * 
	 * @param sequence
	 *            The sequence
	 * @return A list of visited ports (with no duplicates)
	 */
	public static List<Port> getVesselsVisitedPorts(final Sequence sequence) {

		final List<Port> visitedPorts = new ArrayList<Port>();

		for (final Event se : sequence.getEvents()) {
			if (se instanceof SlotVisit) {
				final SlotVisit visit = (SlotVisit) se;

				if (!visitedPorts.contains(visit.getPort())) {
					visitedPorts.add(visit.getPort());
				}
			} else if (se instanceof VesselEventVisit) {
				final VesselEventVisit visit = (VesselEventVisit) se;

				if (!visitedPorts.contains(visit.getPort())) {
					visitedPorts.add(visit.getPort());
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

		LocalDateTime start = LocalDateTime.now();
		for (final Port portA : ports) {
			for (final Port portB : ports) {
				if (!portA.equals(portB)) {

					final DryDockEvent dry = csc.addDryDock(portB, start, 1);

					if (allowedDrydockVessel != null) {
						dry.getAllowedVessels().add(allowedDrydockVessel);
					}
					if (allowedDrydockVesselClass != null) {
						dry.getAllowedVessels().add(allowedDrydockVesselClass);
					}

					start = start.plusHours(2);
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

		final LocalDateTime start = LocalDateTime.now();
		int charterOutDurationDays = 1;
		for (final Port portA : ports) {
			for (final Port portB : ports) {

				final String id = "CharterOut " + portA.getName() + " to " + portB.getName();

				final CharterOutEvent charterOut = csc.addCharterOut(id, portA, portB, start, 1000, charterOutDurationDays, cvValue, dischargePrice, 100, 0);

				if (allowedCharterOutVessel != null) {
					charterOut.getAllowedVessels().add(allowedCharterOutVessel);
				}
				if (allowedCharterOutVesselClass != null) {
					charterOut.getAllowedVessels().add(allowedCharterOutVesselClass);
				}

				if (portA.equals(portB)) {
					charterOutDurationDays /= 2;
				}

			}
		}
	}
}
