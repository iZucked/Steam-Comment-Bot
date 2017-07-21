/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;

public class SimilarityUIParametersTests {
	@Test
	public void testLow3Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2015-12");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.LOW, start, end);
		Assert.assertEquals(16, settings.getLowInterval().getThreshold());
		Assert.assertEquals(0, settings.getLowInterval().getWeight());
		Assert.assertEquals(32, settings.getMedInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(64, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testMed3Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2015-12");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.MEDIUM, start, end);
		Assert.assertEquals(8, settings.getLowInterval().getThreshold());
		Assert.assertEquals(0, settings.getLowInterval().getWeight());
		Assert.assertEquals(16, settings.getMedInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(32, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testHigh3Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2015-12");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.HIGH, start, end);
		Assert.assertEquals(8, settings.getLowInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getLowInterval().getWeight());
		Assert.assertEquals(16, settings.getMedInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(32, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testLow6Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2016-03");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.LOW, start, end);
		Assert.assertEquals(32, settings.getLowInterval().getThreshold());
		Assert.assertEquals(0, settings.getLowInterval().getWeight());
		Assert.assertEquals(64, settings.getMedInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(128, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testMed6Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2016-03");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.MEDIUM, start, end);
		Assert.assertEquals(16, settings.getLowInterval().getThreshold());
		Assert.assertEquals(0, settings.getLowInterval().getWeight());
		Assert.assertEquals(32, settings.getMedInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(64, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testHigh6Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2016-03");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.HIGH, start, end);
		Assert.assertEquals(16, settings.getLowInterval().getThreshold());
		Assert.assertEquals(250_000, settings.getLowInterval().getWeight());
		Assert.assertEquals(32, settings.getMedInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getMedInterval().getWeight());
		Assert.assertEquals(64, settings.getHighInterval().getThreshold());
		Assert.assertEquals(500_000, settings.getHighInterval().getWeight());
		Assert.assertEquals(5_000_000, settings.getOutOfBoundsWeight());
	}

	@Test
	public void testOff6Month() {
		final LocalDate start = LocalDate.parse("2015-09-01");
		final YearMonth end = YearMonth.parse("2016-03");
		final SimilaritySettings settings = SimilarityUIParameters.getSimilaritySettings(SimilarityMode.OFF, start, end);
		Assert.assertEquals(8, settings.getLowInterval().getThreshold());
		Assert.assertEquals(0, settings.getLowInterval().getWeight());
		Assert.assertEquals(16, settings.getMedInterval().getThreshold());
		Assert.assertEquals(0, settings.getMedInterval().getWeight());
		Assert.assertEquals(32, settings.getHighInterval().getThreshold());
		Assert.assertEquals(0, settings.getHighInterval().getWeight());
		Assert.assertEquals(0, settings.getOutOfBoundsWeight());
	}

}
