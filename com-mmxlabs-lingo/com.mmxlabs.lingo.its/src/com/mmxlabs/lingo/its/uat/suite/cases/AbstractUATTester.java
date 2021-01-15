/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.cases;

import java.io.IOException;

import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.uat.suite.utils.FeatureBasedUAT;

public abstract class AbstractUATTester {
	protected UATCase[] cases;
	protected FeatureBasedUAT uatFeatureExtractor;

	protected void createContractPropertiesFiles() throws IOException, Exception {
		if (TestingModes.UATCasestMode == TestMode.Generate) {
			for (UATCase testCase : getCases()) {
				getUATFeatureExtractor().createPropertiesForCase(testCase.lingoFilePath, testCase.cargoName);
			}
		}
	}

	public void testAllNLNGContractProperties() throws IOException, Exception {
		for (UATCase testCase : getCases()) {
			getUATFeatureExtractor().checkPropertiesForCase(testCase.lingoFilePath, testCase.cargoName, false);
		}
	}

	protected void singleUATTestCase(String caseName) throws Exception {
		UATCase c = findCase(caseName);
		singleUATTestCase(c, false);
	}

	protected void singleUATTestCase(String caseName, boolean additionalChecks) throws Exception {
		UATCase c = findCase(caseName);
		singleUATTestCase(c, additionalChecks);
	}

	private void singleUATTestCase(UATCase testCase, boolean additionalChecks) throws Exception {
		getUATFeatureExtractor().checkPropertiesForCase(testCase.lingoFilePath, testCase.cargoName, additionalChecks);
	}

	protected UATCase findCase(String caseName) {
		UATCase testCase = null;
		for (UATCase c : getCases()) {
			if (c.lingoFilePath == caseName) {
				testCase = c;
				break;
			}
		}
		assert (testCase != null);
		return testCase;
	}

	protected UATCase[] getCases() {
		return cases;
	}

	protected FeatureBasedUAT getUATFeatureExtractor() {
		return uatFeatureExtractor;
	}

	protected void setUATFeatureExtractor(FeatureBasedUAT fbUAT) {
		this.uatFeatureExtractor = fbUAT;
	}

}
