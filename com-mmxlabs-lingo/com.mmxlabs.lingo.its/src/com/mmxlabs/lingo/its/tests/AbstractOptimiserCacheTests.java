/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.custom.CTabFolder;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;

/**
 * Test class base for running cache verification tests.
 * 
 * @author Simon Goodall
 *
 */
public abstract class AbstractOptimiserCacheTests extends AbstractAdvancedOptimisationTester {

	private enum CacheType {
		TimeWindow("TimeWindow"), //
		VPO("VPO"), //
		Volume("VolumeAllocator"), //
		VolumeSequences("VolumeAllocatedSequence"), //
		PNL("PNL") //
		;
		private final String lbl;

		private CacheType(final String lbl) {
			this.lbl = lbl;
		}

		@Override
		public String toString() {
			return lbl;
		}
	}

	@Override
	public void init(@NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd) {
		init(scenarioURL, periodStart, periodEnd, true);
		// Do not bother with properties file checks. Other tests should cover repeatability.
		doPropertiesChecks = false;
	}

	public List<DynamicNode> makeTests(@NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd) {
		final List<DynamicNode> tests = new LinkedList<>();

		for (final boolean withGCO : BOOLS) {
			final String gcoLabel = withGCO ? "GCO_" : "";
			for (final CacheType cacheType : CacheType.values()) {
				final String label = String.format("Limited_%s_%s_Verify", gcoLabel, cacheType);
				tests.add(DynamicTest.dynamicTest(label, () -> {
					Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);
					init(scenarioURL, periodStart, periodEnd);
					runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, withGCO, withCacheSettings(//
							cacheType == CacheType.VPO ? CacheMode.Verify : CacheMode.Off, //
							cacheType == CacheType.TimeWindow ? CacheMode.Verify : CacheMode.Off, //
							cacheType == CacheType.Volume ? CacheMode.Verify : CacheMode.Off, //
							cacheType == CacheType.PNL ? CacheMode.Verify : CacheMode.Off, //
							cacheType == CacheType.VolumeSequences ? CacheMode.Verify : CacheMode.Off //
					), LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
				}));
			}
		}

		return tests;
	}
}
