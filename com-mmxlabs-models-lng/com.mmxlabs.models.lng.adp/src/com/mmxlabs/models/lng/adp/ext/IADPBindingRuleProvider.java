/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;

public interface IADPBindingRuleProvider {
	void generateBindingRules(@NonNull ADPModel adpModel);

}
