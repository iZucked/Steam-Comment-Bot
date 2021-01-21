/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import org.json.simple.JSONArray;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON;

public class HeadlessOptimiserJSON extends HeadlessGenericJSON<HeadlessGenericJSON.Params, HeadlessOptimiserJSON.Metrics>{
	private OptimisationPlan plan;

	public HeadlessOptimiserJSON() {
	}


	public static class Metrics extends HeadlessGenericJSON.Metrics {
		private JSONArray stages;

		public JSONArray getStages() {
			return stages;
		}

		public void setStages(JSONArray stages) {
			this.stages = stages;
		}
	}


	public void setOptimisationPlan(OptimisationPlan optimisationPlan) {
		this.plan = optimisationPlan;		
	}
	
	public OptimisationPlan getOptimisationPlan() {
		return plan;
	}

}
