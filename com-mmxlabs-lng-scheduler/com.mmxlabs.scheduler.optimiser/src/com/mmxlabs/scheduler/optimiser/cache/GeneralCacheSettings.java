package com.mmxlabs.scheduler.optimiser.cache;

/**
 * Small params class for general cache settings.
 * 
 * @author Simon Goodall
 *
 */
public final class GeneralCacheSettings {

	private GeneralCacheSettings() {
		// Private constructor to keep Sonar happy.
	}

	/**
	 * Enable or disable random verification. See {@link #VERIFICATION_CHANCE} for verification frequency. This is different to {@link CacheMode#Verify} which verifies *every* cache call.
	 * 
	 */
	public static final boolean ENABLE_RANDOM_VERIFICATION = false;

	// % chance of performing a cache verification. Lower is better for runtime.
	public static final double VERIFICATION_CHANCE = 0.001;

	// Time Window Scheduler see TimeWindowScheduler
	public static final int TimeWindowScheduler_Default_CacheSize = 100_000;
	public static final boolean TimeWindowScheduler_RecordStats = false;

	// PNL based time window trimmer - PNLBasedWindowTrimmer
	public static final int PNLWindowTrimmer_Default_CacheSize = 10_000_000; // 10m in master
	public static final boolean PNLWindowTrimmer_RecordStats = false;

	// VoyagePlanEvaluator - see CachingVoyagePlanEvaluator
	public static final int VoyagePlanEvaluator_Default_CacheSize = 5_000_000; // 5m in master
	public static final boolean VoyagePlanEvaluator_RecordStats = false;
	
	
}
