/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.calculation.multipleEvents;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.VesselEventVisit;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.shiplingo.platform.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.shiplingo.platform.its.tests.calculation.ScenarioTools;
import com.mmxlabs.shiplingo.platform.models.manifest.wizards.CustomScenarioCreator;

/**
 * Class for tests to do with multiple dry docks.
 * 
 * @author Adam
 * 
 */
public class DryDockTest {

	/**
	 * Test lots of dry docks.
	 * 
	 * Dry dock at A, dry dock at B, dry dock at C. Check fuel use of journeys.
	 */
	@Test
	public void threeDryDocksTest() {

		final float dischargePrice = 1f;
		final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		final long legDurationHours = 10;

		final Port portA = PortFactory.eINSTANCE.createPort();
		final Port portB = PortFactory.eINSTANCE.createPort();
		final Port portC = PortFactory.eINSTANCE.createPort();
		portA.setName("PortA");
		portB.setName("PortB");
		portC.setName("PortC");
		final int distanceBetweenPorts = 100;
		csc.addPorts(portA, portB, distanceBetweenPorts);
		csc.addPorts(portB, portC, distanceBetweenPorts);

		final String vesselClassName = "vc";
		final int numOfVesselsToCreate = 1;
		final float baseFuelUnitPrice = 1f;
		final int speed = 10;
		final int capacity = 1000000;
		final int consumption = TimeUnitConvert.convertPerHourToPerDay(10);
		final int NBORate = TimeUnitConvert.convertPerHourToPerDay(5);
		final int pilotLightRate = 0;
		final int minHeelVolume = 1000;
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume, false);

		final int dryDockDurationDays = 10;
		// final float cvValue = 1;

		final Date startFirstDryDock = new Date();
		final Date endFirstDryDock = new Date(startFirstDryDock.getTime() + TimeUnit.DAYS.toMillis(dryDockDurationDays));
		final Date startSecondDryDock = new Date(endFirstDryDock.getTime() + TimeUnit.HOURS.toMillis(legDurationHours));
		final Date endSecondDryDock = new Date(startSecondDryDock.getTime() + TimeUnit.DAYS.toMillis(dryDockDurationDays));
		final Date startThirdDryDock = new Date(endSecondDryDock.getTime() + TimeUnit.HOURS.toMillis(legDurationHours));
		// final Date endThirdDryDock = new Date(startThirdDryDock.getTime() + TimeUnit.DAYS.toMillis(dryDockDurationDays));

		csc.addDryDock(portA, startFirstDryDock, dryDockDurationDays);
		csc.addDryDock(portB, startSecondDryDock, dryDockDurationDays);
		csc.addDryDock(portC, startThirdDryDock, dryDockDurationDays);

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		ScenarioTools.printSequences(result);

		for (final Sequence seq : result.getSequences()) {
			for (final ScheduledEvent e : seq.getEvents()) {
				if (e instanceof Journey) {
					final Journey j = (Journey) e;

					FuelUsageAssertions.assertLNGNotUsed(j);

					for (final FuelQuantity fq : j.getFuelUsage()) {
						if (fq.getFuelType() == FuelType.BASE_FUEL) {
							Assert.assertEquals("100MT of basefuel used", 100, fq.getQuantity());
						}
					}
				} else if (e instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) e;

					Assert.assertEquals("Duration of dry dock matches expected", TimeUnit.DAYS.toHours(dryDockDurationDays), vev.getEventDuration());
				}
			}
		}
	}
}
