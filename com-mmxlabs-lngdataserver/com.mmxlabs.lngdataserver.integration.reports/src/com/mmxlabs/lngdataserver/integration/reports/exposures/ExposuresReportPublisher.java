/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.exposures;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.DefaultReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresReportPublisher implements IReportPublisherExtension {

	@Override
	public String getReportType() {
		return "ExposuresReport";
	}

	@Override
	public IReportContent publishReport(final SupportedReportFormats supportedFormats, final IScenarioDataProvider scenarioDataProvider, final ScheduleModel scheduleModel) throws Exception {
		final List<String> versions = supportedFormats.getVersionsFor(getReportType());

		if (versions.isEmpty() || versions.contains("1")) {

			final List<ExposuresReportModel> models = ExposuresReportJSONGenerator.createReportData(scheduleModel, scenarioDataProvider);

			final ObjectMapper objectMapper = new ObjectMapper();
			final String content = objectMapper.writeValueAsString(models);
			return new DefaultReportContent(getReportType(), "1", content);
		}
		
		throw new UnsupportedOperationException();
	}
}
