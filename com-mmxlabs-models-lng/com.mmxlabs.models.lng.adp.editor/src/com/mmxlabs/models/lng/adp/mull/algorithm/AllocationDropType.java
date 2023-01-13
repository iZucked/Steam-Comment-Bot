/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

/**
 * MULL states for comparing allocations before and after applying a MULL change
 * 
 * Each drop type is of the form "firstCase_secondCase"
 * 
 * firstCase (before the underscore) references the state of the monthly
 * entitlement with respect to [-FCL, FCL] *before* allocating the cargo
 * 
 * secondCase (after the underscore) references the state of the monthly
 * entitlement with respect to [-FCL, FCL] *after* allocating the cargo
 * 
 * @author miten
 *
 */
public enum AllocationDropType {
	ABOVE_ABOVE, ABOVE_IN, ABOVE_BELOW, IN_IN, IN_BELOW, BELOW_BELOW
}
