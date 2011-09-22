/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation;

import org.junit.Test;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;

/**
 * @author Adam Semenenko
 * 
 */
public class HeelOutOfCharterOutTests {

	@Test
	public void test() {

		final int speed = 10;
		final int fuelConsumption = ScenarioTools.convertPerHourToPerDay(10);
		final int NBORate = ScenarioTools.convertPerHourToPerDay(5);

		final int distanceBetweenPorts = 1000;
		final float baseFuelUnitPrice = 1f;
		// set discharge price and cv value high to make NBO expensive to try and force BF use.
		final float dischargePrice = 100;
		final float cvValue = 100;
		
		final int travelTime = 100;
		final float equivalenceFactor = 1f;

		final int minSpeed = speed;
		final int maxSpeed = speed;
		final int capacity = 100000;
		final int ballastMinSpeed = speed;
		final int ballastMinConsumption = speed;
		final int ballastMaxSpeed = speed;
		final int ballastMaxConsumption = fuelConsumption;
		final int ballastIdleConsumptionRate = fuelConsumption;
		final int ballastIdleNBORate = NBORate;
		final int ballastNBORate = NBORate;
		final int ladenMinSpeed = speed;
		final int ladenMinConsumption = fuelConsumption;
		final int ladenMaxSpeed = speed;
		final int ladenMaxConsumption = fuelConsumption;
		final int ladenIdleConsumptionRate = fuelConsumption;
		final int ladenIdleNBORate = NBORate;
		final int ladenNBORate = NBORate;
		final int pilotLightRate = 0;

		final int charterOutTimeDays = 10;
		final int heelLimit = 500;

		Scenario scenario = ScenarioTools.createCharterOutScenario(distanceBetweenPorts, baseFuelUnitPrice, dischargePrice, cvValue, travelTime, equivalenceFactor, minSpeed, maxSpeed, capacity,
				ballastMinSpeed, ballastMinConsumption, ballastMaxSpeed, ballastMaxConsumption, ballastIdleConsumptionRate, ballastIdleNBORate, ballastNBORate, ladenMinSpeed, ladenMinConsumption,
				ladenMaxSpeed, ladenMaxConsumption, ladenIdleConsumptionRate, ladenIdleNBORate, ladenNBORate, pilotLightRate, charterOutTimeDays, heelLimit);

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		// print the schedule
		for (Sequence seq : result.getSequences()) {
			printSequence(seq);
		}
	}

	private static void printSequence(final Sequence seq) {

		for (final ScheduledEvent e : seq.getEvents()) {

			if (e instanceof Idle) {
				
				final Idle i = (Idle) e;
				System.err.println("Idle:");
				System.err.println("\tvessel state: " + i.getVesselState() + ", Duration: " + i.getEventDuration() + ", port: " + i.getPort().getName());
				ScenarioTools.printFuel(i.getFuelUsage());
				
			} else if (e instanceof CharterOutVisit) {
				
				final CharterOutVisit cov = (CharterOutVisit) e;
				System.err.println("Charter Out:");
				System.err.println("\tDuration: " + cov.getEventDuration());
				
			} else if (e instanceof VesselEventVisit) {
				
				final VesselEventVisit vev = (VesselEventVisit) e;
				System.err.println("VesselEventVisit:");
				System.err.println("\tDuration: " + vev.getEventDuration());
				
			} else if (e instanceof Journey) {
				
				final Journey j = (Journey) e;
				System.err.println("Journey:");
				System.err.println("\tDuration: " + j.getEventDuration() + ", distance: " + j.getDistance() + ", destination: " + j.getToPort().getName());

				ScenarioTools.printFuel(j.getFuelUsage());

			} else if (e instanceof SlotVisit) {
				SlotVisit sv = (SlotVisit) e;
				System.err.println("SlotVisit:");
				System.err.println("\tDuration: " + sv.getEventDuration());
			}  else if (e instanceof PortVisit) {
				PortVisit pv = (PortVisit) e;
				System.err.println("PortVisit:");
				System.err.println("\tDuration: " + pv.getEventDuration());
			} else {
				System.err.println("Unknown:");
				System.err.println("\t" + e.getClass());
			}
		}
	}
}
