/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.Collections;
import java.util.HashSet;

import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.DateTime;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class LNGScenarioUtils {

	/**
	 * Find the earliest and latest times set by events in the model. This takes into account:
	 * 
	 * <ul>
	 * <li>Slot window dates</li>
	 * <li>Vessel event dates</li>
	 * <li>Vessel availability dates</li>
	 * </ul>
	 */
	public static Pair<DateTime, DateTime> findEarliestAndLatestTimes(@NonNull final LNGScenarioModel rootObject) {
		DateTime earliestTime = null;
		DateTime latestTime = null;
		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();

		final HashSet<DateTime> allDates = new HashSet<>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			allDates.add(event.getStartByAsDateTime());
			allDates.add(event.getStartAfterAsDateTime());
		}
		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			if (vesselAvailability.isSetStartBy())
				allDates.add(vesselAvailability.getStartByAsDateTime());
			if (vesselAvailability.isSetStartAfter())
				allDates.add(vesselAvailability.getStartAfterAsDateTime());

			if (vesselAvailability.isSetEndBy())
				allDates.add(vesselAvailability.getEndByAsDateTime());
			if (vesselAvailability.isSetEndAfter())
				allDates.add(vesselAvailability.getEndAfterAsDateTime());
		}
		for (final Slot s : cargoModel.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTimeWithFlex());
			allDates.add(s.getWindowEndWithSlotOrPortTimeWithFlex());
		}
		for (final Slot s : cargoModel.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTimeWithFlex());
			allDates.add(s.getWindowEndWithSlotOrPortTimeWithFlex());
		}

		earliestTime = allDates.isEmpty() ? new DateTime(0) : Collections.min(allDates);
//		 round down earliest time
//		earliestTime = DateAndCurveHelper.roundTimeDown(earliestTime);
		latestTime = allDates.isEmpty() ? new DateTime(0) : Collections.max(allDates);
		return new Pair<DateTime, DateTime>(earliestTime, latestTime);
	}

}
