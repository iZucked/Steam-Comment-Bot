/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.annotation.NonNull;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ResetInitialSequencesStage;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;

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
	public LSOLogger.LoggingParameters loggingParameters;
	public JsonNode other;
	
	public int getNumThreads() {
		return other.get("numThreads").asInt();
	}
	
	public static OptimiserConfigurationOptions readFromFile(String jsonFilename) {
		try {
			String inputJSONX = new String(Files.readAllBytes(Paths.get(jsonFilename)));
			
			JSONObject obj = new JSONObject(inputJSONX);
			
			DefaultingJsonEngine engine = new DefaultingJsonEngine(".");
			
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
			
			result.loggingParameters = decodeLoggingParameters(mapper, root.get("loggingParameters"));
			
			result.other = mapper.treeToValue(root.get("other"), JsonNode.class);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	private static LSOLogger.LoggingParameters decodeLoggingParameters(ObjectMapper mapper, JsonNode node) {
		LoggingParameters result = new LSOLogger.LoggingParameters();
		result.loggingInterval = node.get("loggingInterval").asInt();
		result.doLogRejectedFitnesses = node.get("doLogRejectedFitnesses").asBoolean();
		result.doLogAcceptedFitnesses = node.get("doLogAcceptedFitnesses").asBoolean();
		return result;
	}
	
}	
	