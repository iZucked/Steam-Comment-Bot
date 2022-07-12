/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;

public class HeadlessGenericJSONTransformer<Params extends HeadlessGenericJSON.Params, Metrics extends HeadlessGenericJSON.Metrics, T extends HeadlessGenericJSON<Params,Metrics>> {

	@SuppressWarnings("null")
	public T createJSONResultObject(Class<Params> clazzP, Class<Metrics> clazzM, Class<T> clazzT) {
		//HeadlessOptioniserJSON result = new HeadlessOptioniserJSON();
		T result;
		try {
			result = clazzT.getDeclaredConstructor().newInstance();
			result.setMetrics(createMetrics(clazzM));
			result.setMeta(createMetaData());
			result.setParams(createParams(clazzP));
			result.setScenarioMeta(createScenarioMeta());
			return result;
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	public void setBasicProperties(T json, JSONObject machineType, String scenarioFileName, int threads) {
		json.getMeta().setMachineType(machineType);
		json.getMeta().setScenario(scenarioFileName);
		json.getParams().setCores(threads);
	}

	@SuppressWarnings("null")
	private Metrics createMetrics(Class<Metrics> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("null")
	private Params createParams(Class<Params> clazz) {
		try {
			Params params = clazz.getDeclaredConstructor().newInstance();
			params.setCores(0);
			return params;
		}
		catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Meta createMetaData() {
		HeadlessGenericJSON.Meta meta = new HeadlessGenericJSON.Meta();
		meta.setMachineType(new JSONObject());
		meta.setVersion("");
		meta.setScenario("");
		meta.setClient("");
		meta.setDate(LocalDateTime.now());

		return meta;
	}
	
	private ScenarioMeta createScenarioMeta() {
		HeadlessGenericJSON.ScenarioMeta scenarioMeta = new HeadlessGenericJSON.ScenarioMeta();
		
		return scenarioMeta;
	}
}
