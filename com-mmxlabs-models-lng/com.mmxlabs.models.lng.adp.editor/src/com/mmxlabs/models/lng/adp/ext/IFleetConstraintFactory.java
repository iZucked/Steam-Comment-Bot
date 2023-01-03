/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import com.mmxlabs.models.lng.adp.FleetConstraint;

public interface IFleetConstraintFactory {

	String getName();

	FleetConstraint createInstance();
}
