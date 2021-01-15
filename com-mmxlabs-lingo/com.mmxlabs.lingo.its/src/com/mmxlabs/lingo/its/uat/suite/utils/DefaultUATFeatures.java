/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;

public class DefaultUATFeatures extends FeatureBasedUAT {

	private List<IdMapContainer> getAllDetails(CargoAllocation cargoAllocation) {
		List<IdMapContainer> allDetails = new ArrayList<IdMapContainer>();
		addDefaultDetails(allDetails, cargoAllocation);
		return allDetails;
	}

	@Override
	protected List<IdMapContainer> getIdMapContainers(Schedule schedule, String cargoName) {
		final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation(cargoName, schedule);
		Assertions.assertTrue(cargoAllocation != null, "Failed to find allocation: " + cargoName);
		return getAllDetails(cargoAllocation);
	}

	@Override
	protected void additionalChecks(Schedule schedule, String cargoName) {
	}

}
