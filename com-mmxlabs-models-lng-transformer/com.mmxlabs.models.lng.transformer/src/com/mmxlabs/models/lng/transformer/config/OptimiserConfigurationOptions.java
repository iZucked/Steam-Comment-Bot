/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

/**
 * This class represents configuration options for the optimiser that are deserialised from 
 * a JSON config file. The options are divided into:
 * <ol>
 *   <li>an {@link OptimisationPlan} object</li>
 *   <li>some data that is provided to the optimiser by the dependency injection</li>
 *   <li>an {@link LSOLogger.LoggingParameters} object</li>
 *   <li>everything else</li>
 * </ol>
 * 
 * Apart from the {@link OptimisationPlan} and the {@link LSOLogger.LoggingParameters}, the data
 * is currently stored in an ad-hoc manner as a JsonNode from the Jackson parse tree.
 * 
 * @author simonmcgregor
 *
 */
public class OptimiserConfigurationOptions {
	@SuppressWarnings("serial")
	public static class CannotDeserialiseException extends RuntimeException {

		public CannotDeserialiseException(@NonNull String message) {
			super(message);
		}
	}
	/**
	 * Inner class to hold optimisation defaults. These are used in creating the optimisation plan.
	 * 
	 * @author simonmcgregor
	 *
	 */
	public static class OptimisationDefaults {
		public AnnealingSettings annealing;
		public ConstraintAndFitnessSettings constraintAndFitnessSettings;
		
		public OptimisationDefaults(AnnealingSettings annealing, ConstraintAndFitnessSettings constraint) {
			this.annealing = annealing;
			this.constraintAndFitnessSettings = constraint;
		}
	}	
	
	public OptimisationPlan plan;
	public JsonNode injections; // hack: pull this directly from JSON
	public JsonNode other;
	private Integer rawNumThreads = null;
	
	public int getNumThreads() {
		if (rawNumThreads != null) {
			return rawNumThreads;
		}
		return other.get("numThreads").asInt();
	}
	
	/**
	 * Forces the number of threads to a particular value. 
	 * @param n
	 */
	public void overrideNumThreads(int n) {
		rawNumThreads = n;
	}
	
	public static OptimiserConfigurationOptions readFromFile(String jsonFilename, Map<String, String> customInfo) {
		try {
			String inputJSONX = new String(Files.readAllBytes(Paths.get(jsonFilename)));
			
			HashMap<String, Object> inputParameters = new HashMap<>();
			// populate input with "custom-" literals
			customInfo.forEach( (k, v) -> inputParameters.put("custom-" + k, v) );
			
			JSONObject obj = new JSONObject(inputJSONX);
			
			DefaultingJsonEngine engine = new DefaultingJsonEngine(".").withLiteralValues(inputParameters);
			
			String inputPlainJSON = engine.newDefaultingJsonStructure(obj).outputString();
			
			return readFromPlainJSONString(inputPlainJSON);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@SuppressWarnings("null")
	public static OptimiserConfigurationOptions readFromPlainJSONString(String plainInputJSON) {
		OptimiserConfigurationOptions result = new OptimiserConfigurationOptions();
		
		try {
 			ObjectMapper mapper = OptimisationJSONDeserialiser.getMapper();
			
			ObjectNode root = mapper.readValue(plainInputJSON, ObjectNode.class);
			
			result.plan = OptimisationJSONDeserialiser.decodeOptimisationPlan(mapper, root.get("plan"));	
			
			result.injections = mapper.treeToValue(root.get("injections"), JsonNode.class);
			
			result.other = mapper.treeToValue(root.get("other"), JsonNode.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Describes whether or not the specified options require any injections into the injection framework.
	 * @return
	 */
	public static boolean requiresInjections(OptimiserConfigurationOptions options) {
		return options != null && options.injections != null;
	}
	
}	
	