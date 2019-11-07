/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.transformer.config.OptimiserConfigurationOptions.CannotDeserialiseException;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;

public class OptimisationJSONDeserialiser {
	private static final String DEFAULT_STAGE_FILE = "default-stages.json";
	private static final String DEFAULT_PLAN_FILE = "default-plans.json";
	private final DefaultingJsonEngine engine;
	
	public OptimisationJSONDeserialiser(final Map<String, Object> literals, String ...searchPath) {
		engine = new DefaultingJsonEngine(searchPath).withLiteralValues(literals);
	}
	
	public OptimisationStage getOptimisationStageFromJSON(final String jsonId) {
		return getOptimisationStageFromJSON(DEFAULT_STAGE_FILE, jsonId);
	}
	
	
	public OptimisationStage getOptimisationStageFromJSON(final String jsonFilename, final String jsonId) {
		
		// load a file, registering the objects declared in it (if possible)
		try {
			engine.loadFile(jsonFilename);
			// find the object with the specified id, and convert it into plain JSON
			String objectJSON = engine.findObject(jsonId).outputString();
			
			return decodeOptimisationStage(objectJSON);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public OptimisationPlan getOptimisationPlanFromJSON(final String jsonId) {
		return getOptimisationPlanFromJSON(DEFAULT_PLAN_FILE, jsonId);
	}

	/**
	 * Reads an optimisation plan from JSON. Typically, the plan will be incompletely initialised (e.g. because 
	 * there are parameters whose values should be computed from higher-level variables in the user input,
	 * and hence cannot be stored as static values in the JSON), so the plan may need further initialisation
	 * before it can actually be used.  
	 * 
	 * @param jsonFilename
	 * @param jsonId
	 * @return
	 */
	public OptimisationPlan getOptimisationPlanFromJSON(final String jsonFilename, final String jsonId) {
		
		// load a file, registering the objects declared in it (if possible)
		try {
			engine.loadFile(jsonFilename);
			// find the object with the specified id, and convert it into plain JSON
			String objectJSON = engine.findObject(jsonId).outputString();
			
			return decodeOptimisationPlan(objectJSON);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}	
	
	static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper().configure(Feature.ALLOW_COMMENTS, true);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new EMFJacksonModule());
		
		return mapper;
	}
		
	public static OptimisationPlan decodeOptimisationPlan(String plainInputJson) {
		ObjectMapper mapper = getMapper();
		
		try {
			@SuppressWarnings("null")
			ObjectNode root = mapper.readValue(plainInputJson, ObjectNode.class);
			return decodeOptimisationPlan(mapper, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static OptimisationStage decodeOptimisationStage(String plainInputJson) {
		ObjectMapper mapper = getMapper();
		
		try {
			@SuppressWarnings("null")
			ObjectNode root = mapper.readValue(plainInputJson, ObjectNode.class);
			return decodeOptimisationStage(mapper, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
		
	/**
	 * Decodes an optimisation plan from a Jackson parse tree node. A structure should be passed to specify default values for annealing settings and constraint/fitness settings.
	 * The default settings are applied in a fairly crude and naive manner, which may lead to unexpected behaviour in some cases.
	 * 
	 * TODO: replace this method with a custom Jackson deserialiser.
	 * 
	 * @param mapper
	 * @param planNode
	 * @param defaults
	 * @return
	 * @throws JsonProcessingException
	 */
	static OptimisationPlan decodeOptimisationPlan(ObjectMapper mapper, JsonNode planNode) throws JsonProcessingException {
		
		@SuppressWarnings("null")
		UserSettings us = mapper.treeToValue(planNode.get("userSettings"), UserSettingsImpl.class);
		
        OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
        
        plan.setUserSettings(us);
        
        if (planNode.has("resultName")) {
        	plan.setResultName(planNode.get("resultName").asText());
        }
        
        ArrayNode stages = (ArrayNode) planNode.get("stages");
        
        // decode the optimisation stages using special logic based on their name, adding default values
        for (JsonNode stage: stages) {
        	 plan.getStages().add(decodeOptimisationStage(mapper, stage));
        }
		
        plan.setSolutionBuilderSettings(decodeSolutionBuilderSettings(mapper, planNode));
        
		return plan;
		
	}
	
	@SuppressWarnings("null")
	private static SolutionBuilderSettings decodeSolutionBuilderSettings(ObjectMapper mapper, JsonNode node) throws JsonProcessingException {
        // add default settings to the solutionBuilderSettings
        SolutionBuilderSettings solutionBuilderSettings;
        
        if (node.has("solutionBuilderSettings")) {
        	solutionBuilderSettings = mapper.treeToValue(node.get("solutionBuilderSettings"), SolutionBuilderSettings.class);
        }
        else {
        	solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();        	
        }	        
        
        return solutionBuilderSettings;
		
	}
	
	/**
	 * Decodes an optimisation stage from a Jackson parse tree node, using the name of the stage to determine the class that
	 * is instantiated.
	 * 
	 * @param mapper
	 * @param node
	 * @param defaults 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static OptimisationStage decodeOptimisationStage(ObjectMapper mapper, JsonNode node) {
		String name = node.get("name").asText();
		OptimisationStage result;
							
		if (name.equals("parallel")) {
			@SuppressWarnings("rawtypes")
			final ParallelOptimisationStage pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate((ParallisableOptimisationStage) decodeOptimisationStage(mapper, node.get("template")));
			pStage.setJobCount( (Integer) ((IntNode) node.get("jobCount")).numberValue() ); 
			result = pStage; 
		}
		else {

			final Class<? extends OptimisationStage> clazz;
			
			if (node.has("@class")) {
				String className = node.get("@class").asText();
			
				String fullClassName = "com.mmxlabs.models.lng.parameters." + className;
	
				try {
					clazz = (Class<? extends OptimisationStage>) Class.forName(fullClassName);
				} catch (ClassNotFoundException | ClassCastException e) {
					throw new CannotDeserialiseException(String.format("Problem deserialising class with name '%s': %s", className, e.getMessage()));
				}
			}
			else {
				throw new CannotDeserialiseException("No @class property on JSON optimisation stage representation.");
				/*
				if (name.equals("lso")) { clazz = LocalSearchOptimisationStage.class; }
				else if (name.equals("mo-sim-all")) { clazz = MultipleSolutionSimilarityOptimisationStage.class; } 
				else if (name.equals("hill")) { clazz = HillClimbOptimisationStage.class; }
				else if (name.equals("cleanstate")) { clazz = CleanStateOptimisationStage.class; } 
				else if (name.equals("reset")) { clazz = ResetInitialSequencesStage.class; } 
				else if (name.equals("actionset")) { clazz = ActionPlanOptimisationStage.class; } 
				else { 
					throw new CannotDeserialiseException(String.format("Unknown optimisation stage name: '%s'", name));
				}*/
			}
			
			try {
				result = mapper.treeToValue(node, clazz);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
				
		}				

		return result;
	}	
	
	
	/**
	 * Debug method to allow an optimisation plan to be written as JSON for visual inspection.
	 * @param plan
	 * @param filename
	 */
	public static void writeOptimisationPlan(OptimisationPlan plan, String filename) {
		try {
			ObjectMapper mapper = getMapper();
	
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), plan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
