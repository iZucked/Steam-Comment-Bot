/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions.ScenarioFileFormat;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptioniserApplication extends HeadlessGenericApplication {

	protected void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outputFile, int threads) throws Exception  {
		HeadlessOptioniserRunner.Options options = getAlgorithmOptionsFromJSON(hOptions.algorithmConfigFile, HeadlessOptioniserRunner.Options.class);		
		
		HeadlessOptioniserJSON json = (new HeadlessOptioniserJSONTransformer()).createJSONResultObject(getDefaultMachineInfo(), (HeadlessOptioniserRunner.Options) options, scenarioFile, threads);
		writeMetaFields(json, scenarioFile, hOptions, threads);

		SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();
		
		HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
		
		int iterations = options.iterations;
		int startTry = (run - 1) * iterations; //every run should start at a different point.
		
		ScenarioFileFormat type = hOptions.getExpectedScenarioFormat();
		
		switch(type) {
			case LINGO_FILE: {
				runner.run(startTry, scenarioFile, logger, options, null);
				break;			
			}
			case CSV_FOLDER: {
				runner.runFromCSVDirectory(startTry, scenarioFile, logger, options, null);
				break;
			}
			default: {
				throw new UnhandledScenarioTypeException(String.format("No method for handling scenario file %s", scenarioFile.getName()));
			}
		}
						
		(new HeadlessOptioniserJSONTransformer()).addRunResult(startTry, logger, json);

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, json);
		} catch (Exception e) {
			System.err.println("Error writing to file:");
			e.printStackTrace();
		}
		
	}

	@Override
	protected String getAlgorithmName() {
		// TODO: use this in the created log
		return "optioniser";
	}

}
