/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

/**
 * This class represents configuration options for the optimiser that are deserialised from a JSON config file. The options are divided into:
 * <ol>
 * <li>an {@link OptimisationPlan} object</li>
 * <li>some data that is provided to the optimiser by the dependency injection</li>
 * <li>an {@link LSOLogger.LoggingParameters} object</li>
 * <li>everything else</li>
 * </ol>
 * 
 * Apart from the {@link OptimisationPlan} and the {@link LSOLogger.LoggingParameters}, the data is currently stored in an ad-hoc manner as a JsonNode from the Jackson parse tree.
 * 
 * @author simonmcgregor
 *
 */
public class OptimiserConfigurationOptions {
	@SuppressWarnings("serial")
	public static class CannotDeserialiseException extends RuntimeException {

		public CannotDeserialiseException(@NonNull final String message) {
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

		public OptimisationDefaults(final AnnealingSettings annealing, final ConstraintAndFitnessSettings constraint) {
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
	 * 
	 * @param n
	 */
	public void overrideNumThreads(final int n) {
		rawNumThreads = n;
	}

	public static OptimiserConfigurationOptions readFromRawJSON(final String inputJSONX, final Map<String, String> customInfo) throws IOException {

		final HashMap<String, Object> inputParameters = new HashMap<>();
		// populate input with "custom-" literals
		customInfo.forEach((k, v) -> inputParameters.put("custom-" + k, v));

		final JSONObject obj = new JSONObject(inputJSONX);

		final DefaultingJsonEngine engine = new DefaultingJsonEngine(".").withLiteralValues(inputParameters);

		final String inputPlainJSON = engine.newDefaultingJsonStructure(obj).outputString();

		return readFromPlainJSONString(inputPlainJSON);

	}

	public static OptimiserConfigurationOptions readFromFile(final String jsonFilename, final Map<String, String> customInfo) {
		try {
			final String inputJSONX = new String(Files.readAllBytes(Paths.get(jsonFilename)));

			final HashMap<String, Object> inputParameters = new HashMap<>();
			// populate input with "custom-" literals
			customInfo.forEach((k, v) -> inputParameters.put("custom-" + k, v));

			final JSONObject obj = new JSONObject(inputJSONX);

			final DefaultingJsonEngine engine = new DefaultingJsonEngine(".").withLiteralValues(inputParameters);

			final String inputPlainJSON = engine.newDefaultingJsonStructure(obj).outputString();

			return readFromPlainJSONString(inputPlainJSON);
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("null")
	public static OptimiserConfigurationOptions readFromPlainJSONString(final String plainInputJSON) throws IOException {
		final OptimiserConfigurationOptions result = new OptimiserConfigurationOptions();

		// Ensure package is loaded
		final Object literals = ParametersPackage.eINSTANCE;

		final ObjectMapper mapper = OptimisationJSONDeserialiser.getMapper();

		final ObjectNode root = mapper.readValue(plainInputJSON, ObjectNode.class);

		result.plan = OptimisationJSONDeserialiser.decodeOptimisationPlan(mapper, root.get("plan"));

		result.injections = mapper.treeToValue(root.get("injections"), JsonNode.class);

		result.other = mapper.treeToValue(root.get("other"), JsonNode.class);

		return result;
	}

	/**
	 * Describes whether or not the specified options require any injections into the injection framework.
	 * 
	 * @return
	 */
	public static boolean requiresInjections(final OptimiserConfigurationOptions options) {
		return options != null && options.injections != null;
	}

}
