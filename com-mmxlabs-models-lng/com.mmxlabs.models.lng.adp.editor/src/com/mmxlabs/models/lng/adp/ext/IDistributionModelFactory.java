/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.SubContractProfile;

@NonNullByDefault
public interface IDistributionModelFactory {

	String getName();

	default boolean validFor(SubContractProfile<?, ?> profile) {
		return true;
	}

	boolean isMatchForCurrent(SubContractProfile<?, ?> profile);

	DistributionModel createInstance();
}
