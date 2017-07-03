/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;

/**
 * Test class base for running cache verification tests.
 * @author Simon Goodall
 *
 */
public abstract class AbstractOptimiserCacheTests extends AbstractAdvancedOptimisationTester {

	public AbstractOptimiserCacheTests(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable YearMonth periodStart, @Nullable YearMonth periodEnd) {
		super(_unused_method_prefix_, scenarioURL, periodStart, periodEnd, true);

	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_VPO_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Verify, CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_TimeWindow_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Verify, CacheMode.Off, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_VolumeAllocator_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Verify, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_PNL_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Verify, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_VolumeAllocatedSequence_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Verify),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_GCO_VPO_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Verify, CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_GCO_TimeWindow_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Verify, CacheMode.Off, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_GCO_VolumeAllocator_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Verify, CacheMode.Off, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_GCO_PNL_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Verify, CacheMode.Off),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_GCO_VolumeAllocatedSequence_Verify() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false, withCacheSettings(CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Off, CacheMode.Verify),
				LNGTransformerHelper.HINT_TESTING_IGNORE_CACHE_SETTINGS);
	}
}
