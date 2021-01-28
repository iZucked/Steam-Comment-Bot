/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.exposures.ExposureEnumerations.ValueMode;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposureReportView.AssetType;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposuresTransformer;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresReportJSONGenerator {

	public static List<ExposuresReportModel> createReportData(final @NonNull ScheduleModel scheduleModel, final @NonNull IScenarioDataProvider scenarioDataProvider, //
			final ScenarioResult scenarioResult) {

		Schedule schedule = scheduleModel.getSchedule();
		if (schedule == null) {
			return null;
		}
		return createTransformedData(schedule, scenarioDataProvider, scenarioResult);
	}

	public static List<ExposuresReportModel> createTransformedData(final @NonNull Schedule schedule, final @NonNull IScenarioDataProvider scenarioDataProvider, //
			final ScenarioResult scenarioResult) {

		final PricingModel pm = ScenarioModelUtil.getPricingModel(scenarioDataProvider);
		final List<CommodityCurve> indices = pm.getCommodityCurves();
		List<IndexExposureData> temp = new LinkedList<>();

		final YearMonth ymStart = YearMonth.from(getEarliestScenarioDate(scenarioDataProvider));
		final YearMonth ymEnd = YearMonth.from(getLatestScenarioDate(scenarioDataProvider));

		for (YearMonth cym = ymStart; cym.isBefore(ymEnd); cym = cym.plusMonths(1)) {

			IndexExposureData exposuresByMonth = 
					ExposuresTransformer.getExposuresByMonth(scenarioResult, schedule, cym, ValueMode.VOLUME_MMBTU,  Collections.emptyList(), null, -1, AssetType.NET, false);

			if (exposuresByMonth.exposures.size() != 0.0) {
				temp.add(exposuresByMonth);
			}
		}
		List<ExposuresReportModel> output = ExposuresReportModel.doTransform(temp, indices);
		return output;
	}

	private static void jsonOutput(final List<ExposuresReportModel> models) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("/tmp/user.json"), models);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static LocalDate getEarliestScenarioDate(final IScenarioDataProvider sdp) {
		LocalDate result = LocalDate.now();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (erl.isAfter(ls.getWindowStart())) {
				erl = ls.getWindowStart();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (erl.isAfter(ds.getWindowStart())) {
				erl = ds.getWindowStart();
			}
		}
		if (erl.isBefore(result)) {
			result = erl;
		}

		return result;
	}

	private static LocalDate getLatestScenarioDate(final IScenarioDataProvider sdp) {
		LocalDate result = LocalDate.now();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (erl.isBefore(ls.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (erl.isBefore(ds.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		if (erl.isAfter(result)) {
			result = erl;
		}

		return result;
	}
}
