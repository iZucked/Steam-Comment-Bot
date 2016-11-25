/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class LNGScenarioUtils {
	
	public static final ZonedDateTime EarliestDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

	/**
	 * Find the earliest and latest times set by events in the model. This takes into account:
	 * 
	 * <ul>
	 * <li>Slot window dates</li>
	 * <li>Vessel event dates</li>
	 * <li>Vessel availability dates</li>
	 * </ul>
	 */
	public static Pair<ZonedDateTime, ZonedDateTime> findEarliestAndLatestTimes(@NonNull final LNGScenarioModel rootObject) {
		ZonedDateTime earliestTime = null;
		ZonedDateTime latestTime = null;
		final CargoModel cargoModel = rootObject.getCargoModel();

		final HashSet<ZonedDateTime> allDates = new HashSet<>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			allDates.add(event.getStartByAsDateTime());
			allDates.add(event.getStartAfterAsDateTime());
			assert !allDates.contains(null);
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
			
			assert !allDates.contains(null);
		}
		for (final Slot s : cargoModel.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTimeWithFlex());
			allDates.add(s.getWindowEndWithSlotOrPortTimeWithFlex());
			
			assert !allDates.contains(null);
		}
		for (final Slot s : cargoModel.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTimeWithFlex());
			allDates.add(s.getWindowEndWithSlotOrPortTimeWithFlex());
			
			assert !allDates.contains(null);
		}

		earliestTime = allDates.isEmpty() ? EarliestDate : Collections.min(allDates);
//		 round down earliest time
//		earliestTime = DateAndCurveHelper.roundTimeDown(earliestTime);
		latestTime = allDates.isEmpty() ? EarliestDate : Collections.max(allDates);
		return new Pair<ZonedDateTime, ZonedDateTime>(earliestTime, latestTime);
	}

}
