/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SupportedReportFormats;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IReportPublisherExtension {

	final String DEFAULT_REPORT_UPLOAD_URL = "/scenarios/v1/reports/upload";
	final String DEFAULT_REPORT_FILE_EXTENSION = ".json";

	default IReportContent publishReport(SupportedReportFormats supportedFormats, ScenarioInstance scenarioInstance, IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel)
			throws Exception {
		return publishReport(supportedFormats, scenarioDataProvider, scheduleModel);
	}

	IReportContent publishReport(SupportedReportFormats supportedFormats, IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel) throws Exception;

	String getReportType();

	default String getEndpointURL() {
		return DEFAULT_REPORT_UPLOAD_URL;
	}

	default String getFileExtension() {
		return DEFAULT_REPORT_FILE_EXTENSION;
	}
}
