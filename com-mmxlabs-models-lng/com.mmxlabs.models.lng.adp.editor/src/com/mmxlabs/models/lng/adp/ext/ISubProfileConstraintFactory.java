/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SubProfileConstraint;

public interface ISubProfileConstraintFactory {

	String getName();

	boolean validFor(ContractProfile<?, ?> profile, SubContractProfile<?, ?> subProfile);

	SubProfileConstraint createInstance();
}
