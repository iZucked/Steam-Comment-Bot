/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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

	// Data Component Provider keys
	public static final String DCP_vesselProvider = "provider-vessels";
	public static final String DCP_portProvider = "provider-ports";
	public static final String DCP_portSlotsProvider = "provider-port-slots";
	public static final String DCP_portDistanceProvider = "provider-port-distances";
	public static final String DCP_timeWindowProvider = "provider-time-windows";
	public static final String DCP_orderedElementsProvider = "provider-ordered-elements";
	public static final String DCP_elementDurationsProvider = "provider-element-durations";
	public static final String DCP_portTypeProvider = "provider-port-type";
	public static final String DCP_portCostProvider = "provider-port-cost";
	public static final String DCP_resourceAllocationProvider = "provider-resource-allocation";
	public static final String DCP_startEndRequirementProvider = "provider-start-end-requirement";
	public static final String DCP_portExclusionProvider = "provider-port-exclusions";
	public static final String DCP_returnElementProvider = "provider-return-elements";
	public static final String DCP_routePriceProvider = "provider-route-prices";
	public static final String DCP_totalVolumeLimitProvider = "provider-total-volume";
	public static final String DCP_discountCurveProvider = "provider-discount-curve";
	public static final String DCP_calculatorProvider = "provider-calculators";

	// Additional Info keys
	public static final String AI_idleInfo = "info-idle";
	public static final String AI_journeyInfo = "info-journey";
	public static final String AI_visitInfo = "info-visit";
	public static final String AI_volumeAllocationInfo = "info-volume-allocation";
	
	public static final String AI_capacityViolationInfo = "info-capacity-violation";

	public static final String G_AI_allocations = "general-info-all-allocations";
	/**
	 * A key for a per-route fitness map, which should be of type {@code Map<IResource, Map<String, Long>}. Each element in the map should map fitness function names to fitness values.
	 */
	public static final String G_AI_fitnessPerRoute = "general-info-fitness-per-route";
	public static final String DCP_optionalElementsProvider = "provider-optional-elements";
}
