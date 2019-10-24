/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.OutputStream;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ExposuresReportPublisher implements IReportPublisherExtension {
	
	@Override
	public void publishReport(ScenarioInstance scenarioInstance, IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception {

		final ScenarioResult sr = new ScenarioResult(scenarioInstance, scheduleModel);
		List<ExposuresReportModel> exposuresModels = ExposuresReportJSONGenerator.createReportData(scheduleModel, scenarioDataProvider, sr);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, exposuresModels);
	}

	@Override
	public String getReportType() {
		return "ExposuresReport";
	}

	@Override
	public void publishReport(@NonNull IScenarioDataProvider scenarioDataProvider, @NonNull ScheduleModel scheduleModel, @NonNull OutputStream outputStream) throws Exception {
		throw new UnsupportedOperationException();
	}
}
