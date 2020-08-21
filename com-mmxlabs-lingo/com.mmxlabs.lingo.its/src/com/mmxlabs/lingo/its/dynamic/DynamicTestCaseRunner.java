/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper.ReportRecord;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

public abstract class DynamicTestCaseRunner {

	protected abstract File getBaseDirectory();

	protected File getBaseDirectory(final @NonNull String clientCode) {

		// Check for explicit folder from properties (i.e. on build server)
		final String testRoot = System.getProperty("lingo.dynamictests");
		if (testRoot != null) {
			return new File(testRoot);
		} else {
			// Or derive from current folder (i.e. running in eclipse)
			final String userDir = System.getProperty("user.dir");
			// Expect userDir to be something like
			// c:\ws\lingo-1\ws\client-v.git\com.mmxlabs.lingo.v.its
			// and we can to redirect to
			// c:\ws\dynamictests\v
			// This should also work with c:\dev\ws rather than c:\ws
			return new File(userDir + "/../../../../dynamictests/" + clientCode);
		}
	}

	@TestFactory
	@Tag(TestCategories.OPTIMISATION_TEST)
	public List<DynamicNode> runOptimisationTests() {
		return OptimisationTestRunner.runOptimisationTests(getBaseDirectory());
	}

	@Disabled
	@TestFactory
	@Tag(TestCategories.OPTIMISATION_TEST)
	public List<DynamicNode> runOptioniserTests() {
		return OptioniserTestRunner.runOptioniserTests(getBaseDirectory());
	}

	@TestFactory
	@Tag(TestCategories.REPORT_TEST)
	public List<DynamicNode> runReportTests() throws JsonParseException, JsonMappingException, IOException {
		return ReportTestRunner.runReportTests(getBaseDirectory(), getExtraReports());
	}

	@TestFactory
	@Tag(TestCategories.REPORT_TEST)
	public List<DynamicNode> runReportWithElementTests() throws IOException {
		return ReportTestRunner.runReportWithElementTests(getBaseDirectory());
	}

	/**
	 * Overridable method to allow custom reports to be hooked in.
	 */
	protected List<ReportRecord> getExtraReports() {
		return Collections.emptyList();
	}
}