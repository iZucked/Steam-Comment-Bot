/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class PeriodVerifierUtil {

	public static void runTest(final LNGScenarioRunner scenarioRunner, final boolean checkPanamaBookings) {

		final IScenarioDataProvider fullModel = scenarioRunner.getScenarioToOptimiserBridge().getScenarioDataProvider();
		final IScenarioDataProvider periodModel = scenarioRunner.getScenarioToOptimiserBridge().getOptimiserScenario();

		final CargoModel periodCargoModel = ScenarioModelUtil.getCargoModel(periodModel);

		final Schedule fullSchedule = ScenarioModelUtil.getScheduleModel(fullModel).getSchedule();
		final Schedule periodSchedule = scenarioRunner.getScenarioToOptimiserBridge().createOptimiserInitialSchedule();

		Assertions.assertNotNull(fullSchedule);
		Assertions.assertNotNull(periodSchedule);

		for (final Cargo cargo : periodCargoModel.getCargoes()) {
			final CargoAllocation fullCargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), fullSchedule);
			final CargoAllocation periodCargoAllocation = ScheduleTools.findCargoAllocation(cargo.getLoadName(), periodSchedule);

			Assertions.assertNotNull(fullCargoAllocation);
			Assertions.assertNotNull(periodCargoAllocation);

			// Check same number of slots
			final int numberOfSlots = fullCargoAllocation.getSlotAllocations().size();
			Assertions.assertEquals(numberOfSlots, periodCargoAllocation.getSlotAllocations().size());

			// Check slot dates are the same
			for (int idx = 0; idx < numberOfSlots; ++idx) {
				Assertions.assertEquals(fullCargoAllocation.getSlotAllocations().get(idx).getSlotVisit().getStart(), periodCargoAllocation.getSlotAllocations().get(idx).getSlotVisit().getStart());
			}
			// Check return dates are the same
			final int numberOfEvents = fullCargoAllocation.getEvents().size();
			Assertions.assertEquals(numberOfEvents, periodCargoAllocation.getEvents().size());
			Assertions.assertEquals(fullCargoAllocation.getEvents().get(numberOfEvents - 1).getEnd(), periodCargoAllocation.getEvents().get(numberOfEvents - 1).getEnd());

			// Check same route, fuel choice
			for (int idx = 0; idx < numberOfEvents; ++idx) {
				final Event fullEvent = fullCargoAllocation.getEvents().get(idx);
				if (fullEvent instanceof Journey) {
					final Journey fullJourney = (Journey) fullEvent;
					final Journey periodJourney = (Journey) periodCargoAllocation.getEvents().get(idx);
					Assertions.assertEquals(fullJourney.getRouteOption(), periodJourney.getRouteOption());

					// TODO: Fuel choice (in master branch)

					// Assert panama booking (if requested) is the same
					if (checkPanamaBookings) {
						// Assertions.assertEquals(fullJourney.getCanalBooking(), periodJourney.getCanalBooking());
						Assertions.assertEquals(fullJourney.getCanalBookingPeriod(), periodJourney.getCanalBookingPeriod());
						Assertions.assertEquals(fullJourney.getCanalDateTime(), periodJourney.getCanalDateTime());
						Assertions.assertEquals(fullJourney.getCanalArrivalTime(), periodJourney.getCanalArrivalTime());
						Assertions.assertEquals(fullJourney.getLatestPossibleCanalDateTime(), periodJourney.getLatestPossibleCanalDateTime());
						// Use Enum in master branch
						// Assertions.assertEquals(fullJourney.getCanalEntry(), periodJourney.getCanalEntry().getName());
					}
				}
			}

			// Assert P&L is "similar"
			Assertions.assertEquals(fullCargoAllocation.getGroupProfitAndLoss().getProfitAndLoss(), periodCargoAllocation.getGroupProfitAndLoss().getProfitAndLoss(), 10_000.0);

		}
		// Again for vessel events,

	}
}
