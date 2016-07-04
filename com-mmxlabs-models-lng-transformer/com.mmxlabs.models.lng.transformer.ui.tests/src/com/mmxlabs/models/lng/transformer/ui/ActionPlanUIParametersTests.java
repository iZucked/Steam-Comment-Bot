/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import com.mmxlabs.models.lng.parameters.SimilarityMode;

public class ActionPlanUIParametersTests {
	@Test
	public void testLow3Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2015-12");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.LOW, start, end);
		Assert.assertEquals(30_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(2_000_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
	
	@Test
	public void testMed3Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2015-12");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.MEDIUM, start, end);
		Assert.assertEquals(5_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(1_500_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
	
	@Test
	public void testHigh3Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2015-12");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.HIGH, start, end);
		Assert.assertEquals(3_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(1_000_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
	
	@Test
	public void testLow6Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2016-03");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.LOW, start, end);
		Assert.assertEquals(60_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(2_000_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
	
	@Test
	public void testMed6Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2016-03");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.MEDIUM, start, end);
		Assert.assertEquals(30_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(2_000_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
	
	@Test
	public void testHigh6Month() {
		YearMonth start = YearMonth.parse("2015-09");
		YearMonth end = YearMonth.parse("2016-03");
		ActionPlanSettings settings = ActionPlanUIParameters.getActionPlanSettings(SimilarityMode.HIGH, start, end);
		Assert.assertEquals(6_000_000, settings.getTotalEvaluations());
		Assert.assertEquals(2_000_000, settings.getInRunEvaluations());
		Assert.assertEquals(5_000, settings.getSearchDepth());
	}
}
