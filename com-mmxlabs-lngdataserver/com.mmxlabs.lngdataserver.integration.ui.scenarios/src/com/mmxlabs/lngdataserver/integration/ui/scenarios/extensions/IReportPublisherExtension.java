/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions;

import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IReportPublisherExtension {

	void publishReport(IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) throws Exception;

	String getReportType();
}
