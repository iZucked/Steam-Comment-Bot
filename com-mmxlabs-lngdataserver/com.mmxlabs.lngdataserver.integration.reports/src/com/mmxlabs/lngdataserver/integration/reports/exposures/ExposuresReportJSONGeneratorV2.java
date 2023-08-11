/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.exposures.ExposureEnumerations.ValueMode;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposureReportView.AssetType;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposureDetailReportView;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposuresTransformer;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
		output.add(new ExposuresReportModelV2());

		// Raw Exposure Information
		final List<SlotAllocation> slotAllocations = new LinkedList<>();
		schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
		output.addAll(ExposuresReportModelV2.doTransform(slotAllocations));	
		final List<PaperDealAllocation> paperDealAllocations = new LinkedList<>();
		schedule.getPaperDealAllocations().forEach(paperDealAllocations::add);
		output.addAll(ExposuresReportModelV2.doTransformPaper(paperDealAllocations));

		// Reduce and add headers
		// TODO
		return output;
	}
}
