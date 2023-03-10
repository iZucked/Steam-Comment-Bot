/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.reports;

import javax.servlet.ServletRequest;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lngdataserver.integration.reports.cargo.CargoReportJSONGenerator;
import com.mmxlabs.lngdataserver.server.api.IReportPublisher;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CargoReportPublisher implements IReportPublisher {

	@Override
	public String getReportName() {
		return "CargoReport";
	}

	@Override
	public String getJSONData(@NonNull final ScenarioResult sr, @NonNull final ServletRequest request) throws Exception {
		final ScheduleModel scheduleModel = sr.getTypedResult(ScheduleModel.class);
		if (scheduleModel == null) {
			return "{}";
		}
		return CargoReportJSONGenerator.createSchemaAndReportData(Mode.FULL, scheduleModel);
	}
}
