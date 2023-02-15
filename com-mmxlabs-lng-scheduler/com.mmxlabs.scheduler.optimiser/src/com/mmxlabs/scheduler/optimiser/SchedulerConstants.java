/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Various constants, such as keys, used within the scheduler.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class SchedulerConstants {

	public static final String SCENARIO_TYPE_ADP = "scenario-type-adp";
	public static final String SCENARIO_TYPE_LONG_TERM = "scenario-type-long-term";

	
	/**
	 * The expected number of threads used by the thread pools.
	 */
	public static final String CONCURRENCY_LEVEL = "concurrency-level";

	// Series parser injector names
	public static final String Parser_Commodity = "Commodity";
	public static final String Parser_BaseFuel = "BaseFuel";
	public static final String Parser_Charter = "Charter";
	public static final String Parser_Currency = "Currency";
	public static final String Parser_PricingBasis = "PricingBasis";

	public static final String MIDNIGHT_ALIGNED_INTEGER_INTERVAL_CURVE = "MidnightAlignedIntegerCurve";

	
	// Caching constants - used in injection framework
	public static final String Key_VoyagePlanEvaluatorCache = "cache-voyage-plan-evaluator";
	public static final String Key_TimeWindowSchedulerCache = "cache-arrival-times";
	public static final String Key_PNLBasedWindowTrimmerCache = "cache-pnl-trimmer-times";

	// Additional Info keys
	public static final String AI_volumeAllocationInfo = "info-volume-allocation";
	public static final String AI_cargoValueAllocationInfo = "info-cargo-value-allocation";
	public static final String AI_generatedCharterOutInfo = "info-generated-charter-out";
	public static final String AI_capacityViolationInfo = "info-capacity-violation";
	public static final String AI_hedgingValue = "element-hedging-value";
	public static final String AI_miscCostsValue = "element-misc-costs-value";
	public static final String AI_cancellationFees = "element-cancellation-fees";
	public static final String AI_charterOutProfitAndLoss = "generated-charter-out-profit-and-loss";
	public static final String AI_profitAndLoss = "element-profit-and-loss";
	public static final String AI_profitAndLossSlotDetails = "element-profit-and-loss-slot-details";
	public static final String AI_similarityDifferences = "info-similarity-changes";

	/**
	 * A key for a per-route fitness map, which should be of type {@code Map<IResource, Map<String, Long>}. Each element in the map should map fitness function names to fitness values.
	 */
	public static final String G_AI_scheduledSequence = "general-info-scheduleSequence";

	public static final String Key_UsePriceBasedWindowTrimming = "scheduler-use-price-based-window-trimming";
	public static final String Key_UsePNLBasedWindowTrimming = "scheduler-use-pnl-based-window-trimming";
	public static final String Key_UseCanalSlotBasedWindowTrimming = "scheduler-use-canal-slot-based-window-trimming";
	
	public static final String Key_UseHeelRetention = "scheduler-use-heel-retention";
	/**
	 * A key to use the best Panama canal Idle days
	 */
	public static final String Key_UseBestPanamaCanalIdleDaysWindowTrimming = "scheduler-use-best-panama-canal-idle-days-window-trimming";

	public static final String Key_SchedulePurges = "schedule-purges";

	/**
	 * When there is not other information available, use this capacity limit.
	 */
	public static final String KEY_DEFAULT_MAX_VOLUME_IN_M3 = "default-max-volume-in-m3";

	/**
	 * Constant linked to an integer hours of idle time before converting ballast idle to charter length
	 */
	public static final String CHARTER_LENGTH_MIN_IDLE_HOURS = "charter-length-min-idle-hours";

	/**
	 * Constant linked to an integer hours of idle time before a forced cooldown is no longer classed as "forced" (i.e. no longer a violation)
	 */
	public static final String COOLDOWN_MIN_IDLE_HOURS = "cooldown-min-idle-hours";

	public static final String OPTIMISE_PAPER_PNL = "optimise-paper-pnl";
	public static final String COMPUTE_EXPOSURES = "compute-exposures";
	public static final String IGNORE_EXPOSURES = "ignore-exposures";
	public static final String EXPOSURES_CUTOFF_AT_PROMPT_START = "exposures-cutoff-at-prompt-start";
	public static final String COMPUTE_PAPER_PNL = "compute-paper-pnl";
	public static final String RE_HEDGE_WITH_PAPERS = "re-hedge-with-papers";
	public static final String RE_HEDGE_CUTOFF_AT_PROMPT_START = "re-hedge-cutoff-at-prompt-start";
	public static final String GENERATED_PAPERS_IN_PNL = "generated-papers-in-pnl";
	public static final String IGNORE_HEDGE_PNL_AT_OPTI = "generated-papers-ignore-at-optimisation";
	public static final String INDIVIDUAL_EXPOSURES = "individual-exposures";
	
	public static final String PROCESS_TRANSFER_MODEL = "process-transfer-model";
	
	public static final String COMMERCIAL_VOLUME_OVERCAPACITY = "COMMERCIAL_VOLUME_OVERCAPACITY";

	
	/**
	 * Ignore small capacity violations e.g. 5 units (which would be 5_000).
	 */
	public static final long CAPACITY_VIOLATION_THRESHOLD = 0L;
	/**
	 * Threshold for the complex "LDD" cargoes
	 */
	public static final int COMPLEX_CARGO_SLOTS_THRESHOLD = 2;

	/*
	 * Injection hints
	 */
	public static final String HINT_DISABLE_CACHES = "hint-lngtransformer-disable-caches";
}
