/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.ProfileConstraint;

public interface IProfileConstraintFactory {

	String getName();

	boolean validFor(ContractProfile<?, ?> profile);

	ProfileConstraint createInstance();
}
