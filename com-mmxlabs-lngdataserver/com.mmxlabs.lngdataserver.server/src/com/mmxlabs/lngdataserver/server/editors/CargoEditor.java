package com.mmxlabs.lngdataserver.server.editors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.server.api.IDatasetEditor;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CargoEditor implements IDatasetEditor {

	@Override
	public String getEditorName() {
		return "tradestable";
	}

	@Override
	public String getJSONData(IScenarioDataProvider sdp, ServletRequest request) throws Exception {

		CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);

		final JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

		final ObjectMapper mapper = new ObjectMapper(jsonFactory);
		mapper.registerModule(new EMFJacksonModule());
		final String loadsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getLoadSlots());
		final String dischargesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getDischargeSlots());
		final String cargoesJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getCargoes());

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getCargoes()));
		sb.append(",");
		sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getLoadSlots()));
		sb.append(",");
		sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cargoModel.getDischargeSlots()));
		sb.append("]");

		return sb.toString();
	}

	@Override
	public String update(IScenarioDataProvider sdp, HttpServletRequest r) throws Exception {
		return "{}";
	}

}
