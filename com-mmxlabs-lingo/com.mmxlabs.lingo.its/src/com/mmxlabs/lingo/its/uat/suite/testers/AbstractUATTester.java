/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.testers;

import java.io.IOException;

import com.mmxlabs.lingo.its.uat.suite.cases.UATCase;
import com.mmxlabs.lingo.its.uat.suite.utils.FeatureBasedUAT;

public abstract class AbstractUATTester {
	protected UATCase[] cases;
	protected FeatureBasedUAT uatFeatureExtractor;

	protected boolean write;

	protected void createContractPropertiesFiles() throws IOException, Exception {
		if (getWrite() && GlobalUATTestsConfig.WRITE_PROPERTIES) {
			for (UATCase testCase : getCases()) {
				getUATFeatureExtractor().createPropertiesForCase(testCase.lingoFilePath, testCase.cargoName);
			}
		}
	}

	protected void createSingleContractPropertiesFiles(UATCase testCase) throws IOException, Exception {
		if (getWrite() && GlobalUATTestsConfig.WRITE_PROPERTIES) {
			getUATFeatureExtractor().createPropertiesForCase(testCase.lingoFilePath, testCase.cargoName);
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

	protected void singleUATTestCase(UATCase testCase, boolean additionalChecks) throws Exception {
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

	protected boolean getWrite() {
		return write;
	}

	protected void setUATFeatureExtractor(FeatureBasedUAT fbUAT) {
		this.uatFeatureExtractor = fbUAT;
	}

}
