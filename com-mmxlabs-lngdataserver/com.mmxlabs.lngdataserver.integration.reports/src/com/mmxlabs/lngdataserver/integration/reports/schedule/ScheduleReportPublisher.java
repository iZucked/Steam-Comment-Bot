/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.schedule;

import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ScheduleReportPublisher implements IReportPublisherExtension {

	@Override
	public void publishReport(IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception {
		List<ScheduleReportModel> scheduleReportModels = ScheduleReportJSONGenerator.createScheduleData(scheduleModel);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, scheduleReportModels);

	}

	@Override
	public String getReportType() {
		return "ScheduleReport";
	}
}
