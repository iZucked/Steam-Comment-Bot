/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation.multipleEvents;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.FuelUsageAssertions;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Class for tests to do with multiple dry docks.
 * 
 * @author Adam
 * 
 */
@ExtendWith(ShiroRunner.class)
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

		final int legDurationHours = 10;

		final Port portA = ScenarioTools.createPort("PortA");
		final Port portB = ScenarioTools.createPort("PortB");
		final Port portC = ScenarioTools.createPort("PortC");
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

		final LocalDateTime startFirstDryDock = LocalDateTime.now();
		final LocalDateTime endFirstDryDock = startFirstDryDock.plusDays(dryDockDurationDays);
		final LocalDateTime startSecondDryDock = endFirstDryDock.plusHours(legDurationHours);
		final LocalDateTime endSecondDryDock = startSecondDryDock.plusDays(dryDockDurationDays);
		final LocalDateTime startThirdDryDock = endSecondDryDock.plusHours(legDurationHours);
		// final Date endThirdDryDock = new Date(startThirdDryDock.getTime() + TimeUnit.DAYS.toMillis(dryDockDurationDays));

		csc.addDryDock(portA, startFirstDryDock, dryDockDurationDays);
		csc.addDryDock(portB, startSecondDryDock, dryDockDurationDays);
		csc.addDryDock(portC, startThirdDryDock, dryDockDurationDays);

		final IScenarioDataProvider scenarioDataProvider = csc.getScenarioDataProvider();

		// evaluate and get a schedule
		final Schedule result = ScenarioTools.evaluate(scenarioDataProvider);

		ScenarioTools.printSequences(result);

		for (final Sequence seq : result.getSequences()) {
			for (final Event e : seq.getEvents()) {
				if (e instanceof Journey) {
					final Journey j = (Journey) e;

					FuelUsageAssertions.assertLNGNotUsed(j);

					for (final FuelQuantity fq : j.getFuels()) {
						if (fq.getFuel() == Fuel.BASE_FUEL) {
							for (final FuelAmount amount : fq.getAmounts()) {
								if (amount.getUnit() == FuelUnit.MT) {
									Assertions.assertEquals(100, amount.getQuantity(), "100MT of basefuel used");
								}
							}
						}
					}
				} else if (e instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) e;

					Assertions.assertEquals(TimeUnit.DAYS.toHours(dryDockDurationDays), vev.getDuration(), "Duration of dry dock matches expected");
				}
			}
		}
	}
}
