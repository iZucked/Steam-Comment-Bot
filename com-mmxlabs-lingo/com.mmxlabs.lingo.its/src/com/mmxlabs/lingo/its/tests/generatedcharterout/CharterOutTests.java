/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.generatedcharterout;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;

public class CharterOutTests extends AbstractOptimisationResultTester {

	@Test
	public void UAT_CharterOut_1() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-bonny-point fortin-min days 60.lingo");

		final ScenarioRunner runner = evaluateScenario(url);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		final Schedule schedule = runner.getIntialSchedule();

		List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
		Assert.assertEquals(charterOuts.size(), 0);
	}

	@Test
	public void UAT_CharterOut_2() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-bonny-point fortin.lingo");

		final ScenarioRunner runner = evaluateScenario(url);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		final Schedule schedule = runner.getIntialSchedule();

		List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
		Assert.assertEquals(charterOuts.size(), 1);
		
		GeneratedCharterOut gco = charterOuts.get(0);
		Assert.assertEquals(gco.getPort().getName(), "Bonny Is");
		Assert.assertEquals(gco.getDuration(), 1307);
		Assert.assertEquals(gco.getGroupProfitAndLoss().getProfitAndLoss(), 5173541);
		Assert.assertEquals(ballastBeforeOrAfter(gco, true), true); // ballast before
		Assert.assertEquals(ballastBeforeOrAfter(gco, false), false); // no ballast after
	}
	
	@Test
	public void UAT_CharterOut_3() throws Exception {
		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/charter-out/gco-barca-point fortin.lingo");

		final ScenarioRunner runner = evaluateScenario(url);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		// Should be the same as the updateScenario as we have only called ScenarioRunner#init()
		final Schedule schedule = runner.getIntialSchedule();

		List<GeneratedCharterOut> charterOuts = findGCOEvents(schedule.getSequences().get(0));
		Assert.assertEquals(charterOuts.size(), 1);
		
		GeneratedCharterOut gco = charterOuts.get(0);
		Assert.assertEquals(gco.getPort().getName(), "Barcelona");
		Assert.assertEquals(gco.getDuration(), 1293);
		Assert.assertEquals(gco.getGroupProfitAndLoss().getProfitAndLoss(), 4371850);
		Assert.assertEquals(ballastBeforeOrAfter(gco, true), true); // ballast before
		Assert.assertEquals(ballastBeforeOrAfter(gco, false), true); // ballast after
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
