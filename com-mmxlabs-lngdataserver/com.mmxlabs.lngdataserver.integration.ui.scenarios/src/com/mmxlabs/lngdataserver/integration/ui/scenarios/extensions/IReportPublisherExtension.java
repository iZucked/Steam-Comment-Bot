/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IReportPublisherExtension {

	final String DEFAULT_REPORT_UPLOAD_URL = "/scenarios/v1/reports/upload";
	final String DEFAULT_REPORT_FILE_EXTENSION = ".json";
	
	default void publishReport(ScenarioInstance scenarioInstance, IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception{
		publishReport(scenarioDataProvider, scheduleModel, outputStream);
	}

	void publishReport(IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception;
	
	String getReportType();
	
	default String getEndpointURL() {
		return DEFAULT_REPORT_UPLOAD_URL;
	}
	
	default String getFileExtension() {
		return DEFAULT_REPORT_FILE_EXTENSION;
	}
}
