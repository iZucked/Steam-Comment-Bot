/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.IExtraDataProvider;
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
	public static Pair<ZonedDateTime, ZonedDateTime> findEarliestAndLatestTimes(@NonNull final LNGScenarioModel rootObject, @Nullable final IExtraDataProvider extraDataProvider) {
		if (extraDataProvider != null) {
			return findEarliestAndLatestTimes(rootObject, extraDataProvider.getExtraLoads(), extraDataProvider.getExtraDischarges(), extraDataProvider.getExtraVesselEvents(),
					extraDataProvider.getExtraVesselCharters());
		} else {
			return findEarliestAndLatestTimes(rootObject, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
		}
	}

	public static Pair<ZonedDateTime, ZonedDateTime> findEarliestAndLatestTimes(@NonNull final LNGScenarioModel rootObject, final List<LoadSlot> extraLoadSlots,
			final List<DischargeSlot> extraDischargeSlots, final List<VesselEvent> extraVesselEvents, final List<VesselCharter> extraVesselCharters) {
		ZonedDateTime earliestTime = null;
		ZonedDateTime latestTime = null;
		final CargoModel cargoModel = rootObject.getCargoModel();

		final HashSet<ZonedDateTime> allDates = new HashSet<>();

		Stream.concat(cargoModel.getVesselEvents().stream(), extraVesselEvents.stream()).forEach(event -> {
			allDates.add(event.getStartByAsDateTime());
			allDates.add(event.getStartAfterAsDateTime());
			assert !allDates.contains(null);
		});
		Stream.concat(cargoModel.getVesselCharters().stream(), extraVesselCharters.stream()).forEach(vesselCharter -> {
			if (vesselCharter.isSetStartBy())
				allDates.add(vesselCharter.getStartByAsDateTime());
			if (vesselCharter.isSetStartAfter())
				allDates.add(vesselCharter.getStartAfterAsDateTime());

			if (vesselCharter.isSetEndBy())
				allDates.add(vesselCharter.getEndByAsDateTime());
			if (vesselCharter.isSetEndAfter())
				allDates.add(vesselCharter.getEndAfterAsDateTime());

			assert !allDates.contains(null);
		});

		Stream.concat(cargoModel.getLoadSlots().stream(), extraLoadSlots.stream()).forEach(s -> {
			allDates.add(s.getSchedulingTimeWindow().getStart());
			allDates.add(s.getSchedulingTimeWindow().getEndWithFlex());

			assert !allDates.contains(null);
		});

		Stream.concat(cargoModel.getDischargeSlots().stream(), extraDischargeSlots.stream()).forEach(s -> {
			allDates.add(s.getSchedulingTimeWindow().getStart());
			allDates.add(s.getSchedulingTimeWindow().getEndWithFlex());

			assert !allDates.contains(null);
		});

		earliestTime = allDates.isEmpty() ? EarliestDate : Collections.min(allDates);
		// round down earliest time
		// earliestTime = DateAndCurveHelper.roundTimeDown(earliestTime);
		latestTime = allDates.isEmpty() ? EarliestDate : Collections.max(allDates);
		return new Pair<>(earliestTime, latestTime);
	}

}
