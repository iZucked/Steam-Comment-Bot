/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.longshort;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.ServiceHelper;

public class LongShortJSONGenerator {

	public static List<LongShortReportModel> createLongShortData(final ScheduleModel scheduleModel) {
		final List<LongShortReportModel> results = ServiceHelper.withOptionalService(ISlotFilter.class, f -> {
			if (f != null) {
				return createLongShortData(scheduleModel, f);
			}
			else {
				return createLongShortData(scheduleModel, new DefaultSlotFilter());
			}
		});
		return results;
	}
	
	public static List<LongShortReportModel> createLongShortData(final ScheduleModel scheduleModel, final ISlotFilter slotFilter) {
		final Map<YearMonth, Integer> shortsPerMonths = new HashMap<>();
		final Map<YearMonth, Integer> longsPerMonths = new HashMap<>();

		for (final OpenSlotAllocation openSlotAllocation : scheduleModel.getSchedule().getOpenSlotAllocations()) {
			final Slot<?> slot = openSlotAllocation.getSlot();
			if (slot.isCancelled()) {
				continue;
			}
			if (slotFilter.includeSlot(slot)) {
				// Long or short?
				final Map<YearMonth, Integer> positionsPerMonth = slot instanceof LoadSlot ? longsPerMonths : shortsPerMonths;

				final LocalDate d = slot.getWindowStart();
				final YearMonth key = YearMonth.of(d.getYear(), d.getMonth());
				positionsPerMonth.merge(key, 1, Integer::sum);
			}
		}

		for (final CargoAllocation cargoAllocation : scheduleModel.getSchedule().getCargoAllocations()) {
			boolean spotSlot = false;
			boolean includeSlot = true;
			Slot<?> nonSpotSlot = null;
			
			for (SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
				final Slot<?> slot = sa.getSlot();
				includeSlot = includeSlot && slotFilter.includeSlot(slot);
				
				if (slot instanceof SpotSlot) {
					spotSlot = true;
				}
				else {
					nonSpotSlot = slot;
				}
			}
			
			if (spotSlot && includeSlot) {
				// Long or short?
				final Map<YearMonth, Integer> positionsPerMonth = nonSpotSlot instanceof LoadSlot ? longsPerMonths : shortsPerMonths;

				final LocalDate d = nonSpotSlot.getWindowStart();
				final YearMonth key = YearMonth.of(d.getYear(), d.getMonth());
				positionsPerMonth.merge(key, 1, Integer::sum);
			}
		}

		final YearMonth min = findMinYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());
		final YearMonth max = findMaxYearMonth(shortsPerMonths.keySet(), longsPerMonths.keySet());

		if (min == null || max == null) {
			return null;
		}

		final List<LongShortReportModel> longShortReportModels = fillLongShortReportModelRange(min, max, shortsPerMonths, longsPerMonths);
		// jsonOutput(longShortReportModels);
		return longShortReportModels;
	}

	private static List<LongShortReportModel> fillLongShortReportModelRange(final YearMonth min, final YearMonth max, final Map<YearMonth, Integer> shortsPerMonths,
			final Map<YearMonth, Integer> longsPerMonths) {

		final List<YearMonth> months = Stream.iterate(min, date -> date.plusMonths(1)).limit(ChronoUnit.MONTHS.between(min, max) + 1).collect(Collectors.toList());
		final List<LongShortReportModel> longShortReportModels = new ArrayList<>();

		for (final YearMonth month : months) {
			final LongShortReportModel longShortReportModel = new LongShortReportModel();
			longShortReportModel.month = month.getMonthValue();
			longShortReportModel.year = month.getYear();
			longShortReportModel.longs = longsPerMonths.computeIfAbsent(month, key -> 0);
			longShortReportModel.shorts = shortsPerMonths.computeIfAbsent(month, key -> 0);

			longShortReportModels.add(longShortReportModel);
		}

		return longShortReportModels;
	}

	private static YearMonth findMinYearMonth(final Collection<YearMonth> l1, final Collection<YearMonth> l2) {
		YearMonth min = null;
		final Optional<YearMonth> minL1 = l1.stream().min(YearMonth::compareTo);
		final Optional<YearMonth> minL2 = l2.stream().min(YearMonth::compareTo);

		if (!minL1.isPresent() && !minL2.isPresent()) {
			return null;
		}

		if (minL2.isPresent() && minL1.isPresent()) {
			if (minL1.get().isBefore(minL2.get())) {
				min = minL1.get();
			} else {
				min = minL2.get();
			}
		} else if (!minL1.isPresent()) {
			min = minL2.get();
		} else {
			min = minL1.get();
		}
		return min;
	}

	private static YearMonth findMaxYearMonth(final Collection<YearMonth> l1, final Collection<YearMonth> l2) {
		YearMonth max;
		final Optional<YearMonth> maxL1 = l1.stream().max(YearMonth::compareTo);
		final Optional<YearMonth> maxL2 = l2.stream().max(YearMonth::compareTo);

		if (!maxL1.isPresent() && !maxL2.isPresent()) {
			return null;
		}

		if (maxL2.isPresent() && maxL1.isPresent()) {
			if (maxL1.get().isAfter(maxL2.get())) {
				max = maxL1.get();
			} else {
				max = maxL2.get();
			}
		} else if (!maxL1.isPresent()) {
			max = maxL2.get();
		} else {
			max = maxL1.get();
		}
		return max;
	}

	private static void jsonOutput(final List<LongShortReportModel> longShortReportModels) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), longShortReportModels);
		} catch (final JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
