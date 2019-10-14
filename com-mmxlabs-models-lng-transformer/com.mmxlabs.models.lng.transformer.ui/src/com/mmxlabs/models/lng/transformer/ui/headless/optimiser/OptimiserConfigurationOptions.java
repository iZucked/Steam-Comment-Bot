/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallisableOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
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
		OptimiserConfigurationOptions result = new OptimiserConfigurationOptions();
		
		try {
 			ObjectMapper mapper = new ObjectMapper().configure(Feature.ALLOW_COMMENTS, true);
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.registerModule(new EMFJacksonModule());
			SimpleModule module = new SimpleModule();
			mapper.registerModule(module);

			ObjectNode root = mapper.readValue(new File(jsonFilename), ObjectNode.class);
			
			// get the default values for AnnealingSettings and ConstraintAndFitnessSettings from the config file
			AnnealingSettings annealing = mapper.treeToValue(root.get("defaults").get("annealingSettings"), AnnealingSettings.class);
			ConstraintAndFitnessSettings constraint = mapper.treeToValue(root.get("defaults").get("constraintAndFitnessSettings"), ConstraintAndFitnessSettings.class);
			OptimisationDefaults defaults = new OptimisationDefaults(annealing, constraint);
			
			result.plan = decodeOptimisationPlan(mapper, root.get("plan"), defaults);	
			
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
	private static OptimisationPlan decodeOptimisationPlan(ObjectMapper mapper, JsonNode planNode, OptimisationDefaults defaults) throws JsonProcessingException {
		
		UserSettings us = mapper.treeToValue(planNode.get("userSettings"), UserSettingsImpl.class);
		
        OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();
        
        plan.setUserSettings(us);
        plan.setResultName(planNode.get("resultName").asText());
        
        ArrayNode stages = (ArrayNode) planNode.get("stages");
        
        // decode the optimisation stages using special logic based on their name, adding default values
        for (JsonNode stage: stages) {
        	 plan.getStages().add(decodeOptimisationStage(mapper, stage, defaults));
        }
		
        plan.setSolutionBuilderSettings(decodeSolutionBuilderSettings(mapper, planNode, defaults));
        
		return plan;
		
	}
	
	private static SolutionBuilderSettings decodeSolutionBuilderSettings(ObjectMapper mapper, JsonNode node, OptimisationDefaults defaults) throws JsonProcessingException {
        // add default settings to the solutionBuilderSettings
        SolutionBuilderSettings solutionBuilderSettings;
        
        if (node.has("solutionBuilderSettings")) {
        	solutionBuilderSettings = mapper.treeToValue(node.get("solutionBuilderSettings"), SolutionBuilderSettings.class);
        }
        else {
        	solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();        	
        }	        
        
        inheritFromDefaultIfNotSpecified(solutionBuilderSettings, defaults.constraintAndFitnessSettings, "constraintAndFitnessSettings", node.get("solutionBuilderSettings"));
        
        ConstraintAndFitnessSettings constraintAndFitnessSettings = solutionBuilderSettings.getConstraintAndFitnessSettings();
        if (constraintAndFitnessSettings != null) {
			// import hack: set imported constraints and objectives to "enabled"
	        setConstraintsAndObjectivesEnabled(constraintAndFitnessSettings);
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
	private static OptimisationStage decodeOptimisationStage(ObjectMapper mapper, JsonNode node, OptimisationDefaults defaults) {
		String name = node.get("name").asText();
		OptimisationStage result;
		
		
		if (name.equals("parallel")) {
			final ParallelOptimisationStage pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate((ParallisableOptimisationStage) decodeOptimisationStage(mapper, node.get("template"), defaults));
			pStage.setJobCount( (Integer) ((IntNode) node.get("jobCount")).numberValue() ); 
			result = pStage; 
		}
		else {

			Class<? extends OptimisationStage> clazz = null;
			
			if (name.equals("lso")) { clazz = LocalSearchOptimisationStage.class; }
			else if (name.equals("hill")) { clazz = HillClimbOptimisationStage.class; }
			
			if (clazz != null) {
				try {
					result = mapper.treeToValue(node, clazz);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
					return null;
				}
				
			}
			else {
				return null;
			}
		}		
		
		if (result instanceof LocalSearchOptimisationStage || result instanceof HillClimbOptimisationStage) {
			// import hack: use default values for annealing settings
			inheritFromDefaultIfNotSpecified(result, defaults.annealing, "annealingSettings", node);
		}
		
		if (result instanceof ConstraintsAndFitnessSettingsStage) {
			// import hack: use default values for constraint & fitness settings
			inheritFromDefaultIfNotSpecified(result, defaults.constraintAndFitnessSettings, "constraintAndFitnessSettings", node);
			// import hack: set imported constraints and objectives to "enabled"
			setConstraintsAndObjectivesEnabled(((ConstraintsAndFitnessSettingsStage) result).getConstraintAndFitnessSettings());
		}


		return result;
	}
	
	/**
	 * Performs an ugly workaround hack to allow annealing settings (and constraint & fitness settings)
	 * to be set from an overridable default during JSON import. Behaviour is not clean or necessarily scalable to
	 * other object properties, and will produce unpredictable results if the imported object is deeply nested. 
	 * 
	 * @param eObj An EObject whose feature is meant to be set from the default value
	 * @param eDefault An EObject representing the default value
	 * @param featureName The name of the feature
	 * @param node The node representing the eObj object in the Jackson parse tree.
	 */
	private static void inheritFromDefaultIfNotSpecified(EObject eObj, EObject eDefault, String featureName, JsonNode node) {
		if (eDefault == null) {
			return;
		}
		
		EStructuralFeature feature = eObj.eClass().getEStructuralFeature(featureName);
		
		EObject eValue = (EObject) eObj.eGet(feature);
		EObject newEValue = EcoreUtil.copy(eDefault);
		
		eObj.eSet(feature, newEValue);
		
		if (eValue != null && node != null)  {
			EClass eClass = eValue.eClass();

			EList<EAttribute> attrs = eClass.getEAllAttributes();
			
			JsonNode subNode = node.get(featureName);
			
			if (subNode != null) {
				for (EStructuralFeature subfeature: eClass.getEAllStructuralFeatures()) {
					if (subNode.has(subfeature.getName())) {
						String name = subfeature.getName();
						newEValue.eSet(subfeature, eValue.eGet(subfeature));
					}
		 		}
			}
		}

	}
	
	/**
	 * Sets the constraints and objectives in a ConstraintAndFitnessSettings object to "enabled". This is a hack for plans newly imported from JSON.
	 *  
	 * TODO: make this a feature of a custom Jackson deserialiser.
	 * 
	 * @param plan
	 */
	private static void setConstraintsAndObjectivesEnabled(ConstraintAndFitnessSettings settings) {
		if (settings == null) {
			return;
		}
		
		for (Constraint constraint: settings.getConstraints()) {
			constraint.setEnabled(true);
		}
		for (Objective objective: settings.getObjectives()) {
			objective.setEnabled(true);
		}
	}
	
}