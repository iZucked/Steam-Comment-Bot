/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.multipleEvents;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.common.TimeUnitConvert;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * <a href="https://mmxlabs.fogbugz.com/default.asp?259">Case 259: Scenario with several cargoes</a>
 * 
 * @author Adam Semenenko
 */
@ExtendWith(ShiroRunner.class)
public class CharterOutTest {

	/**
	 * There are two charter outs in a row. The first has heel available, the second does not. Check that the first uses heel and the second doesn't.
	 * 
	 * There is a charter out that start and ends at Port A. Next a dry dock at Port B (to force travel and to check heel usage). Next a charter out that starts and ends at Port B, then a dry dock at
	 * port C to force a travel and check heel usage.
	 */
	@Test
	public void twoCharterOutHeelUsageTest() {

		// discharge price makes LNG slightly cheaper than BF.
		final float dischargePrice = 0.99f;

		final CustomScenarioCreator csc = new CustomScenarioCreator(dischargePrice);

		// a leg between ports A and B will take 100 hours.
		final int legDurationHours = 100;

		final Port portA = ScenarioTools.createPort("PortA");
		final Port portB = ScenarioTools.createPort("PortB");
		final Port portC = ScenarioTools.createPort("PortC");
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
		csc.addVesselSimple(vesselClassName, numOfVesselsToCreate, baseFuelUnitPrice, speed, capacity, consumption, NBORate, pilotLightRate, minHeelVolume, false);

		final String firstCharterOutID = "first charter out";
		final String secondCharterOutID = "second charter out";
		final int firstCOHeelLimit = 1000;
		final int secondCOHeelLimit = 0;
		final int charterOutDurationDays = 10;
		final int dryDockDurationDays = 0;
		final float cvValue = 1;

		final LocalDateTime startFirstCharterOut = LocalDateTime.now();
		final LocalDateTime endFirstCharterOut = startFirstCharterOut.plusDays(charterOutDurationDays);
		final LocalDateTime startFirstDryDock = endFirstCharterOut.plusHours(legDurationHours);
		final LocalDateTime startSecondCharterOut = startFirstDryDock;
		final LocalDateTime endSecondCharterOut = startSecondCharterOut.plusDays(charterOutDurationDays);
		final LocalDateTime startSecondDryDock = endSecondCharterOut.plusHours(legDurationHours);

		csc.addCharterOut(firstCharterOutID, portA, portA, startFirstCharterOut, firstCOHeelLimit, charterOutDurationDays, cvValue, dischargePrice, 0, 0);
		csc.addDryDock(portB, startFirstDryDock, dryDockDurationDays);
		csc.addCharterOut(secondCharterOutID, portB, portB, startSecondCharterOut, secondCOHeelLimit, charterOutDurationDays, cvValue, dischargePrice, 0, 0);
		csc.addDryDock(portC, startSecondDryDock, dryDockDurationDays);

		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		ScenarioTools.printSequences(result);

		for (final Sequence seq : result.getSequences()) {
			for (final Event e : seq.getEvents()) {
				if (e instanceof Journey) {
					final Journey j = (Journey) e;

					if (j.getDestination().equals(portB)) {
						// first journey, so check that heel is used.
						// get the amount of heel used
						long LNGUsed = 0;
						for (final FuelQuantity fq : j.getFuels()) {
							if ((fq.getFuel() == Fuel.FBO) || (fq.getFuel() == Fuel.NBO)) {
								for (FuelAmount fa : fq.getAmounts()) {
									if (fa.getUnit() == FuelUnit.M3) {
										LNGUsed += fa.getQuantity();
									}
								}
							}
						}

						Assertions.assertTrue(LNGUsed <= firstCOHeelLimit, "Heel not exceeded");

						FuelUsageAssertions.assertBaseFuelNotUsed(j);
					} else if (j.getDestination().equals(portC)) {
						// second journey, check heel is NOT used.
						// get the amount of heel used
						long LNGUsed = 0;
						for (final FuelQuantity fq : j.getFuels()) {
							if ((fq.getFuel() == Fuel.FBO) || (fq.getFuel() == Fuel.NBO)) {
								for (FuelAmount fa : fq.getAmounts()) {
									if (fa.getUnit() == FuelUnit.M3) {
										LNGUsed += fa.getQuantity();
									}
								}
							}
						}

						Assertions.assertTrue(LNGUsed == secondCOHeelLimit, "Heel not exceeded");

						FuelUsageAssertions.assertLNGNotUsed(j);
					} else {
						Assertions.fail("Journey not to a recognised port?");
					}
				}
			}
		}
	}
}
