/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.testers;

import com.mmxlabs.lingo.its.uat.suite.cases.UATMultiCargoCase;
import com.mmxlabs.lingo.its.uat.suite.cases.UATTypedCase;
import com.mmxlabs.lingo.its.uat.suite.utils.FeatureBasedUAT;
import com.mmxlabs.models.lng.schedule.Schedule;

public class MultiCargoTester {
	
	public static void writeAndCheckProperties(UATMultiCargoCase multiCargoCase, boolean write) throws Exception {
		UATTypedCase[] typedCases = multiCargoCase.cases;
		String lingoFilePath = multiCargoCase.lingoFilePath;
		FeatureBasedUAT featureExtractor = typedCases[0].featureBasedUAT;
		Schedule s = featureExtractor.getSchedule(lingoFilePath);

		if (write && GlobalUATTestsConfig.WRITE_PROPERTIES) {
			createMultiCargoPropertiesFiles(s, typedCases);
		}
		
		checkMultiCargoPropertiesFiles(s, typedCases);
	}
	
	public static void writeAndCheckProperties(UATMultiCargoCase multiCargoCase, boolean write, Schedule s) throws Exception {
		UATTypedCase[] typedCases = multiCargoCase.cases;
		String lingoFilePath = multiCargoCase.lingoFilePath;
		if (write && GlobalUATTestsConfig.WRITE_PROPERTIES) {
			createMultiCargoPropertiesFiles(s, typedCases);
		}
		
		checkMultiCargoPropertiesFiles(s, typedCases);
	}

	
	public static void createMultiCargoPropertiesFiles(Schedule schedule, UATTypedCase[] typedCases) throws Exception  {
			for (UATTypedCase uatCase : typedCases) {
				uatCase.featureBasedUAT.createPropertiesForTypedCase(schedule, uatCase.lingoFilePath, uatCase.cargoName);
			}
	}
	
	public static void checkMultiCargoPropertiesFiles(Schedule schedule, UATTypedCase[] typedCases) throws Exception {
			for (UATTypedCase uatCase : typedCases) {
				uatCase.featureBasedUAT.checkPropertiesForTypedCase(schedule, uatCase.lingoFilePath, uatCase.cargoName, false);
			}
	}		
}
