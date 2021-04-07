/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

/**
 * MULL states for comparing allocations before and after applying a MULL change
 * 
 * Each drop type is of the form "firstCase_secondCase"
 * 
 * firstCase (before the underscore) references the state of the monthly
 * entitlement with respect to [-FCL, FCL] *before* allocating the cargo The
 * 
 * secondCase (after the underscore) references the state of the monthly
 * entitlement with respect to [-FCL, FCL] *after* allocating the cargo
 * 
 * @author miten
 *
 */
public enum AllocationDropType {
	ABOVE_ABOVE, ABOVE_IN, ABOVE_BELOW, // points towards a more heavily constrained FCL value
	IN_IN, IN_BELOW, BELOW_BELOW
}
