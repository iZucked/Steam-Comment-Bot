/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.longshort;

import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class LongShortReportPublisher implements IReportPublisherExtension {

	@Override
	public void publishReport(final IScenarioDataProvider scenarioDataProvider, final ScheduleModel scheduleModel, final OutputStream outputStream) throws Exception {
		final List<LongShortReportModel> longShortReportModels = LongShortJSONGenerator.createLongShortData(scheduleModel);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, longShortReportModels);
	}

	@Override
	public String getReportType() {
		return "LongShortReport";
	}
}
