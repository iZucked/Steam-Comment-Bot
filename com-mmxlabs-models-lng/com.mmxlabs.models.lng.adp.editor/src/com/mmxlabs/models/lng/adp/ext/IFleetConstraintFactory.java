/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import com.mmxlabs.models.lng.adp.FleetConstraint;

public interface IFleetConstraintFactory {

	String getName();

	FleetConstraint createInstance();
}
