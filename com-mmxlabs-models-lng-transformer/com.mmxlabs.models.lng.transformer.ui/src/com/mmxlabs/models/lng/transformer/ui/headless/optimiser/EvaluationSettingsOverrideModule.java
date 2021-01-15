/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;

public class EvaluationSettingsOverrideModule extends JsonConfiguredModule {

	public EvaluationSettingsOverrideModule(JsonNode data) {
		super(data);
		// bind the "latenessComponentParameters" import using a custom method
		importers.put("latenessComponentParameters", (name, value) -> bind(ILatenessComponentParameters.class).toInstance(parseLatenessComponentParameters(value)));
		
		// bind the "latenessComponentParameters" import using a custom method
		importers.put("excessIdleTimeComponentParameters", (name, value) -> bind(IExcessIdleTimeComponentParameters.class).toInstance(parseExcessIdleTimeComponentParameters(value)));
		
		// bind the "latenessComponentParameters" import using a default binding (but a custom name)		
		importers.put("optimiserReevaluate", (name, value) -> this.bindFromJsonNode(value, LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE));
	}

	/**
	 * Returns an {@link IExcessIdleTimeComponentParameters} object created from a {@link JsonNode} object using custom code.
	 * @param node
	 * @return
	 */
	private @NonNull IExcessIdleTimeComponentParameters parseExcessIdleTimeComponentParameters(JsonNode node) {
		ExcessIdleTimeComponentParameters result = new ExcessIdleTimeComponentParameters();

		IExcessIdleTimeComponentParameters.Interval[] intervals = IExcessIdleTimeComponentParameters.Interval.values();
		
		for (IExcessIdleTimeComponentParameters.Interval interval: intervals) {
			String name = interval.toString();
			result.setWeight(interval, node.get(name).get("weight").asInt());
			result.setThreshold(interval, node.get(name).get("threshold").asInt());
		}
		
		result.setEndWeight(node.get("endWeight").asInt());
		
		return result;
	}

	/**
	 * Returns an {@link LatenessComponentParameters} object created from a {@link JsonNode} object using custom code.
	 * @param node
	 * @return
	 */
	private @NonNull LatenessComponentParameters parseLatenessComponentParameters(JsonNode node) {
		LatenessComponentParameters result = new LatenessComponentParameters();
		
		ILatenessComponentParameters.Interval[] intervals = ILatenessComponentParameters.Interval.values();
		
		for (ILatenessComponentParameters.Interval interval: intervals) {
			String name = interval.toString();
			
			result.setHighWeight(interval, node.get(name).get("highWeight").asInt());
			result.setLowWeight(interval, node.get(name).get("lowWeight").asInt());
			result.setThreshold(interval, node.get(name).get("threshold").asInt());
			
		}
		return result;
		
	}



}
