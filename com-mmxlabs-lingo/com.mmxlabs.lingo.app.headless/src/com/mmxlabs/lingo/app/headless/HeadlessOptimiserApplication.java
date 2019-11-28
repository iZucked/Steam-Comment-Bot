/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions.ScenarioFileFormat;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptimiserApplication extends HeadlessGenericApplication {
	
	@SuppressWarnings("serial")
	public class CustomTypeResolverBuilder extends DefaultTypeResolverBuilder
	{
	    public CustomTypeResolverBuilder()
	    {
	        super(DefaultTyping.NON_FINAL);
	    }

	    @Override
	    public boolean useForType(JavaType t)
	    {
	    	Class<?> clazz = t.getRawClass();
	    	// only provide class information for OptimisationStage objects
	    	// don't add class information to plain Object instances that are somehow assignable from OptimisationStage
	    	if (clazz.equals(Object.class) == false && clazz.isAssignableFrom(OptimisationStage.class)) {
	            return true;
	        }

	        return false;
	    }
	}
	
	/**
	 * Runs the optimiser and writes the output log results.
	 * 
	 * The headless optimiser shares a lot of logic with the headless optioniser, but the code paths differ significantly,
	 * particularly the code that actually writes data to the output object.
	 * 
	 * Hence, this method does some things that ought to be handled in HeadlessGenericApplication. Future 
	 * implementations should probably refactor this method and clean up the separation of concerns.
	 */
	protected void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outputFile, int threads) throws Exception {
		HeadlessOptimiserJSON json = (new HeadlessOptimiserJSONTransformer()).createJSONResultObject(getDefaultMachineInfo(), scenarioFile, threads);
		json.getMeta().setClient(clientCode);
		json.getMeta().setVersion(buildVersion);
		json.getMeta().setMachineType(machineInfo);
		json.getMeta().setCustomInfo(hOptions.customInfo);
		json.getMeta().setMaxHeapSize(Runtime.getRuntime().maxMemory());
		json.getParams().setCores(threads);

		HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
		
		ScenarioFileFormat type = hOptions.getExpectedScenarioFormat();
		
		switch(type) {
			case LINGO_FILE: {
				runner.run(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
				break;			
			}
			case CSV_FOLDER: {
				runner.runFromCsvDirectory(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
				break;
			}
			case CSV_ZIP: {
				runner.runFromCsvZipFile(scenarioFile, hOptions, new NullProgressMonitor(), null, json);
				break;
			}
			default: {
				throw new UnhandledScenarioTypeException(String.format("No method for handling scenario file %s", scenarioFile.getName()));
			}
		}
		
		renameInvalidBsonFields(json.getMetrics().getStages());

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
			resolver.init(JsonTypeInfo.Id.CLASS, null);
			resolver.inclusion(JsonTypeInfo.As.PROPERTY);
			resolver.typeProperty("@class");			
			mapper.setDefaultTyping(resolver);
			
			
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, json);
		} catch (Exception e) {
			System.err.println("Error writing to file:");
			e.printStackTrace();
		}

	}

	/**
	 * Checks if a field name (in the JSON output structure) is actually a valid BSON fieldname.
	 * @param name
	 * @return
	 */
	private boolean isValidBsonFieldname(String name) {
		return name.matches("^[_0-9a-zA-Z\\-]+$");
	}

	/**
	 * Converts a field name into a valid BSON fieldname, if necessary.
	 * @param name
	 * @return
	 */
	private String makeValidBsonFieldname(String name) {
		// force only alphanumeric characters, underscore & hyphen
		String result = name.replaceAll("[^_a-zA-Z0-9\\-]", "_");

		// don't allow an empty string
		if (result.equals("")) {
			result = "_";
		}

		return result;

	}

	/**
	 * Renames fields that have BSON-invalid names (e.g. containing whitespace) 
	 * @param object A JSONObject
	 */
	private void renameInvalidBsonFields(JSONObject object) {
		if (object == null) {
			return;
		}
		
		Set keys = object.keySet();

		List<Object> badNames = new LinkedList<>();

		for (Object key : keys) {
			String name = key.toString();
			Object value = object.get(key);

			if (!isValidBsonFieldname(name)) {
				badNames.add(key);
			}

			if (value instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) value);
			} else if (value instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) value);
			}
		}

		for (Object key : badNames) {
			String name = key.toString();
			String newName = makeValidBsonFieldname(name);
			if (object.containsKey(newName)) {
				throw new RuntimeException("Tried to ".format("Tried to rename invalid BSON name '%s' to '%s' but encountered a name collision.", name, newName));
			} else {
				object.put(newName, object.get(key));
				object.remove(key);
			}

		}

	}

	/**
	 * Renames fields that have BSON-invalid names (e.g. containing whitespace) 
	 * @param object A JSONArray of objects
	 */
	private void renameInvalidBsonFields(JSONArray object) {
		for (Object child : object) {
			if (child instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) child);
			} else if (child instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) child);
			}
		}

	}

	@Override
	protected String getAlgorithmName() {
		// TODO: use this in the created log
		return "optimisation";
	}

}
