package com.mmxlabs.lngdataserver.server.reports;

import java.util.List;

import javax.servlet.ServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.reports.schedule.ScheduleReportJSONGeneratorV2;
import com.mmxlabs.lngdataserver.integration.reports.schedule.ScheduleReportModelV2;
import com.mmxlabs.lngdataserver.server.api.IReportPublisher;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class ScheduleChartPublisher implements IReportPublisher {

	@Override
	public String getReportName() {
		return "schedulechart";
	}

	@Override
	public String getJSONData(ScenarioResult sr, ServletRequest req) throws Exception {

		final ScheduleModel scheduleModel = sr.getTypedResult(ScheduleModel.class);

		final List<ScheduleReportModelV2> scheduleReportModels = ScheduleReportJSONGeneratorV2.createScheduleData(scheduleModel);

		final ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(scheduleReportModels);
	}
}
