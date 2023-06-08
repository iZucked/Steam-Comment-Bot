/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
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
			return publishReport("1", scenarioDataProvider, scheduleModel);
		}
		throw new UnsupportedReportException();
	}

	@Override
	public @NonNull IReportContent publishReport(@NonNull String version, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull ScheduleModel scheduleModel) throws Exception {
		if ("1".equals(version)) {
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

	@Override
	public @NonNull List<@NonNull String> getVersions() {
		return Lists.newArrayList("1");
	}
}
