package com.mmxlabs.models.lng.transformer.util;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

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
	public static Pair<Date, Date> findEarliestAndLatestTimes(LNGScenarioModel rootObject) {
		Date earliestTime = null;
		Date latestTime = null;
		final CargoModel cargoModel = rootObject.getPortfolioModel().getCargoModel();

		final HashSet<Date> allDates = new HashSet<Date>();

		for (final VesselEvent event : cargoModel.getVesselEvents()) {
			allDates.add(event.getStartBy());
			allDates.add(event.getStartAfter());
		}
		for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
			if (vesselAvailability.isSetStartBy())
				allDates.add(vesselAvailability.getStartBy());
			if (vesselAvailability.isSetStartAfter())
				allDates.add(vesselAvailability.getStartAfter());

			if (vesselAvailability.isSetEndBy())
				allDates.add(vesselAvailability.getEndBy());
			if (vesselAvailability.isSetEndAfter())
				allDates.add(vesselAvailability.getEndAfter());
		}
		for (final Slot s : cargoModel.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}
		for (final Slot s : cargoModel.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}

		earliestTime = allDates.isEmpty() ? new Date(0) : Collections.min(allDates);
		// round down earliest time
		earliestTime = DateAndCurveHelper.roundTimeDown(earliestTime);
		latestTime = allDates.isEmpty() ? new Date(0) : Collections.max(allDates);
		return new Pair<Date, Date>(earliestTime, latestTime);
	}

}
