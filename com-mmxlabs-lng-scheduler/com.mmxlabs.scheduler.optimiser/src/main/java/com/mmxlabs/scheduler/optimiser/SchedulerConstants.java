/**
 * Copyright (C) Minimax Labs Ltd., 2010
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
	public static final String DCP_resourceAllocationProvider = "provider-resource-allocation";
	public static final String DCP_startEndRequirementProvider = "provider-start-end-requirement";
	public static final String DCP_portExclusionProvider = "provider-port-exclusions";
	public static final String DCP_returnElementProvider = "provider-return-elements";
	public static final String DCP_routePriceProvider = "provider-route-prices";
	public static final String DCP_totalVolumeLimitProvider = "provider-total-volume";
	
	// Additional Info keys
	public static final String AI_idleInfo = "info-idle";
	public static final String AI_journeyInfo = "info-journey";
	public static final String AI_visitInfo = "info-visit";
	public static final String AI_volumeAllocationInfo = "info-volume-allocation";
}
