/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
			Assertions.assertEquals(1, charterOuts.size());

			GeneratedCharterOut gco = charterOuts.get(0);
			Assertions.assertEquals("Bonny Is", gco.getPort().getName());
			Assertions.assertEquals(5382, gco.getDuration());
			Assertions.assertEquals(12558000, gco.getGroupProfitAndLoss().getProfitAndLoss());
			Assertions.assertFalse(ballastBeforeOrAfter(gco, true)); // ballast before
			Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // ballast after
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
			Assertions.assertEquals(3, charterOuts.size());
			{
				GeneratedCharterOut gco = charterOuts.get(0);
				Assertions.assertEquals("Bonny Is", gco.getPort().getName());
				Assertions.assertEquals(5382, gco.getDuration());
				Assertions.assertEquals(12558000, gco.getGroupProfitAndLoss().getProfitAndLoss());
				Assertions.assertFalse(ballastBeforeOrAfter(gco, true)); // ballast before
				Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // no ballast after
			}
			{
				GeneratedCharterOut gco = charterOuts.get(1);
				Assertions.assertEquals("Bonny Is", gco.getPort().getName());
				Assertions.assertEquals(1343, gco.getDuration());
				Assertions.assertEquals(5316041, gco.getGroupProfitAndLoss().getProfitAndLoss());
				Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
				Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // no ballast after
			}
			{
				GeneratedCharterOut gco = charterOuts.get(2);
				Assertions.assertEquals("Barcelona", gco.getPort().getName());
				Assertions.assertEquals(913, gco.getDuration());
				Assertions.assertEquals(4565000, gco.getGroupProfitAndLoss().getProfitAndLoss());
				Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
				Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // no ballast after
			}
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
			Assertions.assertEquals(2, charterOuts.size());

			{
				GeneratedCharterOut gco = charterOuts.get(0);
				Assertions.assertEquals("Barcelona", gco.getPort().getName());
				Assertions.assertEquals(1329, gco.getDuration());
				Assertions.assertEquals(4983750, gco.getGroupProfitAndLoss().getProfitAndLoss());
				Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
				Assertions.assertTrue(ballastBeforeOrAfter(gco, false)); // ballast after
			}
			{
				GeneratedCharterOut gco = charterOuts.get(1);
				Assertions.assertEquals("Barcelona", gco.getPort().getName());
				Assertions.assertEquals(913, gco.getDuration());
				Assertions.assertEquals(4565000, gco.getGroupProfitAndLoss().getProfitAndLoss());
				Assertions.assertTrue(ballastBeforeOrAfter(gco, true)); // ballast before
				Assertions.assertFalse(ballastBeforeOrAfter(gco, false)); // ballast after
			}
		});
	}

	private List<GeneratedCharterOut> findGCOEvents(Sequence sequence) {
		List<GeneratedCharterOut> charterOuts = new ArrayList<>();
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
			Journey journey = (Journey) event;
			if (!journey.isLaden()) {
				return true;
			}
		}
		return false;
	}

}
