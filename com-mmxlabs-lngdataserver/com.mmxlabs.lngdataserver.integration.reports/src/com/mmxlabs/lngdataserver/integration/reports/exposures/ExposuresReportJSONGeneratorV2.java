/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresReportJSONGeneratorV2 {
	
	public static List<ExposuresReportModelV2> createReportData(final @NonNull ScheduleModel scheduleModel, final @NonNull IScenarioDataProvider scenarioDataProvider) {
		Schedule schedule = scheduleModel.getSchedule();
		if (schedule == null) {
			return null;
		}
		return createTransformedData(schedule, scenarioDataProvider);
	}

	public static List<ExposuresReportModelV2> createTransformedData(final @NonNull Schedule schedule, final @NonNull IScenarioDataProvider scenarioDataProvider) {		
		List<ExposuresReportModelV2> output = new ArrayList<>();

		// Raw Exposure Information
		final List<SlotAllocation> slotAllocations = new LinkedList<>();
		schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
		output.addAll(ExposuresReportModelV2.doTransformSlot(slotAllocations));	
		final List<PaperDealAllocation> paperDealAllocations = new LinkedList<>();
		schedule.getPaperDealAllocations().forEach(paperDealAllocations::add);
		output.addAll(ExposuresReportModelV2.doTransformPaper(paperDealAllocations));

		// Join on
			// Deal, Index, Contract Month, Units (Expected equal)
			// Minimise Hedge Period Start
			// Maximise Hedge Period End
			// Sum volume and currency value
		final HashMap<String, ExposuresReportModelV2> map = new HashMap<>();
		for (ExposuresReportModelV2 model : output) {
			final String key = model.deal + model.marketIndex + model.contractMonth + model.currencyUnit + model.volumeUnit;
			map.merge(key, model,
				(v1, v2) -> {
					v1.hedgeStart = v1.hedgeStart.compareTo(v2.hedgeStart) <= 0 ? v1.hedgeStart : v2.hedgeStart;
					v1.hedgeEnd = v1.hedgeEnd.compareTo(v2.hedgeEnd) > 0 ? v1.hedgeEnd : v2.hedgeEnd;
					v1.currencyValue += v2.currencyValue;
					v1.volume += v2.volume;
					return v1;
				}
			);
		}
		output = new ArrayList<>(map.values());
		// Add headers
			// TODO Address sorting in datahub
			// ie. Sorting by a column will break the order of header rows
			// need to add datahub support first for this feature

		return output;
	}
}