/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.generatedcharterout;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LNGScenarioRunnerCreator;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterOutTests extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void UAT_CharterOut_1() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-bonny-point fortin-min days 60.lingo");
		LNGScenarioRunnerCreator.withLiNGOFileLegacyEvaluationRunner(url, true, runner -> {
			Assertions.assertNotNull(runner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
			Assertions.assertEquals(charterOuts.size(), 0);
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void UAT_CharterOut_2() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-bonny-point fortin.lingo");

		LNGScenarioRunnerCreator.withLiNGOFileLegacyEvaluationRunner(url, true, runner -> {
			Assertions.assertNotNull(runner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
			Assertions.assertEquals(charterOuts.size(), 1);

			GeneratedCharterOut gco = charterOuts.get(0);
			Assertions.assertEquals("Bonny Is", gco.getPort().getName());
			Assertions.assertEquals(1319, gco.getDuration());
			Assertions.assertEquals(5221041, gco.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
			Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // no ballast after
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void UAT_CharterOut_3() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-point fortin.lingo");

		LNGScenarioRunnerCreator.withLiNGOFileLegacyEvaluationRunner(url, true, runner -> {
			Assertions.assertNotNull(runner);

			// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
			final Schedule schedule = runner.getSchedule();
			Assertions.assertNotNull(schedule);

			List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
			Assertions.assertEquals(charterOuts.size(), 1);

			GeneratedCharterOut gco = charterOuts.get(0);
			Assertions.assertEquals("Barcelona", gco.getPort().getName());
			Assertions.assertEquals(1305, gco.getDuration());
			Assertions.assertEquals(4893750, gco.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
			Assertions.assertTrue(ballastBeforeOrAfter(gco, false)); // ballast after
		});
	}

	private List<GeneratedCharterOut> findGCOEvents(Sequence sequence) {
		List<GeneratedCharterOut> charterOuts = new ArrayList<GeneratedCharterOut>();
		for (Event e : sequence.getEvents()) {
			if (e instanceof GeneratedCharterOut) {
				charterOuts.add((GeneratedCharterOut) e);
			}
		}
		return charterOuts;
	}

	private boolean ballastBeforeOrAfter(GeneratedCharterOut charterOut, boolean before) {
		Event event;
		if (before) {
			// note: skipping idles
			event = charterOut.getPreviousEvent().getPreviousEvent();
		} else {
			event = charterOut.getNextEvent();
		}
		if (event instanceof Journey) {
			if (!((Journey) event).isLaden()) {
				return true;
			}
		}
		return false;
	}

}
