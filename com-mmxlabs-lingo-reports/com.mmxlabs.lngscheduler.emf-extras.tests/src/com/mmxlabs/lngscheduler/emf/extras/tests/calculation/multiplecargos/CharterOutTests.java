/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.calculation.multiplecargos;

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

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.demo.app.wizards.CustomScenarioCreator;
import com.mmxlabs.lngscheduler.emf.extras.tests.calculation.ScenarioTools;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?259">Case 259: Scenario with several cargoes</a>
 * 
 * @author Adam Semenenko
 */
public class CharterOutTests {

	// discharge price makes LNG slightly cheaper than BF.
	private static final float dischargePrice = 0.99f;
	private static final int loadPrice = 1;

	/**
	 * There are two charter outs in a row. The first has heel available, the second does not. Check that the first uses heel and the second doesn't.
	 * 
	 * There is a charter out that start and ends at Port A. Next a dry dock at Port B (to force travel and to check heel usage). Next a charter out that starts and ends at Port B, then a dry dock at
	 * port C to force a travel and check heel usage.
	 */
	@Test
	public void twoCharterOutHeelUseageTest() {

		CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		// We want a leg between ports A and B to take 10 hours (9 hours travel, 1 hour idle).
		final long legDurationHours = 100;

		final Port portA = PortFactory.eINSTANCE.createPort();
		final Port portB = PortFactory.eINSTANCE.createPort();
		final Port portC = PortFactory.eINSTANCE.createPort();
		portA.setName("PortA");
		portB.setName("PortB");
		portC.setName("PortC");
		final int distanceBetweenPorts = 1000;
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
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume);

		final String firstCharterOutID = "first charter out";
		final String secondCharterOutID = "second charter out";
		final int firstCOHeelLimit = 1000;
		final int secondCOHeelLimit = 0;
		final int charterOutDurationDays = 10;
		final float cvValue = 1;

		final Date startFirstCharterOut = new Date();
		final Date endFirstCharterOut = new Date(startFirstCharterOut.getTime() + TimeUnit.DAYS.toMillis(charterOutDurationDays));
		final Date startFirstDryDock = new Date(endFirstCharterOut.getTime() + TimeUnit.HOURS.toMillis(legDurationHours));
		final Date startSecondCharterOut = new Date(startFirstDryDock.getTime());
		final Date endSecondCharterOut = new Date(startSecondCharterOut.getTime() + TimeUnit.DAYS.toMillis(charterOutDurationDays));
		final Date startSecondDryDock = new Date(endSecondCharterOut.getTime() + TimeUnit.HOURS.toMillis(legDurationHours));

		csc.addCharterOut(firstCharterOutID, portA, portA, startFirstCharterOut, firstCOHeelLimit, charterOutDurationDays, cvValue, dischargePrice, 0, 0);
		csc.addDryDock(portB, startFirstDryDock);
		csc.addCharterOut(secondCharterOutID, portB, portB, startSecondCharterOut, secondCOHeelLimit, charterOutDurationDays, cvValue, dischargePrice, 0, 0);
		csc.addDryDock(portC, startSecondDryDock);

		final Scenario scenario = csc.buildScenario();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenario);

		ScenarioTools.printSequences(result);

		for (final Sequence seq : result.getSequences()) {
			for (final ScheduledEvent e : seq.getEvents()) {
				if (e instanceof Journey) {
					Journey j = (Journey) e;
					
					if (j.getToPort().equals(portB)) {
						// first journey, so check that heel is used.
						// get the amount of heel used
						long LNGUsed = 0;
						for (FuelQuantity fq : j.getFuelUsage()) {
							if (fq.getFuelType() == FuelType.FBO || fq.getFuelType() == FuelType.NBO)
								LNGUsed += fq.getQuantity();
						}

						Assert.assertTrue("Heel not exceeded", LNGUsed <= firstCOHeelLimit);
						
						assertBaseFuelNotUsed(j);
					}
					else if (j.getToPort().equals(portC)) {
						// second journey, check heel is NOT used.
						// get the amount of heel used
						long LNGUsed = 0;
						for (FuelQuantity fq : j.getFuelUsage()) {
							if (fq.getFuelType() == FuelType.FBO || fq.getFuelType() == FuelType.NBO)
								LNGUsed += fq.getQuantity();
						}

						Assert.assertTrue("Heel not exceeded", LNGUsed == secondCOHeelLimit);
						
						assertLNGNotUsed(j);
					}
					else
						Assert.fail("Journey not to a recognised port?");
				}
			}
		}
	}

	/**
	 * Assert that during a given journey base fuel is not used, but NBO and FBO are
	 * 
	 * @param j
	 */
	private void assertBaseFuelNotUsed(Journey j) {

		for (FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel not used", fq.getQuantity() == 0);
			else if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("NBO used", fq.getQuantity() > 0);
			else if (fq.getFuelType() == FuelType.FBO)
				Assert.assertTrue("FBO used", fq.getQuantity() > 0);
		}
	}
	/**
	 * Assert that during a given journey NBO and FBO are not used and base fuel is.
	 * 
	 * @param j
	 */
	private void assertLNGNotUsed(Journey j) {

		for (FuelQuantity fq : j.getFuelUsage()) {
			if (fq.getFuelType() == FuelType.NBO)
				Assert.assertTrue("NBO not used", fq.getQuantity() == 0);
			else if (fq.getFuelType() == FuelType.FBO)
				Assert.assertTrue("FBO not used", fq.getQuantity() == 0);
			else if (fq.getFuelType() == FuelType.BASE_FUEL)
				Assert.assertTrue("Base fuel used", fq.getQuantity() > 0);
		}
	}
}
