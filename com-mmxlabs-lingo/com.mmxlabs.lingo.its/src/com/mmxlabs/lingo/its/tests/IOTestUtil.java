/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard;
import com.mmxlabs.models.lng.scenario.exportWizards.ExportCSVWizard.exportInformation;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class IOTestUtil {
	
	
	public static String[] ListToArray(EList<CargoAllocation> originalCargoAllocations){
			
			int size = originalCargoAllocations.size();
			String[] array = new String[size];
			
			int i = 0;
			for (CargoAllocation temp :  originalCargoAllocations) {
				array[i] = (temp.getName());
				i += 1; 
			}
			return array;
			
		}
	
	
	public static ArrayList makeSlotAllocationArray(EList<CargoAllocation> originalCargoAllocations){
		
		ArrayList output = new ArrayList();
		
		for(CargoAllocation ca: originalCargoAllocations){
			EList<SlotAllocation> allocations = ca.getSlotAllocations();
			
			for (SlotAllocation sa : allocations){
				output.add(sa.getPort().getLocation());
				//output.add(sa.getSlot().getCargo().getLoadName());
			}
		}
		return output;
	}
	
	/**
	 * Converts EList<Fitness> into long[] without headers for easy comparison.
	 * @param fitnesses - Input EList of fitness values
	 * @return array - Output array of fitness longs
	 */
	public static long[] fitnessListToArrayValues(EList<Fitness> fitnesses){
		
		int size = fitnesses.size();
		long[] array = new long[size];
		
		int i = 0;
		for (Fitness temp : fitnesses) {
			array[i] = (temp.getFitnessValue());
			i += 1; 
		}
		return array;
		
	}
	/**
	 *  Deletes the temporary directory and all its contents
	 */
	public static void tempDirectoryTeardown(){
		
		File tempDir = new File(System.getProperty("java.io.tmpdir", null), "tempdir-old");
		
		String[]entries =	tempDir.list();
		for(String s: entries){
		    File currentFile = new File(tempDir.getPath(),s);
		    currentFile.delete();
		}
		
		tempDir.delete();
	}
	
	
	
	/**
	 *  Takes the URL of a scenario and returns the associated LNGScenarioModel
	 * @param url - URL of the scenario files
	 * @return - LNGScenarioModel object created from the target files
	 * @throws MalformedURLException
	 */
	public static LNGScenarioModel URLtoScenarioCSV(URL url) throws MalformedURLException{
		
		CSVTestDataProvider csv_tdp = new CSVTestDataProvider(url);
	
		LNGScenarioModel testCase = csv_tdp.getScenarioModel();

		return testCase;
		
	}
	
	
	
	public static LNGScenarioModel URLtoScenarioLiNGO(URL url) throws Exception{
		
		AbstractOptimisationResultTester AORT = new AbstractOptimisationResultTester();
			
			final ScenarioInstance instance = AORT.loadScenario(url);
		
			LNGScenarioModel testCase =  (LNGScenarioModel) instance.getInstance();
	
			return testCase;
			
		}
	
	
	/**
	 *  Produces an EList of the fitnesses from the passed LNGScenerioModel
	 * @param model - LNGScenarioModel to acquire fitnesses of
	 * @return - EList<Fitness> of the associated fitnesses.
	 */
	public static EList<Fitness> ScenarioModeltoFitnessList(LNGScenarioModel model){
		
		final OptimiserSettings settings = LNGScenarioRunnerCreator.createExtendedSettings(ScenarioUtils.createDefaultSettings());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		
		LNGScenarioRunner runner = LNGScenarioRunnerCreator.createScenarioRunnerWithLSO(executorService, model, settings);

		final Schedule schedule = runner.getSchedule();
		
		final EList<Fitness> fitnesses = schedule.getFitnesses();
		
		return fitnesses;
	}
	
public static Schedule ScenarioModeltoSchedule(LNGScenarioModel model){
		
		final OptimiserSettings settings = LNGScenarioRunnerCreator.createExtendedSettings(ScenarioUtils.createDefaultSettings());
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		
		LNGScenarioRunner runner = LNGScenarioRunnerCreator.createScenarioRunnerWithLSO(executorService, model, settings);

		final Schedule schedule = runner.getSchedule();
		
		return schedule;
	}
	
	/**
	 *  Exports the passed LNGScenarioModel to a temporary directory 
	 * @param model - the LNGScenarioModel to be written to the temporary directory
	 * @return - URL by which to locate the temporary directory
	 * @throws MalformedURLException
	 */
	public static URL exportTestCase(LNGScenarioModel model) throws MalformedURLException{
		
		ExportCSVWizard ECSVW = new ExportCSVWizard();
		
		exportInformation info = ECSVW.new exportInformation();
		
		File tempDir = new File(System.getProperty("java.io.tmpdir", null), "tempdir-old");
		
		info.outputDirectory = tempDir;
		info.delimiter = ',';
		info.decimalSeparator = '.';
	
		ECSVW.exportScenario(model, info,true,"holder" );
		
		File restoreDir = new File(System.getProperty("java.io.tmpdir", null), "tempdir-old/holder");

		URL restoreURL = restoreDir.toURI().toURL();
		
		return restoreURL;
	}

}
