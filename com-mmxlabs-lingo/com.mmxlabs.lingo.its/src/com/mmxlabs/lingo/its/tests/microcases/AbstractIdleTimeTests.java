/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;

/**
 * Common code for IdleTimeTests.
 */
public abstract class AbstractIdleTimeTests extends AbstractMicroTestCase {
	private static List<String> requiredFeatures = Lists.newArrayList("contingency-idle-time");
	private static List<String> addedFeatures = new LinkedList<>();

	@BeforeAll
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterAll
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	protected void validateCargoIdleTime(LNGScenarioRunner scenarioRunner, int expectedLadenIdleTime, int expectedBallastIdleTime) {
		final Schedule schedule = scenarioRunner.evaluateInitialState();
		final SimpleCargoAllocation cargoAllocation = new SimpleCargoAllocation(schedule.getCargoAllocations().get(0));
		final Idle ladenIdle = cargoAllocation.getLadenIdle();
		final Idle ballastIdle = cargoAllocation.getBallastIdle();			
		Assertions.assertEquals(expectedLadenIdleTime, ladenIdle.getDuration());
		Assertions.assertEquals(expectedBallastIdleTime, ballastIdle.getDuration());
	}
	
	protected void validateSlotVisitDuration(LNGScenarioRunner scenarioRunner, String slotName, int expectedTimeAtSlot) {
		final Schedule schedule = scenarioRunner.evaluateInitialState();
		final CargoAllocation cargoAllocation = schedule.getCargoAllocations().get(0);
		for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
			if (Objects.equals(sa.getName(), slotName)) {
				Assertions.assertEquals(expectedTimeAtSlot, sa.getSlotVisit().getDuration());
			}
		}
	}
}
