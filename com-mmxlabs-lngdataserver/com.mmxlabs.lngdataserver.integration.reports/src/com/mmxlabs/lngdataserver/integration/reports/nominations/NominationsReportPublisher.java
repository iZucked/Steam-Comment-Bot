/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class NominationsReportPublisher implements IReportPublisherExtension {

	@Override
	public void publishReport(final IScenarioDataProvider scenarioDataProvider, final ScheduleModel scheduleModel, final OutputStream outputStream) throws Exception {
		final LNGScenarioModel scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		final List<Nominations> nominationsExportModels = NominationsJSONGenerator.createNominationsData(scenarioModel);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, nominationsExportModels);
	}

	@Override
	public String getReportType() {
		return "NominationsReport";
	}
}
