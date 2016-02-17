/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Various constants, such as keys, used within the scheduler.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class SchedulerConstants {

	// Additional Info keys
	public static final @NonNull String AI_idleInfo = "info-idle";
	public static final @NonNull String AI_journeyInfo = "info-journey";
	public static final @NonNull String AI_visitInfo = "info-visit";
	public static final @NonNull String AI_volumeAllocationInfo = "info-volume-allocation";
	public static final @NonNull String AI_cargoValueAllocationInfo = "info-cargo-value-allocation";
	public static final @NonNull String AI_heelLevelInfo = "info-heel-level";
	public static final @NonNull String AI_generatedCharterOutInfo = "info-generated-charter-out";
	public static final @NonNull String AI_capacityViolationInfo = "info-capacity-violation";
	public static final @NonNull String AI_portCostInfo = "info-port-cost";
	public static final @NonNull String AI_hedgingValue = "element-hedging-value";
	public static final @NonNull String AI_cancellationFees = "element-cancellation-fees";
	public static final @NonNull String AI_charterOutProfitAndLoss = "generated-charter-out-profit-and-loss";
	public static final @NonNull String AI_profitAndLoss = "element-profit-and-loss";
	public static final @NonNull String AI_profitAndLossSlotDetails = "element-profit-and-loss-slot-details";
	public static final @NonNull String AI_latenessInfo = "info-lateness";
	public static final @NonNull String AI_similarityDifferences = "info-similarity-changes";

	/**
	 * A key for a per-route fitness map, which should be of type {@code Map<IResource, Map<String, Long>}. Each element in the map should map fitness function names to fitness values.
	 */
	public static final String G_AI_fitnessPerRoute = "general-info-fitness-per-route";

	/**
	 * A key for a per-route fitness map, which should be of type {@code Map<IResource, Map<String, Long>}. Each element in the map should map fitness function names to fitness values.
	 */
	public static final String G_AI_scheduledSequence = "general-info-scheduleSequence";
}
