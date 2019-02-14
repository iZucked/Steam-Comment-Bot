/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@RunWith(value = ShiroRunner.class)
public class EndEventTests extends AbstractShippingCalculationsTestClass {
	@Test
	public void testUseDefaultFinalIdlingWhenEndTimeUnspecified() {
		System.err.println("\n\nUnspecified end time should result in idling due to a defined minimum time between last event and end.");
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final IScenarioDataProvider scenario = msc.getScenarioDataProvider();

		msc.vesselAvailability.unsetEndAfter();
		msc.vesselAvailability.unsetEndBy();

		SequenceTester checker = getDefaultTester();

		final int lastIdleHours = /* SchedulerBuilder.minDaysFromLastEventToEnd */60 * 24 - checker.getExpectedValues(Expectations.DURATIONS, Journey.class)[2];
		;
		checker.setExpectedValue(lastIdleHours, Expectations.DURATIONS, EndEvent.class, 0);
		checker.setExpectedValue(0, Expectations.BF_USAGE, EndEvent.class, 0);
		checker.setupOrdinaryFuelCosts();

		final Schedule schedule = ScenarioTools.evaluate(scenario);
		ScenarioTools.printSequences(schedule);

		final Sequence sequence = schedule.getSequences().get(0);

		checker.check(sequence);
	}
}
