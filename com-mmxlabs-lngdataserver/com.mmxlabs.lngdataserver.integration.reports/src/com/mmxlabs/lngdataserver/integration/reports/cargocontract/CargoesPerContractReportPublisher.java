/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.cargocontract;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.mmxlabs.lngdataserver.integration.reports.cargocontract.CargoesPerContractJSONGenerator;
import com.mmxlabs.lngdataserver.integration.reports.cargocontract.CargoesPerContractReportModel;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CargoesPerContractReportPublisher implements IReportPublisherExtension {

	@Override
	public void publishReport(IScenarioDataProvider scenarioDataProvider, ScheduleModel scheduleModel, OutputStream outputStream) {
		List<CargoesPerContractReportModel> cargoesPerContractReportModels = CargoesPerContractJSONGenerator.createLongShortData(scheduleModel);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(outputStream,  cargoesPerContractReportModels);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getReportType() {
		return "CargoesPerContractReport";
	}
}
