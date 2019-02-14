/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresReportPublisher implements IReportPublisherExtension {
	
	@Override
	public void publishReport(IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception {

		List<ExposuresReportModel> exposuresModels = ExposuresReportJSONGenerator.createReportData(scheduleModel, scenarioDataProvider);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, exposuresModels);
	}

	@Override
	public String getReportType() {
		return "ExposuresReport";
	}
}
