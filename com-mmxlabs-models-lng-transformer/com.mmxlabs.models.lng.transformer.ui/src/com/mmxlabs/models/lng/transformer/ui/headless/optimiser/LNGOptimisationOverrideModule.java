/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.util.Map;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.TypeLiteral;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.RouletteWheelMoveGenerator;

public class LNGOptimisationOverrideModule extends JsonConfiguredModule {

	public LNGOptimisationOverrideModule(JsonNode data) {
		super(data);
		
		// bind the "moveDistributions" name to a Map<String, Double> imported from JSON 
		importers.put("moveDistributions", (name, value) -> this.bindFromJsonNode(value, name, new TypeLiteral<Map<String, Double>>() {} ));
		
		// bind the "shuffle-cutoff" name to an Integer imported from JSON (JSON can't distinguish between different numeric types)
		importers.put("shuffle-cutoff", (name, value) -> this.bindFromJsonNode(value, name, new TypeLiteral<Integer>() {} ));
		
		// bind the "equalDistributions" name to an Boolean imported from JSON (we can auto-detect this)
		importers.put("equalDistributions", (name, value) -> this.bindFromJsonNode(value, name));
		
		// bind the "useLegacyCheck" name to an Boolean imported from JSON (we can auto-detect this)
		importers.put("useLegacyCheck", (name, value) -> this.bindFromJsonNode(value, name));
		
		// use the "useRouletteWheel" import for a custom binding 
		importers.put("useRouletteWheel", (name, value) ->  {
			if (value.asBoolean() == true) {
				bind(RouletteWheelMoveGenerator.class).in(Singleton.class);
				bind(IMoveGenerator.class).to(RouletteWheelMoveGenerator.class);				
			}
		});
	}

}
