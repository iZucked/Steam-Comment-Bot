/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

/**
 * Various constants, such as keys, used within the scheduler.
 * 
 * @author Simon Goodall
 * 
 */
public final class SchedulerConstants {

	// Additional Info keys
	public static final String AI_idleInfo = "info-idle";
	public static final String AI_journeyInfo = "info-journey";
	public static final String AI_visitInfo = "info-visit";
	public static final String AI_volumeAllocationInfo = "info-volume-allocation";
	public static final String AI_heelLevelInfo = "info-heel-level";
	/**
	 * @since 2.0
	 */
	public static final String AI_generatedCharterOutInfo = "info-generated-charter-out";

	public static final String AI_capacityViolationInfo = "info-capacity-violation";
	public static final String AI_portCostInfo = "info-port-cost";

	// public static final String G_AI_allocations = "general-info-all-allocations";
	/**
	 * @since 2.0
	 */
	public static final String AI_profitAndLoss = "element-profit-and-loss";

	public static final String AI_hedgingValue = "element-hedging-value";

	public static final String AI_cancellationFees = "element-cancellation-fees";
	// /**
	// * @since 7.0
	// */
	// public static final String AI_profitAndLossNoTimeCharterRate = "element-profit-and-loss-no-time-charter-rate";
	/**
	 * @since 2.0
	 */
	public static final String AI_charterOutProfitAndLoss = "generated-charter-out-profit-and-loss";
	// /**
	// * @since 7.0
	// */
	// public static final String AI_charterOutProfitAndLossNoTimeCharterRate = "generated-charter-out-profit-and-loss-no-time-charter-rate";
	/**
	 * A key for a per-route fitness map, which should be of type {@code Map<IResource, Map<String, Long>}. Each element in the map should map fitness function names to fitness values.
	 */
	public static final String G_AI_fitnessPerRoute = "general-info-fitness-per-route";
}
