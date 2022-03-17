/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.DefaultReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.UnsupportedReportException;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class NominationsReportPublisher implements IReportPublisherExtension {

	@Override
	public IReportContent publishReport(final SupportedReportFormats supportedFormats, final IScenarioDataProvider scenarioDataProvider, final ScheduleModel scheduleModel) throws Exception {

		final List<String> versions = supportedFormats.getVersionsFor(getReportType());

		if (versions.isEmpty() || versions.contains("1")) {
			final List<Nominations> models = NominationsJSONGenerator.createNominationsData(scenarioDataProvider);
			final ObjectMapper objectMapper = new ObjectMapper();
			final String content = objectMapper.writeValueAsString(models);
			if (content != null) {
				return new DefaultReportContent(getReportType(), "1", content);
			}
		}
		throw new UnsupportedReportException();
	}

	@Override
	public String getReportType() {
		return "NominationsReport";
	}
}
